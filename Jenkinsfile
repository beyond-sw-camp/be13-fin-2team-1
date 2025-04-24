pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = 'village1031/gandalp-api'
        DOCKER_CREDENTIALS_ID = 'docker-access'
        EC2_IP = '13.209.96.152'
        EC2_USER = 'ec2-user'
        CONTAINER_NAME = 'gandalpContaioner'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', credentialsId: 'github-token', url: 'git@github.com:beyond-sw-camp/be13-fin-SolitaryDevelopers-Gandalp-BE.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew build'
                sh 'ls -al ./build/libs'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE_NAME}")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        dockerImage.push("${env.BUILD_NUMBER}")
                        dockerImage.push('latest')
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}