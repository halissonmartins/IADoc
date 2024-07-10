package com.halisson.iadoc_question.batch;

import java.time.LocalDateTime;

import org.springframework.batch.item.ItemProcessor;

import com.halisson.iadoc_question.entity.Question;
import com.halisson.iadoc_question.entity.QuestionStatus;
import com.halisson.iadoc_question.enums.EnumQuestionStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuestionInitializeItemProcessor implements ItemProcessor<Question, Question> {
			
	public QuestionInitializeItemProcessor() {
		super();
	}

	@Override
	public Question process(Question question) throws Exception{
		
		log.debug("Initializing question {}", question.getQuestion());
		
		question.setQuestionStatus(new QuestionStatus(EnumQuestionStatus.PROCESSING_STARTED));
		question.setProcessingStartedAt(LocalDateTime.now());
        
        return question;
	}
}
