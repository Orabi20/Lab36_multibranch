@Library('jenkins-shared-library') _

pipeline {
    agent { label 'agent-vm-1' }

    environment {
        DOCKER_USERNAME = credentials('Docker-User')
        DOCKER_PASSWORD = credentials('Docker-Password')
        DOCKER_IMAGE = "${DOCKER_USERNAME}/jenkins-app-lab35:${BUILD_NUMBER}"
        K8S_API_SERVER = credentials('api-server')
        K8S_TOKEN = credentials('token')
        NAMESPACE = 'ivolve'
        DEPLOYMENT_FILE = 'Jenkins/Lab35_sharedlib_agent/deployment.yaml'
    }

    stages {
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

