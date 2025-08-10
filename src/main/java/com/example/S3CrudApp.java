package com.example;

import java.util.List;

public class S3CrudApp {
    public static void main(String[] args) {
        // Replace with your S3 bucket name
        String bucketName = "kev-garbage-bin";
        S3Service s3Service = new S3Service(bucketName);

        try {
            // CREATE - Add new users
            User user1 = new User("1", "John Doe", "john@example.com");
            User user2 = new User("2", "Jane Smith", "jane@example.com");

            s3Service.createUser(user1);
            s3Service.createUser(user2);

            // READ - Get a specific user
            User retrievedUser = s3Service.getUser("1");
            System.out.println("Retrieved: " + retrievedUser);

            // UPDATE - Modify user
            user1.setEmail("john.doe@example.com");
            s3Service.updateUser(user1);

            // LIST - Get all users
            List<User> allUsers = s3Service.getAllUsers();
            System.out.println("All users:");
            allUsers.forEach(System.out::println);

            // DELETE - Remove a user
            s3Service.deleteUser("2");

            // Verify deletion
            System.out.println("Users after deletion:");
            s3Service.getAllUsers().forEach(System.out::println);

        } finally {
            s3Service.close();
        }
    }
}