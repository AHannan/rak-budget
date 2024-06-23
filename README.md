Here's the updated README for the Budget service, now using H2 database:

## Budget Service

The Budget Service allows users to manage their budgets by creating categories and setting budgets for different expense categories.

### Prerequisites

- Java 21 or higher
- Gradle

### Setup

1. **Clone the Repository**

   ```sh
   git clone https://github.com/AHannan/rak-budget
   cd rak-budget
   ```

2. **Configure the Database**

   The application is configured to use H2 in-memory database for development. The database configuration is specified in the `application.properties` file. You do not need to make any changes to the database configuration for development.

3. **Build the Project**

   ```sh
   ./gradlew build
   ```

4. **Run the Application**

   ```sh
   ./gradlew bootRun
   ```

### Database Configuration

The application uses H2 in-memory database for development. Below is the relevant section of the `application.properties` file:

```properties
spring.profiles.active=dev
spring.mvc.pathmatch.matching-strategy=ant_path_matcher

server.port=8080
server.servlet.contextPath=/api

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:budget
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.url=jdbc:h2:mem:budget
spring.flyway.user=sa
spring.flyway.password=password
spring.flyway.schemas=public

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/api-docs
```

### Accessing the H2 Console

The H2 console is available at `http://localhost:8080/api/h2-console`. Use the following settings to log in:

- **JDBC URL**: `jdbc:h2:mem:budget`
- **User Name**: `sa`
- **Password**: `password`

### API Endpoints

#### Create Category

Creates a new budget category.

- **URL**: `/api/budget-categories`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Request Body**:

  ```json
  {
      "name": "Utilities"
  }
  ```

- **Curl Command**:

  ```sh
  curl --location --request POST 'http://localhost:8080/api/budget-categories' \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "name": "Utilities"
  }'
  ```

#### Create Budget

Creates a new budget for a specific user.

- **URL**: `/api/budgets`
- **Method**: `POST`
- **Content-Type**: `application/json`
- **Request Body**:

  ```json
  {
      "userId": "abcb72dc-1334-43c5-94ec-ecacaf3309cc",
      "amount": "2000",
      "categoryId": "78030f1a-0231-4211-86d0-efd6696f5377"
  }
  ```

- **Curl Command**:

  ```sh
  curl --location --request POST 'http://localhost:8080/api/budgets' \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "userId": "abcb72dc-1334-43c5-94ec-ecacaf3309cc",
      "amount": "2000",
      "categoryId": "78030f1a-0231-4211-86d0-efd6696f5377"
  }'
  ```

### Running Tests

To run the tests for the Budget service, execute the following command:

```sh
./gradlew test
```

### Additional Information

For more details, refer to the API documentation available at `http://localhost:8080/api/swagger-ui.html` after starting the application.

### Contributing

If you wish to contribute to this project, please fork the repository and create a pull request with your changes.
