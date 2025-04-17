pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = 'village1031/schocolla-api' // Docker Hub에 저장할 이미지 이름
        DOCKER_CREDENTIALS_ID = 'docker-access' // Docker Hub 자격 증명 ID
        EC2_IP = '52.78.55.230' // EC2 인스턴스 IP 주소
        EC2_USER = 'ec2-user' // EC2 인스턴스 사용자 ex ubuntu
        CONTAINER_NAME = 'schocollaContaioner' // 컨테이너 이름 ex springContainer
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', credentialsId: 'village1031', url: 'git@github.com:ChaGyoungtae/be13-fin-2team-1.git'
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
                sshagent (credentials: ['github-access-key']) { // 'aws_key'는 Jenkins에 저장된 SSH 자격 증명 ID
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
