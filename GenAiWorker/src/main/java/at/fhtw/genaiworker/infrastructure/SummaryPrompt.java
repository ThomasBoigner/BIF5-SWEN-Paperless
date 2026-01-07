package at.fhtw.genaiworker.infrastructure;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public record SummaryPrompt(PromptTemplate template) {

    public SummaryPrompt() {
        this(new PromptTemplate("""
            Summarize the following document in 150 words or less.
            Respond ONLY with JSON: { "summary": "<text>" }

            {content}
            """));
    }

    public Prompt createPrompt(String content) {
        return template.create(Map.of("content", content));
    }
}
