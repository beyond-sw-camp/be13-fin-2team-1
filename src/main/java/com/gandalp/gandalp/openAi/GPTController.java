package com.gandalp.gandalp.openAi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
public class GPTController {

    private final OpenAIService openAIService;

    @GetMapping("/test")
    public String testGpt() {
        return openAIService.askQuestion("대한민국의 수도는 어디인가?");
    }
}
