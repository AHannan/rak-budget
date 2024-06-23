## Budget Service

The Budget Service allows users to manage their budgets by creating categories and setting budgets for different expense categories.

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL database

### Setup

1. **Clone the Repository**

   ```sh
   git clone https://github.com/AHannan/rak-budget
   cd rak-budget
   ```

2. **Configure the Database**

   Update the `application.yml` file with your PostgreSQL database credentials.

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/your_database
       username: your_username
       password: your_password
       schema: public

     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
       database-platform: org.hibernate.dialect.PostgreSQLDialect

   server:
     port: 8080
   ```

3. **Build the Project**

   ```sh
   mvn clean install
   ```

4. **Run the Application**

   ```sh
   mvn spring-boot:run
   ```

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

### Database Schema

Here is the PostgreSQL schema for the `budgets` and `budget_categories` tables.

```sql
CREATE TABLE budget_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL
);

CREATE TABLE budgets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    category_id UUID REFERENCES budget_categories(id),
    creation_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modification_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Running Tests

To run the tests for the Budget service, execute the following command:

```sh
mvn test
```

### Additional Information

For more details, refer to the API documentation available at `http://localhost:8080/swagger-ui.html` after starting the application.

### Contributing

If you wish to contribute to this project, please fork the repository and create a pull request with your changes.

### License

This project is licensed under the MIT License.