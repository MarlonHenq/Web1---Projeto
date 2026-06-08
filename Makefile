.PHONY: run build test clean

run:
	mvn spring-boot:run

build:
	mvn clean package -DskipTests

test:
	mvn test

clean:
	mvn clean
