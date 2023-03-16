##

This is a POC for a new logger appender, where DEBUG messages are by default not logged, but in case of an error, the DEBUG messages are logged as well.

## How to test this POC

#### Clone the repository
```shell
git clone git@github.com:leobeal-cpi/logger.git
```

#### Make sure you have Java 19 installed
Tip: You can use [sdkman](https://sdkman.io/) to manage your Java versions.

```shell
java -version
sdk install java 19-amzn
```

#### Start the application and its dependencies

```shell
make start
```

#### Send a request to the application
No errors, so no debug messages are logged.
```shell
curl -X POST http://localhost:8080/
```

#### Send a request to the application with an error
Debug messages are logged.
```shell
curl -X POST http://localhost:8080/?error=true
```

## How it works
The FingersCrossedAppender checks for the log level and in case of DEBUG, sends it to a buffer instead of logging it, otherwise it logs it as usual.
Whenever a log has a level higher or equal to ERROR, the buffer is flushed and the messages are logged.

## Known issues
At the moment the debug messages are not logged in the corrrect order. Not sure if this is an issue since they will saved in Elasticsearch anyway.

## Next steps
This is only a initial POC and it seems to work well.
Next steps is to make sure the FingersCrossedAppender can extend the FileAppender in order to write out logs.