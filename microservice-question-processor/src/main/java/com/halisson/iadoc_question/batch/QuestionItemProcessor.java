package com.halisson.iadoc_question.batch;

import java.time.LocalDateTime;

import org.springframework.batch.item.ItemProcessor;

import com.halisson.iadoc_question.entity.Question;
import com.halisson.iadoc_question.entity.QuestionStatus;
import com.halisson.iadoc_question.enums.EnumQuestionStatus;
import com.halisson.iadoc_question.service.ChatService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuestionItemProcessor implements ItemProcessor<Question, Question> {
	
	private final ChatService chatService;
	
	public QuestionItemProcessor(ChatService chatService) {
		super();
		this.chatService = chatService;
	}

	@Override
	public Question process(Question question) throws Exception {
		log.debug("Processing question {}", question.getQuestion());
		
		String response = this.chatService.answerQuestion(question.getQuestion());
		
		log.debug("Finished processing question {} \n {}", question.getQuestion(), response);
		
		question.setResponse(response);
		question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.SUCCESSFULLY_ANSWERED));
		question.setAnsweredAt(LocalDateTime.now());
		
		return question;
	}

}
