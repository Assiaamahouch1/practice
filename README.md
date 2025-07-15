# Fixify Authentication Service

A comprehensive Spring Boot authentication service with JWT tokens for the Fixify platform, supporting 4 types of users with different registration flows and role-based access control.

## Architecture Overview

The service implements a complete JWT-based authentication system with:

- **Spring Security 6** for authentication and authorization
- **JWT tokens** with access and refresh token support
- **Multi-user type support**: Client, Reparateur, Livreur, Admin, Super Admin
- **Email verification** system
- **Role-based access control**
- **PostgreSQL** database with H2 for testing

## User Types & Registration Logic

### 1. Client
- **Registration**: Optional (can browse without account)
- **Login Methods**: Email OR Phone number
- **Verification**: Email verification (if email provided)
- **Access**: Required for placing orders

### 2. Reparateur
- **Registration**: Required with admin validation
- **Status Flow**: NON_VERIFIE → VERIFIE → CERTIFIE
- **Email**: Required for registration
- **Profile**: Complete profile with speciality, rates, location

### 3. Livreur
- **Registration**: Required
- **Features**: Geolocation tracking, availability status
- **Dashboard**: Glovo-style delivery interface
- **Email**: Required for registration

### 4. Admin/Super Admin
- **Super Admin**: Pre-existing account
- **Admin**: Created by Super Admin
- **Permissions**: System management and user validation

## API Endpoints

### Authentication Endpoints

#### User Registration
```http
POST /api/auth/register/client
Content-Type: application/json

{
  "email": "client@example.com",  // Optional, can use phone instead
  "phone": "+1234567890",         // Optional, can use email instead
  "password": "password123",      // Required, min 6 chars
  "firstName": "John",            // Required
  "lastName": "Doe"               // Required
}
```

```http
POST /api/auth/register/reparateur
Content-Type: application/json

{
  "email": "reparateur@example.com",  // Required
  "phone": "+1234567890",             // Optional
  "password": "password123",          // Required
  "firstName": "John",                // Required
  "lastName": "Doe",                  // Required
  "speciality": "Electronics",        // Optional
  "experienceYears": 5,               // Optional
  "hourlyRate": 50.00,               // Optional
  "description": "Expert in electronics repair",  // Optional
  "address": "123 Main St",          // Optional
  "city": "Paris"                    // Optional
}
```

```http
POST /api/auth/register/livreur
Content-Type: application/json

{
  "email": "livreur@example.com",     // Required
  "phone": "+1234567890",             // Optional
  "password": "password123",          // Required
  "firstName": "John",                // Required
  "lastName": "Doe",                  // Required
  "vehicleType": "Motorcycle",        // Optional
  "licenseNumber": "LIC123456",       // Optional
  "zoneCoverage": "Central Paris"     // Optional
}
```

#### Authentication
```http
POST /api/auth/login
Content-Type: application/json

{
  "emailOrPhone": "user@example.com",  // Email or phone number
  "password": "password123"            // User password
}

Response:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJ...",
    "refreshToken": "refresh-token-uuid",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "role": "CLIENT",
      "status": "ACTIVE",
      "emailVerified": true
    }
  }
}
```

#### Token Management
```http
POST /api/auth/refresh-token
Content-Type: application/json

{
  "refreshToken": "refresh-token-uuid"
}
```

```http
POST /api/auth/logout
Authorization: Bearer <access-token>
```

#### Email Verification
```http
GET /api/auth/verify-email?token=<verification-token>
```

#### Current User Profile
```http
GET /api/auth/me
Authorization: Bearer <access-token>
```

## JWT Token Structure

### Access Token (15 minutes expiry)
```json
{
  "sub": "user@example.com",
  "role": "CLIENT",
  "userId": 1,
  "iat": 1640995200,
  "exp": 1640996100
}
```

### Refresh Token (7 days expiry)
- UUID-based token stored in database
- Used to generate new access tokens
- Automatically revoked on logout

## Database Schema

### User Entity Hierarchy
- **users** (base table)
  - id, email, phone, password, firstName, lastName
  - role, status, emailVerified, phoneVerified
  - verificationToken, verificationTokenExpiry
  - createdAt, updatedAt

- **clients** (extends users)
  - Minimal additional fields

- **reparateurs** (extends users)
  - reparateurStatus, photoUrl, cin, rib
  - speciality, experienceYears, hourlyRate
  - description, address, city, latitude, longitude

- **livreurs** (extends users)
  - vehicleType, licenseNumber
  - currentLatitude, currentLongitude, isAvailable
  - zoneCoverage

- **admins** (extends users)
  - permissions, department

- **refresh_tokens**
  - token, userId, expiryDate, revoked

## Security Configuration

### CORS Settings
- Configurable allowed origins
- Supports credentials
- All HTTP methods allowed for auth endpoints

### Protected Endpoints
- `/api/auth/me` - Requires authentication
- `/api/auth/logout` - Requires authentication
- `/api/admin/**` - Requires ADMIN or SUPER_ADMIN role

### Public Endpoints
- All registration endpoints
- Login endpoint
- Email verification
- Token refresh

## Configuration

### Environment Variables
```yaml
# Database
DB_USERNAME=fixify
DB_PASSWORD=password

# JWT
JWT_SECRET=your-secret-key

# Email
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=your-email
MAIL_PASSWORD=your-password

# CORS
CORS_ORIGINS=http://localhost:3000,http://localhost:4200
```

### Application Properties
```yaml
# Database connection
spring.datasource.url=jdbc:postgresql://localhost:5432/fixify_auth

# JWT configuration
fixify.jwt.access-token-expiration=900000    # 15 minutes
fixify.jwt.refresh-token-expiration=604800000 # 7 days

# Email settings
spring.mail.host=${MAIL_HOST:smtp.gmail.com}
spring.mail.port=587
```

## Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
- Unit tests for JWT utilities
- Integration tests for authentication endpoints
- Context loading tests
- Mock email service for testing

### Test Database
- H2 in-memory database for tests
- Automatic schema creation/destruction
- Isolated test transactions

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+ (for production)

### Running the Application
1. Clone the repository
2. Configure database connection in `application.yml`
3. Run: `mvn spring-boot:run`
4. Access: http://localhost:8080

### Running with Docker
```bash
# Build
mvn clean package
docker build -t fixify-auth-service .

# Run
docker run -p 8080:8080 fixify-auth-service
```

## API Documentation

For detailed API documentation, start the application and visit:
- Swagger UI: http://localhost:8080/swagger-ui.html (if enabled)
- H2 Console: http://localhost:8080/h2-console (for testing)

## Error Handling

The service includes comprehensive error handling:
- Validation errors (400 Bad Request)
- Authentication errors (401 Unauthorized)
- Conflict errors (409 Conflict) for duplicate users
- Internal server errors (500) with proper logging

## Logging

Configured logging levels:
- Application: DEBUG level for development
- Security: DEBUG level for authentication troubleshooting
- SQL: Enabled for database query monitoring

## Future Enhancements

- Rate limiting for authentication endpoints
- Password reset functionality
- OAuth2 integration
- Multi-factor authentication
- Audit logging
- Session management
- User profile management endpoints