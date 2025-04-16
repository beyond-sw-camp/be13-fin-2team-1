pipeline {
    agent any

    stages {
        stage('Gradle Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build'
                sh 'ls -al build/libs'
            }
        }
    }

    post {
        success {
            echo '✅ Gradle Build 완료!'
            archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
        }
        failure {
            echo '❌ Gradle Build 실패... 확인이 필요합니다.'
        }
    }
}