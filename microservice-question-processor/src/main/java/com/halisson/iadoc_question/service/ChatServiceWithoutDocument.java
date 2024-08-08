package com.halisson.iadoc_question.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatServiceWithoutDocument extends AbstractChatService{
	
	@Value("classpath:/prompts/template-reference-without-document.st")
	private Resource sbPromptTemplateWithoutDocument;

	public ChatServiceWithoutDocument(ChatClient.Builder chatClientBuilder) {
		super(chatClientBuilder.build());
	}

	public String answerQuestion(@NotBlank String message) {
		
		log.info(message);
		
		PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplateWithoutDocument);
		
		Map<String, Object> promptParameters = new HashMap<>();
		promptParameters.put("input", message);
		
		return prompt(promptTemplate, promptParameters);

	}

}
