package com.halisson.iadoc_question.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotBlank;

@Service
public class ChatService {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

	private final ChatClient chatClient;
	private final VectorStore vectorStore;
	
	@Value("classpath:/prompts/template-reference.st")
	private Resource sbPromptTemplate;

	public ChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		super();
		this.chatClient = chatClientBuilder.build();
		this.vectorStore = vectorStore;
	}

	public String answerQuestion(@NotBlank String message) {
		
		logger.info(message);
		
		PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);
		
		Map<String, Object> promptParameters = new HashMap<>();
		promptParameters.put("input", message);
		promptParameters.put("documents", String.join("\n", findSimilarDocuments(message)));
		
		String content = chatClient.prompt(promptTemplate.create(promptParameters)).call().content();
		
		logger.info(content);
		
		return content;

	}

	private List<String> findSimilarDocuments(String message) {
		List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(3));
		return similarDocuments.stream().map(Document::getContent).toList();
	}
}
