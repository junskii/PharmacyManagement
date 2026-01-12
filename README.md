# Pharmacy Management System

## 📚 About This Project

This project is a **learning project** developed as part of a **Software Design Patterns** course. The application demonstrates the practical implementation of various design patterns in a real-world scenario - a pharmacy management system. The primary goal is to understand how design patterns can be applied to solve common software design problems and improve code maintainability, flexibility, and reusability.

## 🎯 Design Patterns Used

This application implements **6 design patterns** from the Gang of Four (GoF) patterns:

### 1. **Factory Pattern** (`designpatterns.factory`)
- **Purpose**: Create user objects based on their role (Admin, Pharmacist, Cashier)
- **Implementation**: 
  - `IUserFactory` interface
  - `AbstractUserFactory` abstract class
  - Concrete factories: `AdminUserFactory`, `CashierUserFactory`, `DefaultUserFactory`
  - `UserFactoryProvider` for factory selection
- **Location**: Used in `AuthController` during user registration
- **Benefits**: Encapsulates object creation logic and provides flexibility for adding new user types

### 2. **Observer Pattern** (`designpatterns.observer`)
- **Purpose**: Notify when medicine stock changes
- **Implementation**:
  - `Observable<T>` interface
  - `Observer<T>` interface
  - `MedicineService` implements `Observable<Medicine>`
  - `StockNotificationService` implements `Observer<Medicine>`
- **Location**: Used in `MedicineService` to notify `StockNotificationService` when stock is low
- **Benefits**: Decouples the subject (MedicineService) from observers, allowing multiple observers without tight coupling

### 3. **Command Pattern** (`designpatterns.command`)
- **Purpose**: Encapsulate sale processing as a command object
- **Implementation**:
  - `Command` interface with `execute()` method
  - `ProcessSaleCommand` concrete command
- **Location**: Used in `SalesController` to process sales transactions
- **Benefits**: Allows parameterization of clients, queuing of requests, and undo operations

### 4. **Decorator Pattern** (`designpatterns.decorator`)
- **Purpose**: Dynamically add discount functionality to sales
- **Implementation**:
  - `Discountable` interface
  - `BaseSaleDiscountable` concrete component
  - `DiscountDecorator` abstract decorator
  - Concrete decorators: `PromoDiscountDecorator`, `PaymentMethodDiscountDecorator`
- **Location**: Used in `SaleService` to calculate discounts
- **Benefits**: Allows adding new discount types without modifying existing code

### 5. **Strategy Pattern** (`designpatterns.strategy`)
- **Purpose**: Define different filtering algorithms for sales records
- **Implementation**:
  - `SaleFilterStrategy` interface
  - Concrete strategies: `CashierNameFilterStrategy`, `DateRangeFilterStrategy`, `HasDiscountFilterStrategy`, `MaxAmountFilterStrategy`, `MinAmountFilterStrategy`, `SearchKeywordFilterStrategy`
- **Location**: Used in `SalesRecordController` for filtering sales records
- **Benefits**: Encapsulates filtering algorithms and makes them interchangeable at runtime

### 6. **Template Method Pattern** (`designpatterns.template`)
- **Purpose**: Define the skeleton of sale processing algorithm
- **Implementation**:
  - `SaleProcessor` abstract class with template method `processSaleTemplate()`
  - `DefaultSaleProcessor` concrete implementation
- **Location**: Used in `SaleService` to process sales with a consistent algorithm structure
- **Benefits**: Defines algorithm structure while allowing subclasses to override specific steps

## 📋 About the Application

**Pharmacy Management System** is a web-based application designed to help pharmacies manage their daily operations efficiently. The system provides comprehensive features for managing medicines, processing sales transactions, managing users, and generating reports.

### Key Features

- **📦 Medicine Management**
  - CRUD operations for medicines
  - Image upload for medicine photos
  - Stock monitoring and tracking
  - Automatic stock notifications when stock is low

- **💰 Sales Transaction**
  - Point of Sale (POS) system
  - Support for multiple payment methods
  - Flexible discount system (promo discounts, payment method discounts)
  - Real-time stock deduction
  - Transaction history

- **👥 User Management**
  - Role-based access control (RBAC)
  - Three user roles: Admin, Pharmacist, Cashier
  - User registration and authentication
  - Different permissions for each role

- **📊 Dashboard**
  - Real-time statistics
  - Sales overview
  - Stock alerts
  - Quick access to main features

- **📈 Sales Records**
  - Comprehensive sales history
  - Advanced filtering (by date, cashier, amount, discount, keyword)
  - Sorting capabilities
  - Detailed sale information

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 3.4.6
- **Java Version**: 17
- **Database**: MySQL
- **ORM**: MyBatis 3.0.4
- **Frontend**: Thymeleaf, Bootstrap
- **Build Tool**: Maven
- **Other Libraries**: Lombok

## 📁 Project Structure

```
Pharmacy/
├── src/
│   ├── main/
│   │   ├── java/com/example/pharmacy/
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST/Web controllers
│   │   │   ├── designpatterns/      # Design pattern implementations
│   │   │   │   ├── command/
│   │   │   │   ├── decorator/
│   │   │   │   ├── factory/
│   │   │   │   ├── observer/
│   │   │   │   ├── strategy/
│   │   │   │   └── template/
│   │   │   ├── mapper/              # MyBatis mappers
│   │   │   ├── models/              # Entity/DTO classes
│   │   │   └── service/             # Business logic services
│   │   └── resources/
│   │       ├── mapper/              # MyBatis XML mappers
│   │       ├── static/              # Static resources (CSS, JS, images)
│   │       ├── templates/           # Thymeleaf templates
│   │       └── application.yml      # Application configuration
│   └── test/                        # Test files
└── pom.xml                          # Maven configuration
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Pharmacy
   ```

2. **Create MySQL database**
   ```sql
   CREATE DATABASE pharmacy;
   ```

3. **Configure database connection**
   - Create `src/main/resources/application.yml` (if not exists)
   - Update database credentials:
     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/pharmacy
         username: your_username
         password: your_password
     ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Open browser: `http://localhost:8081`
   - Login with your credentials (or register a new user)

## 📝 Database Schema

The application uses the following main tables:
- `users` - User accounts with roles
- `roles` - User roles (ADMIN, PHARMACIST, CASHIER)
- `medicines` - Medicine inventory
- `sales` - Sales transactions
- `sale_items` - Items in each sale
- `stock_notifications` - Stock alert notifications

## 👤 User Roles

- **ADMIN**: Full access to all features (user management, medicine management, sales, reports)
- **PHARMACIST**: Can manage medicines and view reports
- **CASHIER**: Can process sales transactions and view sales records

## 🔒 Security Notes

⚠️ **Important**: This is a learning project. The application uses plain text password storage for simplicity. In a production environment, you should:
- Use password hashing (BCrypt, Argon2)
- Implement proper authentication (JWT, OAuth2)
- Add input validation and sanitization
- Implement CSRF protection
- Use HTTPS

## 📚 Learning Outcomes

By studying this project, you will learn:
- How to implement 6 different design patterns in a real application
- How design patterns improve code organization and maintainability
- How to structure a Spring Boot application
- How to use MyBatis for database operations
- How to build a web application with Thymeleaf
- How to implement role-based access control

## 🤝 Contributing

This is a learning project, but suggestions and improvements are welcome!

## 📄 License

This project is created for educational purposes.

## 👨‍💻 Author

Created as part of Software Design Patterns course assignment.

---

**Note**: This application is designed for educational purposes to demonstrate design pattern implementations. For production use, additional security measures and optimizations are required.

