FROM maven:3.6.0-jdk-11-slim AS build
COPY --chown=maven:maven . .
CMD ["mvn", "clean", "test", "-DfileXml=tests.xml", "-DselectedTests=dummy"]
