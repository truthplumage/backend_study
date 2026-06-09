# GitHub Actions를 활용한 Kubernetes CI/CD 방식

## 1. 개념

GitHub Actions는 GitHub Repository에 push가 발생했을 때 자동으로 빌드, 테스트, 배포 작업을 실행하는 기능이다.

현재 프로젝트에서는 GitHub Actions와 Self-hosted Runner를 함께 사용해서 로컬 Minikube에 배포할 수 있다.

```text
GitHub push
  ↓
GitHub Actions Workflow 실행
  ↓
Self-hosted Runner
  ↓
Gradle Build
  ↓
Docker Image Build
  ↓
Minikube Image Load
  ↓
kubectl apply
```

---

## 2. 왜 Self-hosted Runner와 같이 쓰는가?

GitHub 기본 Runner인 `ubuntu-latest`는 GitHub 서버에서 실행된다.

그래서 내 PC의 Docker, Minikube, Kubernetes에 직접 접근할 수 없다.

로컬 Minikube에 배포하려면 Runner가 내 PC 또는 내 서버에서 실행되어야 한다.

```yaml
runs-on: self-hosted
```

---

## 3. Workflow 파일 위치

GitHub Actions workflow는 아래 위치에 둔다.

```text
.github/workflows/demo-service-k8s.yml
```

---

## 4. 추천 Workflow

로컬 Minikube에 배포하는 경우에는 build와 deploy를 모두 `self-hosted`에서 실행하는 것이 단순하다.

```yaml
name: demo-service k8s deploy

on:
  workflow_dispatch:
  push:
    branches:
      - main

env:
  IMAGE_NAME: demo-service
  IMAGE_TAG: k8s

jobs:
  deploy:
    runs-on: self-hosted

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

      - name: Build Docker image
        run: docker build -t $IMAGE_NAME:$IMAGE_TAG .

      - name: Load image to Minikube
        run: minikube image load $IMAGE_NAME:$IMAGE_TAG

      - name: Apply Kubernetes manifests
        run: kubectl apply -k k8s/demo-service

      - name: Check rollout
        run: kubectl rollout status deployment/demo-service -n msa
```

---

## 5. 실행 조건

Self-hosted Runner가 설치된 PC에는 아래 도구가 있어야 한다.

```bash
java -version
docker version
minikube version
kubectl version --client
```

Minikube도 실행 중이어야 한다.

```bash
minikube start
```

---

## 6. Workflow 실행 방식

### 6.1 Push로 자동 실행

`main` 브랜치에 push하면 자동 실행된다.

```yaml
on:
  push:
    branches:
      - main
```

### 6.2 수동 실행

GitHub 화면에서 직접 실행할 수도 있다.

```yaml
on:
  workflow_dispatch:
```

GitHub에서:

```text
Actions
  → demo-service k8s deploy
  → Run workflow
```

---

## 7. 배포 확인

Workflow가 끝난 뒤 로컬에서 확인한다.

```bash
kubectl get pods -n msa
kubectl get svc -n msa
kubectl rollout status deployment/demo-service -n msa
```

NodePort 확인:

```bash
minikube ip
curl http://$(minikube ip):30080/swagger-ui/index.html
```

Mac + Docker Driver에서 NodePort 접속이 안 되면 port-forward를 사용한다.

```bash
kubectl port-forward svc/demo-service 8080:80 -n msa
```

---

## 8. 주의할 점

### 8.1 build와 deploy Runner가 다르면 이미지가 안 보일 수 있다

예를 들어 `ubuntu-latest`에서 Docker 이미지를 만들고, `self-hosted`에서 `kubectl apply`만 하면 Self-hosted Runner의 Minikube에는 이미지가 없을 수 있다.

로컬 Minikube 방식에서는 아래 둘 중 하나를 선택한다.

```text
1. build, docker build, minikube image load, kubectl apply를 모두 self-hosted에서 실행
2. Docker Hub/GHCR 같은 Registry에 push한 뒤 Kubernetes에서 pull
```

실습용 로컬 Minikube는 1번이 더 쉽다.

### 8.2 Deployment 이미지명과 Workflow 이미지명이 같아야 한다

Workflow:

```bash
docker build -t demo-service:k8s .
minikube image load demo-service:k8s
```

Deployment:

```yaml
containers:
  - name: demo-service
    image: demo-service:k8s
    imagePullPolicy: Never
```

### 8.3 Runner가 꺼져 있으면 실행되지 않는다

Self-hosted Runner 상태가 `Offline`이면 GitHub Actions job이 대기하거나 실패한다.

GitHub에서 확인:

```text
Settings
  → Actions
  → Runners
```

---

## 9. 현재 프로젝트 기준 요약

```text
1. main 브랜치에 push
2. GitHub Actions 실행
3. Self-hosted Runner가 코드 checkout
4. Gradle bootJar 실행
5. Docker image 생성
6. Minikube에 image load
7. k8s/demo-service manifest 적용
8. rollout 상태 확인
```

핵심은 `runs-on: self-hosted`에서 Minikube 관련 명령을 실행하는 것이다.
