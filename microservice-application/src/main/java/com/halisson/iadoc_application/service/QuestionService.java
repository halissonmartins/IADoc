package com.halisson.iadoc_application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.halisson.iadoc_application.dto.QuestionDto;
import com.halisson.iadoc_application.entity.Question;
import com.halisson.iadoc_application.excection.NotFoundException;
import com.halisson.iadoc_application.repository.QuestionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	private final DocumentService documentService;
	
	public List<QuestionDto> findAll() {
		return questionRepository.findAll().stream().map(QuestionDto::new).collect(Collectors.toList());
	}
	
	public QuestionDto findById(Long id) {
		return new QuestionDto(questionRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Question not found with the given ID.")));
	}
	
	public QuestionDto createQuestion(String question, Long documentId) {
		
		documentService.findById(documentId);
		
		var questionSaved = questionRepository.save(new Question(question, documentId));
		
		return new QuestionDto(questionSaved);
	}

}
