package com.halisson.iadoc_question.service;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractChatService {

	private final ChatClient chatClient;

	public AbstractChatService(ChatClient chatClient) {
		super();
		this.chatClient = chatClient;
	}
	
	public abstract String answerQuestion(String message);

	protected String prompt(PromptTemplate promptTemplate, Map<String, Object> promptParameters) {
		String content = chatClient.prompt(promptTemplate.create(promptParameters)).call().content();
		
		log.info(content);
		
		return content;
	}
}
