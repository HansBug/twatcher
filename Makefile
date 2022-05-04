.PHONY: info build test run clean

MVN  ?= $(shell which mvn)
JAVA ?= $(shell which java)

NAME    ?= $(shell cat pom.xml | grep -oPm1 "(?<=<artifactId>)[^<]+")
VERSION ?= $(shell cat pom.xml | grep -oPm1 "(?<=<version>)[^<]+")

SRC_DIR    ?= src
TARGET_DIR ?= target

CLASSPATH ?= ${TARGET_DIR}/classes
JAR_FILE  ?= ${TARGET_DIR}/${NAME}-${VERSION}-jar-with-dependencies.jar

CMD ?=

# information of twatcher
info:
	@echo ${NAME}-${VERSION}
jar:
	@echo ${JAR_FILE}
version:
	@echo ${VERSION}

# build
${JAR_FILE}:
	$(MVN) package

build:
	$(MVN) package

# unittest
test:
	$(MVN) test

# run code
run: ${JAR_FILE}
	$(JAVA) -cp "${JAR_FILE}:${CLASSPATH}" com.hansbug.${NAME}.Main ${CMD}
trun: ${JAR_FILE}
	$(JAVA) -cp "${JAR_FILE}" TestMain

# clean targets
clean:
	$(MVN) clean
