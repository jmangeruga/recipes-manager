FROM amazoncorretto:11.0.12-alpine3.14

# There alternatives to include the whole jar that benefit docker layer cache. However, this seemed enough for this case.
COPY ./target/recipes-manager-*.jar recipes-manager.jar

EXPOSE 8443
ENTRYPOINT ["java","-jar","recipes-manager.jar"]