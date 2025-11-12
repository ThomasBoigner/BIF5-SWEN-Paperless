FROM amazoncorretto:25-jdk AS builder
LABEL maintainer="Dominik Banach, Nasr Mohamed, Thomas Boigner"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:25-jdk
LABEL maintainer="Dominik Banach, Nasr Mohamed, Thomas Boigner"

RUN apt update
RUN apt install -y tesseract-ocr
COPY src/main/resources/tessdata ./OcrWorker/src/main/resources/tessdata/

COPY --from=builder dependencies/ ./
# COPY --from=builder internal-dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
