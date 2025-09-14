# S3 CRUD Operations with Java

This project demonstrates CRUD operations with Amazon S3 using Java and Gradle.

## Setup

1. **AWS Credentials**: Configure AWS credentials using one of these methods:
   - AWS CLI: `aws configure`
   - Environment variables: `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`
   - IAM roles (if running on EC2)

2. **S3 Bucket**: Create an S3 bucket and update the bucket name in `S3CrudApp.java`

3. **Required Permissions**: Your AWS user/role needs these S3 permissions:
   - `s3:PutObject`
   - `s3:GetObject`
   - `s3:DeleteObject`
   - `s3:ListBucket`

## Running the Application

```bash
# Build the project
./gradlew build

# Run the application
./gradlew run
```

## CRUD Operations

### Standard Application
- **CREATE**: `createUser(User user)` - Uploads JSON to S3
- **READ**: `getUser(String userId)` - Downloads and deserializes JSON
- **UPDATE**: `updateUser(User user)` - Overwrites existing JSON
- **DELETE**: `deleteUser(String userId)` - Removes JSON from S3
- **LIST**: `getAllUsers()` - Lists all user objects

### Lambda Functions
- **POST /users** - Create user via API Gateway
- **GET /users/{userId}** - Get user via API Gateway
- **DELETE /users/{userId}** - Delete user via API Gateway

## Project Structure

```
src/main/java/com/example/
├── User.java          # Data model
├── S3Service.java     # S3 CRUD operations
├── S3CrudApp.java     # Main application
└── lambda/            # Lambda handlers
    ├── CreateUserHandler.java
    ├── GetUserHandler.java
    └── DeleteUserHandler.java
```

## Local Lambda Testing with SAM

### Prerequisites
- Install [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html)
- Configure AWS credentials

### Build and Test

1. **Build fat JAR with dependencies:**
   ```bash
   gradle fatJar
   ```

2. **Test individual Lambda functions:**
   ```bash
   # Test create user
   sam local invoke CreateUserFunction -e events/create-user.json
   
   # Test get user
   sam local invoke GetUserFunction -e events/get-user.json
   
   # Test delete user
   sam local invoke DeleteUserFunction -e events/delete-user.json
   ```

3. **Start local API Gateway:**
   ```bash
   sam local start-api --port 3000
   ```

4. **Test HTTP endpoints:**
   ```bash
   # Create user
   curl -X POST http://127.0.0.1:3000/users \
     -H "Content-Type: application/json" \
     -d '{"id":"456","name":"Jane Doe","email":"jane@example.com"}'
   
   # Get user
   curl http://127.0.0.1:3000/users/456
   
   # Delete user
   curl -X DELETE http://127.0.0.1:3000/users/456
   ```