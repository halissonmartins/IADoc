package com.halisson.iadoc_application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	public Page<QuestionDto> findAll(Integer pageNumber, Integer pageSize) {

		Sort sort = Sort.by("question").ascending();
		Pageable sortPageable = PageRequest.of(pageNumber, pageSize, sort);
		
		return questionRepository.findAll(sortPageable).map(QuestionDto::new);
	}
	
	public QuestionDto findById(Long id) {
		return new QuestionDto(questionRepository.findById(id).orElseThrow(
				() -> new NotFoundException("Question not found with the given ID.")));
	}
	
	public QuestionDto createQuestion(String question, Long documentId) {
		
		if(documentId != null && documentId > 0) {
			documentService.findById(documentId);
		}
		
		var questionSaved = questionRepository.save(new Question(question, documentId));
		
		return new QuestionDto(questionSaved);
	}

}
