$(VERBOSE).SILENT:
.DEFAULT_GOAL := help

COMPOSE_RUN_JAVA = docker compose run --no-deps --rm java
COMPOSE_RUN_JAVA_RUN = docker compose run --no-deps --rm java_run
COMPOSE_RUN_GRADLE = docker compose run --no-deps --entrypoint "gradle" --rm java
COMPOSE_RUN_JAVA_MAKE = docker compose run --no-deps --entrypoint "make" --rm java
COMPOSE_RUN_SH = docker compose run --no-deps --entrypoint "sh" --rm java

####################################################################################
##@ Building
####################################################################################

.PHONY: version
version: ## Java version with docker run pattern
	$(COMPOSE_RUN_GRADLE) --version

.PHONY: clean
clean: ## Gradle Clean
	$(COMPOSE_RUN_GRADLE) --no-daemon clean

.PHONY: test
test: ## Gradle Test (Fast)
	$(COMPOSE_RUN_GRADLE) --no-daemon unitTest

.PHONY: test_integration_min
test_integration_min: ## Gradle Test (Fast-Mid)
	$(COMPOSE_RUN_GRADLE) --no-daemon integration-min

.PHONY: test_integration
test_integration: ## Gradle Test (Mid-Slow)
	$(COMPOSE_RUN_GRADLE) --no-daemon integration

.PHONY: install
install: ## Gradle build
	$(COMPOSE_RUN_GRADLE) --no-daemon build -x test

####################################################################################
##@ Running
####################################################################################

.PHONY: run_docker
run_docker: PORT = 9090
run_docker: JAR_FILE = ./target/file-service-poc-0.0.1-SNAPSHOT.jar
run_docker: ## Run
	@docker run -it -v .:/opt/app -p $(PORT):8080 --entrypoint "java" file-service-poc:latest -jar $(JAR_FILE)

.PHONY: run
run: PORT = 9090
run: JAR_FILE = ./target/file-service-poc-0.0.1-SNAPSHOT.jar
run: ## Run
	@docker compose run -p $(PORT):8080 java -jar $(JAR_FILE)

.PHONY: run_compose
run_compose: ## Run app and mysql with docker compose
	@docker compose -f docker-compose-run.yml up --build

.PHONY: run_infrastructure_only
run_infrastructure_only: ## Run only mysql with docker compose
	@docker compose -f docker-compose-run.yml up --build database

####################################################################################
##@ Utils
####################################################################################
.PHONY: help
help: ## Display this help
	@awk \
	  'BEGIN { \
	    FS = ":.*##"; printf "\nUsage:\n"\
			"  make \033[36m<target>\033[0m\n" \
	  } /^[a-zA-Z_-]+:.*?##/ { \
	    printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 \
	  } /^##@/ { \
	    printf "\n\033[1m%s\033[0m\n", substr($$0, 5) \
	  } ' $(MAKEFILE_LIST)
