# MLOps Awesome

# Concept

## Prepare

### Jenkins

`docker pull jenkins/jenkins`

`docker run -p 8080:8080 -p 50000:50000 -v /your/home/jenkins:/var/jenkins_home jenkins/jenkins`

Ref : [install-jenkins-using-docker](https://medium.com/@eloufirhatim/install-jenkins-using-docker-e76f41f79682)

### Nexus

`docker pull sonatype/nexus3`

`docker volume create --name nexus-data`

`docker run -p 8081:8081 --name nexus -v /your/home/nexus:/nexus-data sonatype/nexus3`

Ref: [install-sonatype-nexus-using-docker](https://ahgh.medium.com/how-to-setup-sonatype-nexus-3-repository-manager-using-docker-7ff89bc311ce)
