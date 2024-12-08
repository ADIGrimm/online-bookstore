
# Online bookstore API

This project is a RESTful API designed for an online bookstore. The API allows users to browse books, categorize them, and manage their library efficiently. It provides endpoints for book management, category association, and user interaction, making it a practical foundation for an e-commerce platform or a digital library.


## Tech Stack

**Frameworks and Libraries:** Spring Boot (Core Framework), MapStruct, Lombok, Liquibase  

**Database:** MySQL, H2

**API Documentation:** SpringDoc OpenAPI

**Security:** Spring Security, JSON Web Tockens 

**Testing:** Mockito, Spring Security Test

**Build and Dependency Management:** Maven


## Usage/Examples

You can see and try all the functions (and models) by activating the project and going to http://localhost:8080/swagger-ui.html (check the port are you working with) by these credentials:
```
    email: admin@email.com
    password: 1234
```


## Installation

**Requeried software:** Java JDK, Maven, MySQL, Docker 

**Installation instruction**

1) Clone the repository
```bash
  git clone https://github.com/ADIGrimm/online-bookstore.git
```

2) Navigate to the project directory
```bash
  cd online-bookstore
```

3) Install dependencies using Maven
```bash
  mvn clean install
```

4) Set up environment variables
```bash
  cp .env.template .env
  # Don't forget to edit .env file to match your configuration
```

5) Build JAR file
```bash
  mvn clean package
```

6) Run the application
```bash
  docker-compose up
```

    
## Running Tests

To run tests, run the following command

```bash
  mvn test
```


## Acknowledgements
 - Mate Academy mentors and students for helping create this API
 - Spring, SQL, Maven, PostMan, and Swagger for access to API development and testing tools
