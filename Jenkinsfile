@Library('jenkins-shared-library') _

pipeline {
    agent { label 'agent-vm-1' }

    environment {
        DOCKER_USERNAME   = credentials('Docker-User')
        DOCKER_PASSWORD   = credentials('Docker-Password')
        K8S_API_SERVER    = credentials('api-server')
        K8S_TOKEN         = credentials('token')
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
                ru
