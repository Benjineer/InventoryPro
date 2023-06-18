FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar InventoryPro.jar
ENTRYPOINT ["java","-jar","/InventoryPro.jar"]