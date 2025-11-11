FROM debian:bookworm AS tesseract-builder
RUN apt update
RUN apt install -y tesseract-ocr

FROM amazoncorretto:25-jdk AS builder
LABEL maintainer="Dominik Banach, Nasr Mohamed, Thomas Boigner"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:25-jdk
LABEL maintainer="Dominik Banach, Nasr Mohamed, Thomas Boigner"
COPY --from=tesseract-builder /usr/bin/tesseract /usr/bin/
# COPY --from=tesseract-builder /usr/share/tessdata /usr/share/tessdata
COPY --from=builder dependencies/ ./
# COPY --from=builder internal-dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "--enable-native-access=ALL-UNNAMED", "org.springframework.boot.loader.launch.JarLauncher"]
