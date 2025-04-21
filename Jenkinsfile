pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = 'village1031/gandalp-api'
        DOCKER_CREDENTIALS_ID = 'docker-access'
        EC2_IP = '15.164.100.123'
        EC2_USER = 'ec2-user'
        CONTAINER_NAME = 'gandalpContaioner'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', credentialsId: 'github-token', url: 'git@github.com:beyond-sw-camp/be13-fin-2team-1.git'
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

        stage('Deploy to EC2') {
            steps {
                sshagent (credentials: ['ec2-access-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_IP} << 'EOF'
                        sudo docker pull ${DOCKER_IMAGE_NAME}:latest
                        sudo docker stop ${CONTAINER_NAME} || true
                        sudo docker rm ${CONTAINER_NAME} || true
                        sudo docker run -d --name ${CONTAINER_NAME} -p 8080:8080 ${DOCKER_IMAGE_NAME}:latest

                        IMAGES=\$(sudo docker images -f 'dangling=true' -q)
                        if [ -n "\$IMAGES" ]; then
                            sudo docker rmi -f \$IMAGES
                        else
                            echo "No dangling images to remove."
                        fi
EOF
                    """
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