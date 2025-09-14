package com.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.S3Service;
import com.example.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class CreateUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final S3Service s3Service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CreateUserHandler() {
        String bucketName = System.getenv("BUCKET_NAME");
        this.s3Service = new S3Service(bucketName);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            User user = objectMapper.readValue(request.getBody(), User.class);
            s3Service.createUser(user);
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(201)
                .withHeaders(headers)
                .withBody("{\"message\":\"User created: " + user.getId() + "\"}");
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}