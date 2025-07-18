# Lab 36: Multi-branch CI/CD Workflow

This lab demonstrates a Jenkins CI/CD pipeline for a multi-branch GitHub repository using a shared library to automate Docker image building, scanning, pushing, and Kubernetes deployment.

---

## 🗂️ Project Structure

```
.
├── Jenkinsfile
├── deployment.yaml
├── Jenkinsfile
└── Dockerfile
```

---

## 🧪 Branches and Namespaces

| Git Branch | Kubernetes Namespace |
|------------|----------------------|
| `main`     | `main`               |
| `stag`     | `stag`               |
| `dev`      | `dev`                |

Each branch has its own CI/CD pipeline and deploys independently to its matching Kubernetes namespace.

---

## 🔧 Prerequisites

- Jenkins with:
  - Multibranch Pipeline Plugin
  - Docker and Kubernetes CLI installed
  - Agent node labeled `agent-vm-1`
- GitHub repository with:
  - `Jenkinsfile` at the root of each branch
  - `deployment.yaml` present
- DockerHub account for image registry
- Kubernetes cluster with:
  - `main`, `stag`, `dev` namespaces (auto-created by pipeline if missing)
- Jenkins Credentials:
  - `Docker-User` and `Docker-Password`
  - `api-server` and `token` for Kubernetes API access

---

## 🧩 Jenkins Shared Library

### 📁 `vars/`

Each of the following files should be in the `vars/` folder of your shared library:

- `runUnitTests.groovy`
- `buildApp.groovy`
- `buildImage.groovy`
- `scanImage.groovy`
- `pushImage.groovy`
- `removeImage.groovy`
- `deployOnK8s.groovy`

These functions are used in the pipeline and keep logic modular and reusable.

---

## 📦 Docker Image

The pipeline dynamically builds the Docker image as:

```text
DOCKER_IMAGE = <DOCKER_USERNAME>/jenkins-app:<BRANCH_NAME>-<BUILD_NUMBER>
```

Example:
```
orabi20/jenkins-app:stag-42
```

---

## ⚙️ Jenkinsfile Overview

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent { label 'agent-vm-1' }

    environment {
        DOCKER_USERNAME   = credentials('Docker-User')
        DOCKER_PASSWORD   = credentials('Docker-Password')
        K8S_API_SERVER    = credentials('api-server')
        K8S_TOKEN         = credentials('token2')
        DEPLOYMENT_FILE   = 'deployment.yaml'
    }

    stages {
        stage('SetNamespace') {
            steps {
                script {
                    env.NAMESPACE    = env.BRANCH_NAME
                    env.DOCKER_IMAGE = "${env.DOCKER_USERNAME}/jenkins-app:${env.BRANCH_NAME}-${BUILD_NUMBER}"
                    echo "Branch: ${env.BRANCH_NAME}"
                    echo "Namespace: ${env.NAMESPACE}"
                    echo "Image: ${env.DOCKER_IMAGE}"
                }
            }
        }

        stage('RunUnitTest') {
            steps {
                runUnitTests()
            }
        }

        stage('BuildApp') {
            steps {
                buildApp()
            }
        }

        stage('BuildImage') {
            steps {
                buildImage(env.DOCKER_IMAGE)
            }
        }

        stage('ScanImage') {
            steps {
                scanImage(env.DOCKER_IMAGE)
            }
        }

        stage('PushImage') {
            steps {
                pushImage(env.DOCKER_IMAGE, env.DOCKER_USERNAME, env.DOCKER_PASSWORD)
            }
        }

        stage('RemoveImageLocally') {
            steps {
                removeImage(env.DOCKER_IMAGE)
            }
        }

        stage('DeployOnK8s') {
            steps {
                deployOnK8s(env.K8S_API_SERVER, env.K8S_TOKEN, env.NAMESPACE, env.DOCKER_IMAGE, env.DEPLOYMENT_FILE)
            }
        }
    }
}
```

---

## 📄 Sample `deployment.yaml`

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: jenkins-app
  labels:
    app: jenkins-app
spec:
  replicas: 1
  selector:
    matchLabels:
      app: jenkins-app
  template:
    metadata:
      labels:
        app: jenkins-app
    spec:
      containers:
        - name: jenkins-app
          image: REPLACE_IMAGE  # Replaced dynamically
          ports:
            - containerPort: 8080
      tolerations:
        - key: "workload"
          operator: "Equal"
          value: "app"
          effect: "NoSchedule"
```
<img width="959" height="431" alt="36 4" src="https://github.com/user-attachments/assets/caff486b-a1a6-4301-b9f5-1f5be10180fe" />

---


## ✅ Result

After pushing to any of the branches (`main`, `stag`, `dev`), Jenkins:

1. Builds the app
2. Builds and scans Docker image
3. Pushes to DockerHub
4. Deploys to Kubernetes in the matching namespace

<img width="959" height="430" alt="image" src="https://github.com/user-attachments/assets/a02a8c70-411d-487e-8ba1-6ec8cd57d792" />

<img width="955" height="90" alt="36 2" src="https://github.com/user-attachments/assets/21b9dd30-f078-424c-b712-8e307e913bfe" />


<img width="959" height="434" alt="image" src="https://github.com/user-attachments/assets/07e12716-d81d-4f94-a345-d2cdcf4fb556" />

<img width="952" height="89" alt="36 3" src="https://github.com/user-attachments/assets/5322331d-0bc2-415a-aa8a-d1c83b367d52" />


<img width="959" height="428" alt="36 6" src="https://github.com/user-attachments/assets/84395911-cadf-42ef-acb6-614e22ddd95a" />

<img width="951" height="99" alt="36 1" src="https://github.com/user-attachments/assets/22cdbc10-6204-446f-a11b-2829aed9bffd" />






---

