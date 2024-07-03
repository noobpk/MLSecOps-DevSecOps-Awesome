pipeline {
    agent any

    environment {
        //Ubuntu-Train
        SSH_SERVER = '172.17.0.2'
        SSH_USER = 'root'
        CREDENTIALS_ID = '7458c9b4-2e01-4ad5-919f-e5e518f8f3ae'
        IMAGE_DIR = '/tmp/image/'
        GIT_REPO_URL = 'https://github.com/noobpk/MLOps-DevSecOps-Awesome.git'
        GIT_CLONE_DIR = '/tmp/MLOps-DevSecOps-Awesome'
        //Nexus
        MODEL_FILE = 'text_classification_cnn_model.h5'
        ENCODE_FILE = 'label_encoder.pickle'
        TOKENIZE_FILE = 'tokenizer.pickle'
        NEXUS_URL = 'http://172.17.0.3:8081/repository/'
        NEXUS_REPO_PATH_MLOPS = 'mlops/'
        NEXUS_REPO_PATH_DEVSECOPS = 'devsecops/'
        NEXUS_CREDENTIALS_ID = '07967224-9f64-4e00-957c-d699e150a07b'
        IMAGE_EXPORT_FILE = 'text-classification-cnn-model.tar'
        //Ubuntu-Deploy
        APP_URL = 'http://172.17.0.5:5000'
    }

    parameters {
        string(name: 'JOB_MLOPS_BUILD_NUMBER', defaultValue: '', description: 'Build number of Job MLOPS')
    }

    stages {
        stage('Fetch Git Repository') {
            steps {
                script {
                    git url: ${GIT_REPO_URL}, branch: "main"
                    // Retrieve the password from Jenkins secret text credentials
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: 'SSH_PASS')]) {
                        def remote = [:]
                        remote.name = 'Ubuntu-Train'
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
                        remote.name = 'Ubuntu-Train'
                        remote.host = SSH_SERVER
                        remote.user = SSH_USER
                        remote.password = SSH_PASS
                        remote.allowAnyHosts = true

                        // Construct the download URL
                        def downloadUrl_0 = "${NEXUS_URL}${NEXUS_REPO_PATH_MLOPS}${params.JOB_MLOPS_BUILD_NUMBER}/text_classification_cnn_model.h5"
                        def downloadUrl_1 = "${NEXUS_URL}${NEXUS_REPO_PATH_MLOPS}${params.JOB_MLOPS_BUILD_NUMBER}/label_encoder.pickle"
                        def downloadUrl_2 = "${NEXUS_URL}${NEXUS_REPO_PATH_MLOPS}${params.JOB_MLOPS_BUILD_NUMBER}/tokenizer.pickle"
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

        stage('SonarQube Code Analysis') {
            environment {
                scannerHome = tool 'Sonar'
            }
            steps {
                script {
                    withSonarQubeEnv('Sonar') {
                        sh "${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=MLOps-DevSecOps-Awesome \
                            -Dsonar.projectName=MLOps-DevSecOps-Awesome \
                            -Dsonar.projectVersion=1.0 \
                            -Dsonar.sources=."
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
                        remote.name = 'Ubuntu-Train'
                        remote.host = SSH_SERVER
                        remote.user = SSH_USER
                        remote.password = SSH_PASS
                        remote.allowAnyHosts = true

                        // Scan with trivy
                        sshCommand remote: remote, command: """
                        cd ${GIT_CLONE_DIR}/code-example/docker-image
                        trivy fs .
                        """
                        // Scan with horusec
                        sshCommand remote: remote, command: """
                        curl -fsSL https://raw.githubusercontent.com/ZupIT/horusec/main/deployments/scripts/install.sh | bash -s latest
                        cd ${GIT_CLONE_DIR}/code-example/docker-image
                        /root/horusec start -p "./" --disable-docker="true"
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    // Manual approval step with 2-minute timeout
                    def userInput = null
                    try {
                        timeout(time: 2, unit: 'MINUTES') {
                            userInput = input(
                                id: 'Proceed', message: 'Do you want to proceed with the next stage?',
                                parameters: [
                                    booleanParam(defaultValue: true, description: '', name: 'Proceed')
                                ]
                            )
                        }
                    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
                        echo "Timeout reached. Automatically proceeding to the next stage."
                    }

                    // Check user input
                    if (userInput != null && userInput) {
                        echo "User chose to proceed."
                    } else if (userInput == null) {
                        echo "No user input received. Proceeding automatically due to timeout."
                    } else {
                        error "Pipeline aborted by user."
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
                        remote.name = 'Ubuntu-Train'
                        remote.host = SSH_SERVER
                        remote.user = SSH_USER
                        remote.password = SSH_PASS
                        remote.allowAnyHosts = true

                        // Build docker image
                        sshCommand remote: remote, command: """
                        cd ${GIT_CLONE_DIR}/code-example/docker-image
                        docker build -t text-classification-cnn-model .
                        docker tag text-classification-cnn-model text-classification-cnn-model:${currentBuild.number}
                        """
                    }
                }
            }
        }

        stage('Analysis Docker Image') {
            steps {
                script {
                    // Add a timeout for the user input prompt
                    def skipStage = false
                    try {
                        timeout(time: 2, unit: 'MINUTES') {
                            skipStage = input(
                                id: 'SkipAnalysis', message: 'Do you want to skip the Analysis Docker Image stage?',
                                parameters: [
                                    booleanParam(defaultValue: true, description: '', name: 'Skip')
                                ]
                            )
                        }
                    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
                        echo "Timeout reached for user input. Proceeding without skipping the stage."
                    }

                    // If user chooses to skip, print a message and mark the stage as successful
                    if (skipStage != null && skipStage) {
                        echo "Skipping the Analysis Docker Image stage."
                        return
                    } else if(skipStage == null) {
                        echo "No user input received. Proceeding automatically due to timeout."
                        return
                    } else {
                        // Retrieve the password from Jenkins secret text credentials
                        withCredentials([string(credentialsId: CREDENTIALS_ID, variable: 'SSH_PASS')]) {
                            def remote = [:]
                            remote.name = 'Ubuntu-Train'
                            remote.host = SSH_SERVER
                            remote.user = SSH_USER
                            remote.password = SSH_PASS
                            remote.allowAnyHosts = true

                            // Analysi docker image with trivy
                            sshCommand remote: remote, command: """
                            trivy image text-classification-cnn-model:${currentBuild.number} --scanners vuln --timeout 10m
                            """
                            // Analysi docker image with docker scout
                            sshCommand remote: remote, command: """
                            docker scout cves text-classification-cnn-model:${currentBuild.number}
                            """
                        }
                    }
                }
            }
        }

        stage('Upload Image to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: env.NEXUS_CREDENTIALS_ID, usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        // Retrieve the password from Jenkins secret text credentials
                        withCredentials([string(credentialsId: CREDENTIALS_ID, variable: 'SSH_PASS')]) {
                            def remote = [:]
                            remote.name = 'Ubuntu-Train'
                            remote.host = SSH_SERVER
                            remote.user = SSH_USER
                            remote.password = SSH_PASS
                            remote.allowAnyHosts = true

                            // Construct the upload URL
                            def uploadUrl_0 = "${NEXUS_URL}${NEXUS_REPO_PATH_DEVSECOPS}${env.BUILD_NUMBER}/${IMAGE_EXPORT_FILE}"

                            // Export image and Upload image to nexus
                            sshCommand remote: remote, command: "mkdir -p ${IMAGE_DIR}"
                            sshCommand remote: remote, command: """
                            echo "docker save -o ${IMAGE_DIR}${IMAGE_EXPORT_FILE} text-classification-cnn-model:${currentBuild.number}"
                            """
                            sshCommand remote: remote, command: """
                            echo "curl -v -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} --upload-file ${IMAGE_DIR}${IMAGE_EXPORT_FILE} ${uploadUrl_0}"
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy Image to Ubuntu-Deploy.srv') {
            steps {
                script {
                    // Retrieve the password from Jenkins secret text credentials
                    withCredentials([string(credentialsId: CREDENTIALS_ID, variable: 'SSH_PASS')]) {
                        def remote = [:]
                        remote.name = 'Ubuntu-Deploy'
                        remote.host = SSH_SERVER
                        remote.user = SSH_USER
                        remote.password = SSH_PASS
                        remote.allowAnyHosts = true

                        // Construct the download URL
                        def downloadUrl_0 = "${NEXUS_URL}${NEXUS_REPO_PATH_DEVSECOPS}${env.BUILD_NUMBER}/${IMAGE_EXPORT_FILE}"

                        // Load image and run image
                        sshCommand remote: remote, command: """
                        echo "wget -O ${IMAGE_DIR}${IMAGE_EXPORT_FILE} ${downloadUrl_0}"
                        """
                        // // Load image
                        sshCommand remote: remote, command: """
                        echo "docker load -i  ${IMAGE_DIR}${IMAGE_EXPORT_FILE}"
                        """
                        // Run image
                        sshCommand remote: remote, command: """
                        docker run -d --name text-classification-cnn-model -p 5000:443 --rm text-classification-cnn-model
                        """
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    // Adding a delay of 2 minutes
                    sleep time: 2, unit: 'MINUTES'
                    
                    def curlCommand = 'curl -s -o /dev/null -w "%{http_code}" -X POST -H "Content-Type: application/json" -d \'{"text": "Sample text for prediction"}\' ${APP_URL}/predict'
                    echo "Executing curl command: ${curlCommand}"

                    def responseCode = sh(script: curlCommand, returnStdout: true).trim()
                    echo "Response code received: ${responseCode}"
                    
                    if (responseCode.startsWith('200')) {
                        echo 'Health Check Successful'
                    } else {
                        error 'Health Check Failed'
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                echo "Completed all stages."
            }
        }
        failure {
            script {
                echo "Pipeline failed."
            }
        }
    }
}
