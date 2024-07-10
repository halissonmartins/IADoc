package com.halisson.iadoc_application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halisson.iadoc_application.dto.CreateQuestionDto;
import com.halisson.iadoc_application.dto.QuestionDto;
import com.halisson.iadoc_application.service.QuestionService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("questions")
public class QuestionController {
	
	private QuestionService questionService;
	
	public QuestionController(QuestionService questionService) {
		super();
		this.questionService = questionService;
	}

	@GetMapping
	public List<QuestionDto> findAll() {
		return questionService.findAll();
	}
	
	@GetMapping("/{id}")
	public QuestionDto findById(@PathVariable Long id) {
		return questionService.findById(id);
	}
	
	@PostMapping
	public QuestionDto create(@RequestBody CreateQuestionDto dto) {		
		return questionService.createQuestion(dto.getQuestion(), dto.getDocumentId());
	}
	
}
