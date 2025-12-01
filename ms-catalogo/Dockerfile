FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiamos Maven wrapper y pom primero (mejora cache)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw -q -DskipTests dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Construimos el jar
RUN ./mvnw -q -DskipTests package

EXPOSE 8081

CMD ["java", "-jar", "target/ms-catalogo-0.0.1-SNAPSHOT.jar"]
