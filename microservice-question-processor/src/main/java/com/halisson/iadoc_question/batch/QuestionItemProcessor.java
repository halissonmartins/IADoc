package com.halisson.iadoc_question.batch;

import java.time.LocalDateTime;

import org.springframework.batch.item.ItemProcessor;

import com.halisson.iadoc_library.enums.EnumQuestionStatus;
import com.halisson.iadoc_question.entity.Question;
import com.halisson.iadoc_question.entity.QuestionStatus;
import com.halisson.iadoc_question.service.AbstractChatService;
import com.halisson.iadoc_question.service.ChatService;
import com.halisson.iadoc_question.service.ChatServiceWithoutDocument;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuestionItemProcessor implements ItemProcessor<Question, Question> {
	
	private final ChatService chatService;
	private final ChatServiceWithoutDocument chatServiceWithoutDocument;
	
	public QuestionItemProcessor(ChatService chatService, ChatServiceWithoutDocument chatServiceWithoutDocument) {
		super();
		this.chatService = chatService;
		this.chatServiceWithoutDocument = chatServiceWithoutDocument;
	}

	@Override
	public Question process(Question question) throws Exception {
		log.debug("Processing question {}", question.getQuestion());
		
		AbstractChatService chat = getChatService(question);
		
		String response = chat.answerQuestion(question.getQuestion());
		
		log.debug("Finished processing question {} \n {}", question.getQuestion(), response);
		
		question.setResponse(response);
		question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.SUCCESSFULLY_ANSWERED));
		question.setAnsweredAt(LocalDateTime.now());
		
		return question;
	}
	
	private AbstractChatService getChatService(Question question) {
		if(question.getDocument() != null) {
			return this.chatService;
		}else {
			return this.chatServiceWithoutDocument;
		}
	}

}
