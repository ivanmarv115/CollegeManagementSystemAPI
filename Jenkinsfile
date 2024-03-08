pipeline {
    agent any
    tools {
        maven '3.9.6'
    }
    stages{
/*
        stage('Compile'){
            steps{
                sh 'mvn clean package'
            }
        }

        stage('Quality Analysis'){
            steps{
                sh ''' mvn sonar:sonar -Dsonar.host.url=http://172.18.0.3:9000/ \
                        -Dsonar.login=squ_39179cdc97e2ba3a4d0bb1f3b0457963c494775c \
                        -Dsonar.projectName=collegeTest \
                        -Dsonar.java.binaries=. \
                        -Dsonar.projectKey=collegeTest
                    '''
            }
        }

        stage('Build and Push'){
            steps{
                script{
                    sh 'docker build -t ivanmarv/college-api .'
                }
                withCredentials([usernamePassword(credentialsId: 'ivanmarvdocker', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                }
                script{
                   sh 'docker push ivanmarv/college-api'
                }
            }
        }
 */

        stage('Deploy'){
            steps{
                sshagent(['18.231.52.168']){
                    sh """
                        ssh -o UserKnownHostsFile=/var/jenkins_home/.ssh/known_hosts ec2-user@ec2-18-229-158-111.sa-east-1.compute.amazonaws.com "
                            docker run -d -p 8080:8080 --name college-api ivanmarv/college-api
                        "
                    """
                }
            }
        }

    }
    post {
        always {
            sh 'docker logout'
            emailext body: 'Test mail',
            subject: 'Test',
            recipient: $DEFAULT_RECIPIENTS,
        }
    }
}
