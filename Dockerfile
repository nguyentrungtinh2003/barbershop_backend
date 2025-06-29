FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
RUN useradd -ms /bin/bash appuser

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
# Gán quyền sở hữu file cho user appuser
RUN chown appuser:appuser app.jar

EXPOSE 8081
# Chạy với quyền user không phải root
USER appuser

ENTRYPOINT ["java","-jar","app.jar"]