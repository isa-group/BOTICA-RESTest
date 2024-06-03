FROM openjdk:11

WORKDIR /app
COPY target/restest-bot.jar /app/restest-bot.jar

CMD ["java", "-jar", "/app/restest-bot.jar"]
