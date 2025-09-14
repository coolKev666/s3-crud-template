package com.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.example.S3Service;
import java.util.HashMap;
import java.util.Map;

public class DeleteUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final S3Service s3Service;

    public DeleteUserHandler() {
        String bucketName = System.getenv("BUCKET_NAME");
        this.s3Service = new S3Service(bucketName);
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String userId = request.getPathParameters().get("userId");
            s3Service.deleteUser(userId);
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withHeaders(headers)
                .withBody("{\"message\":\"User deleted: " + userId + "\"}");
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}