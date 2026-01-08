package at.fhtw.genaiworker.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor

@Slf4j
@Service
public class SummaryApplicationService {
    private final ChatClient chatClient;
    private final static PromptTemplate PROMPT_TEMPLATE = new PromptTemplate("Summarize the following document in 150 words or less. {content}");

    public String createSummary(String fullText) {
        Prompt prompt = PROMPT_TEMPLATE.create(Map.of("content", fullText));

        String summary = chatClient
                .prompt(prompt)
                .call()
                .content();

        return summary != null ? summary : "";
    }
}
