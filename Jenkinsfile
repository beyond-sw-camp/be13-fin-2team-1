pipeline {
    stages {
        stage('Gradle Build') {
            steps {
                container('maven') {
                    sh 'chmod +x gradlew && ./gradlew clean build'
                    sh 'ls -al'
                }
            }
        }
}
