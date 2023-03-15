pipeline {
    agent any
    stages {
        stage('Stage 1') {
            steps {
                echo 'Hello world!'
            }
        }
         stage('编译构建') {
             steps {
                 sh label: '', script: 'mvn clean package'
             }
         }
         stage('项目部署') {
            steps {
                 echo '项目部署'
            }
         }
    }
}