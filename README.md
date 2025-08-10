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

- **CREATE**: `createUser(User user)` - Uploads JSON to S3
- **READ**: `getUser(String userId)` - Downloads and deserializes JSON
- **UPDATE**: `updateUser(User user)` - Overwrites existing JSON
- **DELETE**: `deleteUser(String userId)` - Removes JSON from S3
- **LIST**: `getAllUsers()` - Lists all user objects

## Project Structure

```
src/main/java/com/example/
├── User.java          # Data model
├── S3Service.java     # S3 CRUD operations
└── S3CrudApp.java     # Main application
```