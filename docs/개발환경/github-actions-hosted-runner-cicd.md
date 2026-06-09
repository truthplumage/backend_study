# GitHub Actions 기본 Runner CI/CD 방식

## 1. 개념

GitHub Actions 기본 Runner는 GitHub가 제공하는 서버에서 workflow를 실행하는 방식이다.

```yaml
runs-on: ubuntu-latest
```

Self-hosted Runner처럼 내 PC에서 실행되는 것이 아니라 GitHub 서버에서 실행된다.

---

## 2. 전체 흐름

기본 Runner는 내 로컬 Minikube에 직접 접근할 수 없다.

그래서 보통 Docker Registry를 중간에 둔다.

```text
GitHub push
  ↓
GitHub Actions ubuntu-latest
  ↓
Gradle Build
  ↓
Docker Image Build
  ↓
Docker Hub 또는 GHCR Push
  ↓
Kubernetes에서 Image Pull
  ↓
배포
```

---

## 3. Self-hosted Runner 방식과 차이

| 구분 | GitHub 기본 Runner | Self-hosted Runner |
|---|---|---|
| 실행 위치 | GitHub 서버 | 내 PC/서버 |
| `runs-on` | `ubuntu-latest` | `self-hosted` |
| 로컬 Minikube 접근 | 불가능 | 가능 |
| Docker 이미지 전달 | Registry 필요 | `minikube image load` 가능 |
| 관리 부담 | 낮음 | Runner 직접 관리 |

---

## 4. 사용하기 좋은 경우

```text
1. 빌드와 테스트만 자동화할 때
2. Docker 이미지를 Registry에 push할 때
3. 운영 Kubernetes가 외부 Registry에서 이미지를 pull할 수 있을 때
4. 내 PC를 CI/CD 서버로 쓰고 싶지 않을 때
```

---

## 5. Workflow 예시

Docker Hub에 이미지를 push하는 예시다.

```yaml
name: demo-service build and push

on:
  workflow_dispatch:
  push:
    branches:
      - main

env:
  IMAGE_NAME: demo-service

jobs:
  build-and-push:
    runs-on: ubuntu-latest

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

      - name: Login to Docker Hub
        uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKERHUB_USERNAME }}
          password: ${{ secrets.DOCKERHUB_TOKEN }}

      - name: Build Docker image
        run: docker build -t ${{ secrets.DOCKERHUB_USERNAME }}/$IMAGE_NAME:${{ github.sha }} .

      - name: Push Docker image
        run: docker push ${{ secrets.DOCKERHUB_USERNAME }}/$IMAGE_NAME:${{ github.sha }}
```

---

## 6. GitHub Secrets 설정

Docker Hub 로그인 정보는 코드에 직접 쓰면 안 된다.

GitHub Repository에서:

```text
Settings
  → Secrets and variables
  → Actions
  → New repository secret
```

추가할 값:

```text
DOCKERHUB_USERNAME
DOCKERHUB_TOKEN
```

---

## 7. Kubernetes Deployment 이미지 설정

Registry에 push한 이미지를 Kubernetes에서 pull하게 한다.

```yaml
containers:
  - name: demo-service
    image: dockerhub계정명/demo-service:이미지태그
    imagePullPolicy: IfNotPresent
```

예:

```yaml
containers:
  - name: demo-service
    image: parkjinwoo/demo-service:abc123
    imagePullPolicy: IfNotPresent
```

운영에서는 보통 고정 태그보다 commit sha 태그를 쓴다.

---

## 8. 배포까지 자동화하려면

GitHub 기본 Runner만으로 로컬 Minikube에 `kubectl apply`는 할 수 없다.

대신 아래 방식 중 하나를 사용한다.

```text
1. Kubernetes 서버가 외부에서 접근 가능한 경우 kubeconfig secret으로 배포
2. Argo CD가 Git 변경을 감지해서 배포
3. Jenkins나 Self-hosted Runner가 내부망에서 배포
```

로컬 Minikube 실습이라면 3번이 가장 단순하다.

---

## 9. GHCR 사용 예시

Docker Hub 대신 GitHub Container Registry를 쓸 수도 있다.

```yaml
name: demo-service build and push ghcr

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
        run: docker build -t $IMAGE_NAME:${{ github.sha }} .

      - name: Push Docker image
        run: docker push $IMAGE_NAME:${{ github.sha }}
```

---

## 10. 최종 요약

```text
GitHub 기본 Runner:
  GitHub 서버에서 build/test/image push 실행

Self-hosted Runner:
  내 PC/서버에서 build/deploy 실행

Jenkins:
  Jenkins 서버에서 pipeline 실행
```

로컬 Minikube에 바로 배포하려면 기본 Runner만으로는 부족하다.

기본 Runner는 Registry에 이미지를 올리는 용도로 쓰는 것이 가장 자연스럽다.
