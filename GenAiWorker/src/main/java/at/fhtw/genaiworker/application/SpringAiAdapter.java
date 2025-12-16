package at.fhtw.genaiworker.application;

import at.fhtw.genaiworker.application.port.GenAiPort;
import at.fhtw.genaiworker.infrastructure.SummaryPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class SpringAiAdapter implements GenAiPort {

    private final ChatClient aiClient;
    private final SummaryPrompt summaryPrompt;

    public SpringAiAdapter(ChatClient aiClient, SummaryPrompt summaryPrompt) {
        this.aiClient = aiClient;
        this.summaryPrompt = summaryPrompt;
    }

    @Override
    public String summarize(String text) {
        Prompt promptText = summaryPrompt.createPrompt(text);

        return aiClient
                .prompt(promptText)
                .call()
                .content();
    }
}