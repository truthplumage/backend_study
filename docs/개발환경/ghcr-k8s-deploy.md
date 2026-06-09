# GHCR을 활용한 Kubernetes 배포 방식

## 1. 개념

GHCR은 GitHub Container Registry다.

Docker 이미지를 GitHub Packages에 올리고, Kubernetes가 그 이미지를 pull해서 배포하는 방식이다.

```text
GitHub push
  ↓
GitHub Actions
  ↓
Docker image build
  ↓
GHCR push
  ↓
Kubernetes image pull
  ↓
Pod 실행
```

---

## 2. 왜 GHCR을 쓰는가?

Self-hosted Runner 없이도 GitHub Actions 기본 Runner에서 이미지를 만들 수 있다.

```text
GitHub Actions ubuntu-latest
  ↓
ghcr.io에 이미지 push
  ↓
Kubernetes에서 ghcr.io 이미지 pull
```

단, 로컬 Minikube에 `kubectl apply`까지 자동으로 하려면 Jenkins나 Self-hosted Runner가 필요하다.

---

## 3. GitHub Actions Workflow

파일 위치:

```text
.github/workflows/demo-service-ghcr.yml
```

예시:

```yaml
name: demo-service ghcr build

on:
  workflow_dispatch:
  push:
    branches:
      - main

env:
  IMAGE_NAME: ghcr.io/${{ github.repository_owner }}/demo-service

jobs:
  build-and-push:
    runs-on: ubuntu-latest

    permissions:
      contents: read
      packages: write

    steps:
      - name: Checkout source
        uses: actions/checkout@v4

      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: "17"

      - name: Build jar
        run: ./gradlew clean bootJar

      - name: Login to GHCR
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}

      - name: Build Docker image
        run: docker build -t $IMAGE_NAME:${{ github.sha }} -t $IMAGE_NAME:latest .

      - name: Push Docker image
        run: |
          docker push $IMAGE_NAME:${{ github.sha }}
          docker push $IMAGE_NAME:latest
```

---

## 4. 이미지 주소

GHCR 이미지 주소 형식:

```text
ghcr.io/깃허브계정명/demo-service:태그
```

예:

```text
ghcr.io/parkjinwoo/demo-service:latest
ghcr.io/parkjinwoo/demo-service:abc123
```

운영에서는 `latest`보다 commit sha 태그를 쓰는 것이 더 안전하다.

---

## 5. Kubernetes Deployment 수정

현재 로컬 이미지 방식:

```yaml
image: demo-service:k8s
imagePullPolicy: Never
```

GHCR 방식:

```yaml
image: ghcr.io/parkjinwoo/demo-service:latest
imagePullPolicy: Always
```

예시:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: demo-service
  namespace: msa
spec:
  replicas: 1
  selector:
    matchLabels:
      app: demo-service
  template:
    metadata:
      labels:
        app: demo-service
    spec:
      containers:
        - name: demo-service
          image: ghcr.io/parkjinwoo/demo-service:latest
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
```

---

## 6. Private GHCR 이미지 pull 설정

GHCR Package가 private이면 Kubernetes에 인증 정보가 필요하다.

GitHub Personal Access Token을 만든다.

필요 권한:

```text
read:packages
```

Secret 생성:

```bash
kubectl create secret docker-registry ghcr-secret \
  --namespace msa \
  --docker-server=ghcr.io \
  --docker-username=깃허브계정명 \
  --docker-password=깃허브PAT \
  --docker-email=이메일
```

Deployment에 `imagePullSecrets`를 추가한다.

```yaml
spec:
  template:
    spec:
      imagePullSecrets:
        - name: ghcr-secret
      containers:
        - name: demo-service
          image: ghcr.io/parkjinwoo/demo-service:latest
```

Public package라면 `imagePullSecrets` 없이도 pull 가능하다.

---

## 7. 배포 명령

이미지를 GHCR에 push한 뒤 Kubernetes manifest를 적용한다.

```bash
kubectl apply -k k8s/demo-service
kubectl rollout status deployment/demo-service -n msa
```

확인:

```bash
kubectl get pods -n msa
kubectl describe pod -n msa
```

이미지 pull 실패가 나면 보통 아래 중 하나다.

```text
1. 이미지 주소가 틀림
2. Package가 private인데 imagePullSecrets가 없음
3. PAT 권한에 read:packages가 없음
4. 이미지 태그가 존재하지 않음
```

---

## 8. GitOps 방식으로 확장

GitHub Actions 기본 Runner는 로컬 Minikube에 직접 `kubectl apply`를 할 수 없다.

그래서 GHCR 방식은 보통 아래처럼 확장한다.

```text
1. GitHub Actions가 GHCR에 이미지 push
2. manifest의 image tag를 변경
3. Argo CD가 Git 변경을 감지
4. Kubernetes에 자동 배포
```

로컬 실습에서는 GitHub Actions로 GHCR push까지만 하고, 배포는 직접 `kubectl apply`해도 된다.

---

## 9. 최종 요약

```text
Self-hosted Runner 방식:
  로컬에서 docker build
  minikube image load
  kubectl apply

GHCR 방식:
  GitHub Actions에서 docker build
  ghcr.io push
  Kubernetes가 image pull
```

GHCR을 쓰면 이미지를 로컬 Minikube에 직접 load하지 않아도 된다.
