package com.halisson.iadoc_application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.halisson.iadoc_application.dto.QuestionDto;
import com.halisson.iadoc_application.entity.Question;
import com.halisson.iadoc_application.repository.QuestionRepository;

@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository;

	public QuestionService(QuestionRepository questionRepository) {
		super();
		this.questionRepository = questionRepository;
	}
	
	public List<QuestionDto> findAll() {
		return questionRepository.findAll().stream().map(QuestionDto::new).collect(Collectors.toList());
	}
	
	public QuestionDto findById(Long id) {
		return new QuestionDto(questionRepository.findById(id).get());
	}
	
	public QuestionDto createQuestion(String question, Long documentId) {
		
		Question questionSaved = questionRepository.save(new Question(question, documentId));
		
		return new QuestionDto(questionSaved);
	}

}
