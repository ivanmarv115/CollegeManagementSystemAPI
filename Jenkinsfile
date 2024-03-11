pipeline {
    agent any
    tools {
        maven '3.9.6'
    }

    environment{
        PROJECT_NAME = 'collegeTest'
        IMG_NAME = 'ivanmarv/college-api'
        CONTAINER_NAME = 'college_api'
        AWS_USER = 'ec2-user'
        AWS_HOST = 'ec2-18-228-39-170.sa-east-1.compute.amazonaws.com'
        SONAR_LOGIN = 'squ_39179cdc97e2ba3a4d0bb1f3b0457963c494775c'
        SONAR_URL = 'http://172.18.0.4:9000/'
    }

    stages{

        stage('Compile'){
            steps{
                sh 'mvn clean package'
            }
        }

        stage('Quality Analysis'){
            steps{
                sh ''' mvn sonar:sonar -Dsonar.host.url=$SONAR_URL \
                        -Dsonar.login=$SONAR_LOGIN \
                        -Dsonar.projectName=$PROJECT_NAME \
                        -Dsonar.java.binaries=. \
                        -Dsonar.projectKey=$PROJECT_NAME
                    '''
            }
        }

        stage('Build and Push'){
            steps{
                script{
                    sh 'docker build -t $IMG_NAME .'
                }
                withCredentials([usernamePassword(credentialsId: 'ivanmarvdocker', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                }
                script{
                   sh 'docker push $IMG_NAME'
                }
            }
        }


        stage('Deploy'){
            steps{
                sshagent(['18.231.52.168']){
                    sh """
                        ssh -o UserKnownHostsFile=/var/jenkins_home/.ssh/known_hosts $AWS_USER@$AWS_HOST "
                            docker stop $CONTAINER_NAME || true
                            docker rm $CONTAINER_NAME || true
                            docker run -d -p 8080:8080 --name $IMG_NAME $CONTAINER_NAME
                        "
                    """
                }
            }
        }

    }
    post {
        always {
            sh 'docker logout'
            emailext body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT', to: 'ivan.martinez@itti.digital',
                mimeType: 'text/html'
        }
    }
}
