package com.gandalp.gandalp.openAi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final OpenAIProperties openAIProperties;

    private final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String askQuestion(String question) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(API_URL);

            // 헤더
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + openAIProperties.getApiKey());

            // JSON 생성
            ObjectMapper mapper = new ObjectMapper();

            // 메시지 생성
            ObjectNode message = mapper.createObjectNode();
            message.put("role", "user");
            message.put("content", question);

            // 전체 요청 생성
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("model", "gpt-3.5-turbo");
            ArrayNode messages = mapper.createArrayNode();
            messages.add(message);
            requestBody.set("messages", messages);

            // 요청 바디 설정
            StringEntity entity = new StringEntity(requestBody.toString(), "UTF-8");
            httpPost.setEntity(entity);

            // 응답
            CloseableHttpResponse response = client.execute(httpPost);
            JsonNode root = mapper.readTree(response.getEntity().getContent());

            return root.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }
}
