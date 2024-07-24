# MLSecOps - DevSecOps - Awesome

# Proposed Pipeline

### 💥 MLSecOps Pipeline

![image](https://github.com/user-attachments/assets/cf843f2e-2f75-40e6-afde-4328083f32f6)

### 💥 DevSecOps Pipeline

![image](https://github.com/user-attachments/assets/918de45e-abfe-47ae-8367-ad9718906144)

## Prepare for MLSecOps

### Setup Jenkins.srv

- `docker pull jenkins/jenkins`

- `docker run -p 8080:8080 -p 50000:50000 --name jenkins-srv -v /your/home/jenkins:/var/jenkins_home jenkins/jenkins`

Ref : [install-jenkins-using-docker](https://medium.com/@eloufirhatim/install-jenkins-using-docker-e76f41f79682)

### Setup Nexus.srv

- `docker pull sonatype/nexus3`

- `docker volume create --name nexus-data`

- `docker run -p 8081:8081 --name nexus-srv -v /your/home/nexus:/nexus-data sonatype/nexus3`

Ref: [install-sonatype-nexus-using-docker](https://ahgh.medium.com/how-to-setup-sonatype-nexus-3-repository-manager-using-docker-7ff89bc311ce)

### Setup Ubuntu-Train.srv

- `docker pull takeyamajp/ubuntu-sshd`

- `docker run --name ubuntu-sshd  -v /Users/lethanhphuc/UIT/Train:/mnt/train-data -v /var/run/docker.sock:/var/run/docker.sock -e TZ=Asia/Tokyo -e ROOT_PASSWORD=root -p 2222:22 takeyamajp/ubuntu-sshd`

Ref: [ubuntu-sshd](https://hub.docker.com/r/takeyamajp/ubuntu-sshd)

### Setup SonarQube.srv

- `docker pull library/sonarqube:latest`

- `docker run --name sonarqube -d -p 9000:9000 sonarqube`

### Docker Network Check

- `docker network inspect bridge`

### Install Dependences

#### On Jenkins.srv

Install ssh-steps plugins

`Dashboard > Manage Jenkins > Plugins > Available plugins > SSH Pipeline Steps`

`Dashboard > Manage Jenkins > Credentials > System > Global credentials (unrestricted) > Add Credentials > Kind (Secret Text) for password of Ubuntu-Train.srv`

`Dashboard > Manage Jenkins > Credentials > System > Global credentials (unrestricted) > Add Credentials > Kind (Secret Text) for password of Nexus.srv`

Install SonarQube

Ref: [SonarQube-Jenkins](https://www.geeksforgeeks.org/how-to-integrate-sonarqube-with-jenkins/)

#### On Ubuntu-Train.srv

Install Curl & PyPip & Git & JQ & Trivy & Docker Scout & aria2

- `ssh root@127.0.0.1 -p 2222`

- `apt update && apt install python3-pip git curl jq aria2 -y`

- `apt-get install wget apt-transport-https gnupg lsb-release -y`

- `wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | apt-key add -`

- `echo deb https://aquasecurity.github.io/trivy-repo/deb $(lsb_release -sc) main | tee -a /etc/apt/sources.list.d/trivy.list`

- `apt-get update && apt-get install trivy -y`

- `curl -fsSL https://raw.githubusercontent.com/docker/scout-cli/main/install.sh -o install-scout.sh`

- `sh install-scout.sh`

Verify After Install

- `python3 --version`

- `pip3 --version`
  
- `git --version`

- `trivy --version`

- `docker scount --help`

Install Docker Engine

- `for pkg in docker.io docker-doc docker-compose docker-compose-v2 podman-docker containerd runc; do apt-get remove $pkg; done`

- `apt-get update && apt-get install ca-certificates -y && install -m 0755 -d /etc/apt/keyrings && curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc && chmod a+r /etc/apt/keyrings/docker.asc`

- `
    echo \
    "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
    $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
        tee /etc/apt/sources.list.d/docker.list > /dev/null
`

- `apt-get update && apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y`
