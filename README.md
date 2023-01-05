## How to start server

```bash
mvn clean
mvn mvn compile
mvn spring-boot:run
```

## How to open the web app

```md
after server has been started, visit localhost:8080 to check out the the web page
```

## How to run all tests

```bash
mvn test
```

## Log

```md
acceptance testing takes a long time and may occasionally fail due to uncertain race conditions, there's a log.txt file under the root directory to show test cases does pass in one shot 
```

## Project Structure

```md
├── README.md                             <-  doc
├── client                                <-  frontend code (react + vite)
├── pom.xml
└── src                                   <-  backend code (spring boot + netty socket io)
    └── test
        └── resources
            ├── features                  <-  acceptance test features   
            │   └── multiplayer.feature
```

## About Commit

```md
[code]        <- game util function code that are tested by matching unit tests
[u-test]      <- unit testings for util function related to game
[a-test]      <- acceptance tests related to game features
[refactor]    <- code that improve but does not change the existing behavior of game
[ui]          <- ui code that does not require unit tests
[network]     <- server or game module related networking handling code
[ui+network]  <- synchronous backend and frontend code changes
[doc]         <- documentation update
[init]        <- initial project dependencies, build steps, directories setup, continuous integration setup
```
