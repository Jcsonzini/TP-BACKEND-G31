FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src ./src
RUN ./mvnw -q -DskipTests package

EXPOSE 8083

CMD ["java", "-jar", "target/ms-logistica-0.0.1-SNAPSHOT.jar"]
