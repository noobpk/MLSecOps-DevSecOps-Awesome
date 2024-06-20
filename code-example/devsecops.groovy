pipeline {
    agent any

    environment {
        //Ubuntu
        SSH_SERVER = '172.17.0.4'
        SSH_USER = 'root'
        CREDENTIALS_ID = '7458c9b4-2e01-4ad5-919f-e5e518f8f3ae'
        GIT_REPO_URL = 'https://github.com/noobpk/MLOps-DevSecOps-Awesome.git'
        GIT_CLONE_DIR = '/tmp/MLOps-DevSecOps-Awesome'
        //Nexus
        MODEL_FILE = 'text_classification_cnn_model.h5'
        ENCODE_FILE = 'label_encoder.pickle'
        TOKENIZE_FILE = 'tokenizer.pickle'
        NEXUS_URL = 'http://172.17.0.3:8081/repository/'
        NEXUS_REPO_PATH = 'mlops/'
        NEXUS_USERNAME = 'admin'
        NEXUS_PASSWORD = 'admin'
    }

    parameters {
        string(name: 'JOB_MLOPS_BUILD_NUMBER', defaultValue: '', description: 'Build number of Job MLOPS')
    }

    stages {
        stage('Clone Git Repository') {
            steps {
                script {
                    // Retrieve the password from Jenkins secret text credentials
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: 'SSH_PASS')]) {
                        def remote = [:]
                        remote.name = 'test'
                        remote.host = SSH_SERVER
                        remote.user = SSH_USER
                        remote.password = SSH_PASS
                        remote.allowAnyHosts = true

                        // Clone the Git repository to the specified directory
                        sshCommand remote: remote, command: """
                        rm -rf ${GIT_CLONE_DIR}
                        git clone ${GIT_REPO_URL} ${GIT_CLONE_DIR}
                        """
                    }
                }
            }
        }

        stage('Load Trained Model from Nexus ') {
            steps {
                script {
                    // Retrieve the password from Jenkins secret text credentials
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: 'SSH_PASS')]) {
                        def remote = [:]
                        remote.name = 'test'
                        remote.host = SSH_SERVER
                        remote.user = SSH_USER
                        remote.password = SSH_PASS
                        remote.allowAnyHosts = true

                        // Construct the download URL
                        def downloadUrl_0 = "${NEXUS_URL}${NEXUS_REPO_PATH}${params.JOB_MLOPS_BUILD_NUMBER}/text_classification_cnn_model.h5"
                        def downloadUrl_1 = "${NEXUS_URL}${NEXUS_REPO_PATH}${params.JOB_MLOPS_BUILD_NUMBER}/label_encoder.pickle"
                        def downloadUrl_2 = "${NEXUS_URL}${NEXUS_REPO_PATH}${params.JOB_MLOPS_BUILD_NUMBER}/tokenizer.pickle"
                        def FILE_PATH = "${GIT_CLONE_DIR}/code-example/docker-image/"
                        // Download artifact
                        sshCommand remote: remote, command: """
                        wget -O ${FILE_PATH}${MODEL_FILE} ${downloadUrl_0}
                        wget -O ${FILE_PATH}${ENCODE_FILE} ${downloadUrl_1}
                        wget -O ${FILE_PATH}${TOKENIZE_FILE} ${downloadUrl_2}
                        """
                    }
                }
            }
        }

        stage('Static analysis security testing') {
            steps {
                script {
                    // Retrieve the password from Jenkins secret text credentials
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: 'SSH_PASS')]) {
                        def remote = [:]
                        remote.name = 'test'
                        remote.host = SSH_SERVER
                        remote.user = SSH_USER
                        remote.password = SSH_PASS
                        remote.allowAnyHosts = true

                        // Install requirements.txt
                        sshCommand remote: remote, command: """
                        curl -fsSL https://raw.githubusercontent.com/ZupIT/horusec/main/deployments/scripts/install.sh | bash -s latest
                        cd ${GIT_CLONE_DIR}/code-example/docker-image
                        /root/horusec start -p "./" -e="true" --disable-docker="true"
                        """
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Retrieve the password from Jenkins secret text credentials
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: 'SSH_PASS')]) {
                        def remote = [:]
                        remote.name = 'test'
                        remote.host = SSH_SERVER
                        remote.user = SSH_USER
                        remote.password = SSH_PASS
                        remote.allowAnyHosts = true

                        // Install requirements.txt
                        sshCommand remote: remote, command: """
                        cd ${GIT_CLONE_DIR}/code-example/image
                        docker build -t text-classification-cnn-model .
                        """
                    }
                }
            }
        }

        stage('Use Job MLOPS Build Number') {
            steps {
                script {
                    // Use the build number of Job A
                    echo "Received build number of Job A: ${params.JOB_MLOPS_BUILD_NUMBER}"
                    
                    // Your steps that use the build number of Job A
                }
            }
        }
    }
}
