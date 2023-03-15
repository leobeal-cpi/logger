.PHONY: help


RED=\033[0;31m
WHITE=\033[0;37m
GREEN=\033[0;32m
YELLOW=\033[0;33m
BOLD=\033[1m
UNBOLD=\033[21m

PROFILE=dev## To change the profile, use make PROFILE=prod <target>
APP_HOST=localhost
APP_PORT=8080

help: ## Ask for help
	@printf "\033[33mUsage:\033[0m\n  make [target] [arg=\"val\"...]\n\n\033[33mTargets:\033[0m\n"
	@grep -E '^[-a-zA-Z0-9_\.\/]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[32m%-22s\033[0m %s\n", $$1, $$2}'

start: ## Start the docker containers dependencies and the application
	@printf "${GREEN}Starting the spring application with the ${BOLD}${PROFILE} ${GREEN}profile${WHITE}\n"
	@lsof -i :$(APP_PORT) > /dev/null && echo "$(RED)Port ${BOLD}$(APP_PORT) $(RED)is already in use$(WHITE)"  || ./mvnw spring-boot:run -Dspring-boot.run.profiles=$(PROFILE)
