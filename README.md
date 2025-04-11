# Car Archive System

SpringBoot application with PostgreSQL backend.

**Live Demo**: [car-demo-latest.onrender.com](https://car-demo-latest.onrender.com)

## Features
- Full CRUD operations for car entities
- PostgreSQL database storage
- External API integration (currency exchange)
- Search, sorting, and pagination implemented
- UI with bootstrap

**Production**:
- CI/CD implemented (automatically build to docker image and deploy to render app)

# Local setup
1. **Requirements**:
    - Java 21
    - PostgreSQL 16
    - Maven

2. **Database**:
   - Create db: car-demo

## Configure database
src/main/resources/application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/car_demo
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

## Run application
```bash
mvn spring-boot:run
```


 Access at: http://localhost:8080

# Car Entity - Database Columns

| Column Name      | Data Type     | Constraints                 |
|------------------|---------------|-----------------------------|
| id               | Long          | Primary Key, Auto-increment |
| make             | String(50)    | Not null                    |
| model            | String(50)    | Not null                    |
| year             | Integer       | 1886-2100 range             |
| color            | String(30)    | Optional                    |
| price            | BigDecimal    | Precision(12,2), > 0        |
| image_url        | String(512)   | Optional                    |
| mileage          | Integer       | ≥ 0                         |
| fuel_type        | String(30)    | Optional                    |
| accident_free    | Boolean       | Default false               |
| publish_date     | LocalDate     | Past/Present dates only     |



# Car Controller Endpoints

## Base URL: `/cars`

| HTTP Method | Endpoint             | Parameters                                                                 | Description                                                                 |
|-------------|----------------------|----------------------------------------------------------------------------|-----------------------------------------------------------------------------|
| `GET`       | `/`                  | `make` (optional), `sort` (optional), `page` (default=0), `size` (default=10) | List all cars with pagination, filtering by make, and sorting options       |
| `GET`       | `/add`               | -                                                                          | Show form to add a new car                                                  |
| `POST`      | `/add`               | `Car` object, `accidentFree` (default=false)                               | Create a new car                                                            |
| `GET`       | `/edit/{id}`         | `id` (path variable)                                                       | Show form to edit an existing car                                           |
| `POST`      | `/update`            | `Car` object, `accidentFree` (default=false)                               | Update an existing car                                                      |
| `GET`       | `/delete/{id}`       | `id` (path variable)                                                       | Delete a car                                                                |
| `GET`       | `/details/{id}`      | `id` (path variable)                                                       | Show car details                                                            |
| `GET`       | `/search`            | `make` (optional), `sort` (optional), `page` (default=0), `size` (default=10) | Search cars with pagination, filtering by make, and sorting options        |
