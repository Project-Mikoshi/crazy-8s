# Crazy 8s

## Authors

```md
Anthony Y. Zhu
101128194
```

## How to start server

```bash
mvn clean install
mvn spring-boot:run
```

## How to open the web app

```md
after server has been started, visit localhost:8080 to check out the the web page
```

## How to run all tests

```md
mvn test
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

