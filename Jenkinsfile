@Library('jenkins-shared-library') _

pipeline {
    agent { label 'agent-vm-1' }

    environment {
        DOCKER_USERNAME   = credentials('Docker-User')
        DOCKER_PASSWORD   = credentials('Docker-Password')
        K8S_API_SERVER    = credentials('api-server')
        K8S_TOKEN         = credentials('token')
        DEPLOYMENT_FILE   = 'k8s/deployment.yaml'
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

        // stage('BuildImage') {
        //     steps {
        //         buildImage(env.DOCKER_IMAGE)
        //     }
        // }

        // stage('ScanImage') {
        //     steps {
        //         scanImage(env.DOCKER_IMAGE)
        //     }
        // }

        // stage('PushImage') {
        //     steps {
        //         pushImage(env.DOCKER_IMAGE, env.DOCKER_USERNAME, env.DOCKER_PASSWORD)
        //     }
        // }

        // stage('RemoveImageLocally') {
        //     steps {
        //         removeImage(env.DOCKER_IMAGE)
        //     }
        // }

        stage('DeployOnK8s') {
            steps {
                deployOnK8s(env.K8S_API_SERVER, env.K8S_TOKEN, env.NAMESPACE, env.DOCKER_IMAGE, env.DEPLOYMENT_FILE)
            }
        }
    }
}
