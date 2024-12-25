package com.halisson.iadoc_question.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatService extends AbstractChatService{
	
	private final VectorStore vectorStore;
	
	@Value("classpath:/prompts/template-reference.st")
	private Resource sbPromptTemplate;

	public ChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		super(chatClientBuilder.build());
		this.vectorStore = vectorStore;
	}

	public String answerQuestion(@NotBlank String message) {
		
		log.info(message);
		
		PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);
		
		Map<String, Object> promptParameters = new HashMap<>();
		promptParameters.put("input", message);
		
		String documents = String.join("\n", findSimilarDocuments(message));
		log.info("Documentos:\n {}", documents);
		promptParameters.put("documents", documents);
		
		return prompt(promptTemplate, promptParameters);

	}

	private List<String> findSimilarDocuments(String message) {
		List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(3));
		return similarDocuments.stream().map(Document::getContent).toList();
	}
}
