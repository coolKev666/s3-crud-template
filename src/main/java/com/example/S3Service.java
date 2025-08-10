package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;

public class S3Service {
    private final S3Client s3Client;
    private final ObjectMapper objectMapper;
    private final String bucketName;

    public S3Service(String bucketName) {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_2)
                .build();
        this.objectMapper = new ObjectMapper();
        this.bucketName = bucketName;
    }

    // CREATE - Upload JSON object to S3
    public void createUser(User user) {
        try {
            String json = objectMapper.writeValueAsString(user);
            String key = "users/" + user.getId() + ".json";
            
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("application/json")
                    .build();
            
            s3Client.putObject(request, RequestBody.fromString(json));
            System.out.println("Created user: " + user.getId());
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
    }

    // READ - Get JSON object from S3
    public User getUser(String userId) {
        try {
            String key = "users/" + userId + ".json";
            
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            String json = s3Client.getObjectAsBytes(request).asUtf8String();
            return objectMapper.readValue(json, User.class);
        } catch (Exception e) {
            System.err.println("Error getting user: " + e.getMessage());
            return null;
        }
    }

    // UPDATE - Update existing JSON object in S3
    public void updateUser(User user) {
        createUser(user); // S3 overwrites by default
        System.out.println("Updated user: " + user.getId());
    }

    // DELETE - Remove JSON object from S3
    public void deleteUser(String userId) {
        try {
            String key = "users/" + userId + ".json";
            
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            s3Client.deleteObject(request);
            System.out.println("Deleted user: " + userId);
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
    }

    // LIST - Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix("users/")
                    .build();
            
            ListObjectsV2Response response = s3Client.listObjectsV2(request);
            
            for (S3Object object : response.contents()) {
                String userId = object.key().replace("users/", "").replace(".json", "");
                User user = getUser(userId);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Error listing users: " + e.getMessage());
        }
        return users;
    }

    public void close() {
        s3Client.close();
    }
}