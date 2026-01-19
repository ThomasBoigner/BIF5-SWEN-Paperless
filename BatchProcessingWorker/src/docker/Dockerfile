FROM amazoncorretto:25-jdk AS builder
LABEL maintainer="Dominik Banach, Nasr Mohamed, Thomas Boigner"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:25-jdk
LABEL maintainer="Dominik Banach, Nasr Mohamed, Thomas Boigner"
COPY --from=builder dependencies/ ./
# COPY --from=builder internal-dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
