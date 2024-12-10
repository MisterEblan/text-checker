FROM maven:latest AS stage1
WORKDIR /article_checker
COPY pom.xml /article_checker
RUN mvn dependency:resolve
COPY . /article_checker
RUN mvn clean
RUN mvn package -DskipTests

FROM openjdk:21 AS final
COPY --from=stage1 /article_checker/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
