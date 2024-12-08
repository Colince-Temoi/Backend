#Start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

# MAINTAINER instruction is deprecated in favor of using label
# MAINTAINER eazybytes.com
#Information around who maintains the image. I.e so and so person or domain maintains the image.
LABEL "org.opencontainers.image.authors"="https://github.com/Colince-Temoi"

# Add the application's jar(target/accounts-0.0.1-SNAPSHOT.jar) to the image as "accounts-0.0.1-SNAPSHOT.jar"
# If you see, the Dockerfile is present inside accounts folder. Inside the accounts folder, we have target folder where the jar is present. That's why I'm trying to say please take the jar from target folder and copy the same jar into my image with the name "accounts-0.0.1-SNAPSHOT.jar "
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

# execute the application - If you are keen enough, you will notice that this is the same command we used to start the application in our local machine(Java command way). We are just telling Docker to start the application in the same way whenever someone tries to generate a container out of this image.
ENTRYPOINT ["java", "-jar", "accounts-0.0.1-SNAPSHOT.jar"]