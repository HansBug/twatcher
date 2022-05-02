.PHONY: info build test run clean

MVN  ?= $(shell which mvn)
JAVA ?= $(shell which java)

NAME    ?= $(shell cat pom.xml | grep -oPm1 "(?<=<artifactId>)[^<]+")
VERSION ?= $(shell cat pom.xml | grep -oPm1 "(?<=<version>)[^<]+")

SRC_DIR    ?= src
TARGET_DIR ?= target

JAR_FILE ?= ${TARGET_DIR}/${NAME}-${VERSION}-jar-with-dependencies.jar

CMD ?=

info:
	@echo ${NAME}-${VERSION}

${JAR_FILE}:
	$(MVN) package

build:
	$(MVN) package

test:
	$(MVN) test

run: ${JAR_FILE}
	$(JAVA) -jar "${JAR_FILE}" ${CMD}

clean:
	$(MVN) clean
