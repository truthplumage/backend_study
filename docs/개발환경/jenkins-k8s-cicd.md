# Jenkins를 활용한 Kubernetes CI/CD 방식

## 1. 개념

Jenkins는 GitHub Actions의 Self-hosted Runner처럼 내 PC나 서버에서 빌드/배포 작업을 실행할 수 있는 CI/CD 도구다.

차이는 GitHub Actions는 GitHub 안의 workflow가 중심이고, Jenkins는 별도 Jenkins 서버가 중심이라는 점이다.

```text
GitHub push
  ↓
Jenkins Webhook
  ↓
Jenkins Pipeline 실행
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

## 2. Jenkins를 쓰는 이유

Jenkins를 쓰면 GitHub Actions 없이도 CI/CD를 구성할 수 있다.

특히 아래 상황에서 많이 쓴다.

```text
1. 회사 내부 서버에서 CI/CD를 직접 운영하고 싶을 때
2. GitHub 외 GitLab, Bitbucket 등 여러 저장소를 연결하고 싶을 때
3. 복잡한 배포 파이프라인을 Jenkinsfile로 관리하고 싶을 때
4. 로컬 Minikube나 내부 Kubernetes에 직접 배포하고 싶을 때
```

---

## 3. GitHub Actions와 차이

| 구분 | GitHub Actions | Jenkins |
|---|---|---|
| 실행 주체 | GitHub Runner | Jenkins 서버 |
| 설정 파일 | `.github/workflows/*.yml` | `Jenkinsfile` |
| 설치 | GitHub 기본 제공, self-hosted runner 선택 | Jenkins 서버 직접 설치 |
| GitHub 연동 | 기본 지원 | Webhook/Plugin 설정 필요 |
| 로컬 Minikube 접근 | self-hosted runner 필요 | Jenkins가 로컬/서버에서 실행되면 가능 |

---

## 4. Jenkins 설치

macOS Homebrew 기준:

```bash
brew install jenkins-lts
brew services start jenkins-lts
```

접속:

```text
http://localhost:8080
```

초기 비밀번호 확인:

```bash
cat ~/.jenkins/secrets/initialAdminPassword
```

---

## 5. Jenkins 서버에 필요한 도구

Jenkins가 실행되는 PC 또는 서버에 아래 도구가 있어야 한다.

```bash
java -version
docker version
minikube version
kubectl version --client
```

현재 프로젝트 기준:

```text
Java 17
Docker
Minikube
kubectl
Gradle Wrapper
```

---

## 6. Jenkinsfile 위치

프로젝트 루트에 둔다.

```text
Jenkinsfile
```

---

## 7. Jenkinsfile 예시

로컬 Minikube 배포 기준 예시:

```groovy
pipeline {
    agent any

    environment {
        IMAGE_NAME = 'demo-service'
        IMAGE_TAG = 'k8s'
        NAMESPACE = 'msa'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Jar') {
            steps {
                sh './gradlew clean bootJar'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME:$IMAGE_TAG .'
            }
        }

        stage('Load Image To Minikube') {
            steps {
                sh 'minikube image load $IMAGE_NAME:$IMAGE_TAG'
            }
        }

        stage('Deploy To Kubernetes') {
            steps {
                sh 'kubectl apply -k k8s/demo-service'
            }
        }

        stage('Check Rollout') {
            steps {
                sh 'kubectl rollout status deployment/demo-service -n $NAMESPACE'
            }
        }
    }
}
```

---

## 8. Jenkins Job 생성

Jenkins 화면에서:

```text
New Item
  → Pipeline 선택
  → 이름 입력
  → Pipeline script from SCM 선택
  → SCM: Git
  → Repository URL 입력
  → Branch: main
  → Script Path: Jenkinsfile
```

저장 후 `Build Now`를 누르면 실행된다.

---

## 9. GitHub Webhook 연결

GitHub Repository에서:

```text
Settings
  → Webhooks
  → Add webhook
```

Payload URL:

```text
http://JENKINS주소/github-webhook/
```

Content type:

```text
application/json
```

Trigger:

```text
Just the push event
```

Jenkins Job에서는 `GitHub hook trigger for GITScm polling`을 체크한다.

---

## 10. 배포 확인

```bash
kubectl get pods -n msa
kubectl get svc -n msa
kubectl rollout status deployment/demo-service -n msa
```

접속 확인:

```bash
curl http://$(minikube ip):30080/swagger-ui/index.html
```

Mac + Docker Driver에서 NodePort 접속이 안 되면:

```bash
kubectl port-forward svc/demo-service 8080:80 -n msa
```

---

## 11. 주의할 점

### 11.1 Jenkins가 Docker를 실행할 수 있어야 한다

Jenkins 프로세스가 Docker 권한이 없으면 `docker build`가 실패한다.

macOS에서 Jenkins를 같은 사용자로 실행하면 보통 문제가 적다.

### 11.2 Jenkins가 kubectl context를 알아야 한다

Jenkins가 실행되는 사용자 기준으로 kubectl 설정이 있어야 한다.

```bash
kubectl config current-context
```

Minikube라면 보통:

```text
minikube
```

### 11.3 이미지 태그가 Deployment와 같아야 한다

Jenkinsfile:

```bash
docker build -t demo-service:k8s .
minikube image load demo-service:k8s
```

Deployment:

```yaml
image: demo-service:k8s
imagePullPolicy: Never
```

---

## 12. 최종 요약

```text
GitHub Actions 방식:
  GitHub workflow + self-hosted runner

Jenkins 방식:
  Jenkins 서버 + Jenkinsfile
```

로컬 Minikube 실습에서는 둘 다 가능하다.

Jenkins를 쓰면 GitHub Actions 없이도 내 PC나 서버에서 빌드와 배포를 자동화할 수 있다.
