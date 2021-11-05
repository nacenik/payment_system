FROM openjdk:11-jdk

COPY target/*.jar /payment.jar

CMD ["java", "-jar", "/payment.jar"]
