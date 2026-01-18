package at.fhtw.genaiworker.infrastructure.genai.gemini;

import at.fhtw.genaiworker.application.GenAiService;
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
public class GeminiGenAiService implements GenAiService {
    private final ChatClient chatClient;

    private final static PromptTemplate PROMPT_TEMPLATE = new PromptTemplate("Summarize the following document in 150 words or less. {content}");

    @Override
    public String summarize(String fulltext) {
        log.trace("Trying to create summary from full text {}", fulltext);

        Prompt prompt = PROMPT_TEMPLATE.create(Map.of("content", fulltext));

        String summary = chatClient
                .prompt(prompt)
                .call()
                .content();

        return summary != null ? summary : "";
    }
}
