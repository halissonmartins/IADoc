package com.halisson.iadoc_application.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.halisson.iadoc_application.controller.interfaces.IQuestionController;
import com.halisson.iadoc_application.dto.CreateQuestionDto;
import com.halisson.iadoc_application.dto.QuestionDto;
import com.halisson.iadoc_application.service.QuestionService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("questions")
public class QuestionController implements IQuestionController {
	
	private QuestionService questionService;
	
	public QuestionController(QuestionService questionService) {
		super();
		this.questionService = questionService;
	}

	@Override
	@GetMapping
	public ResponseEntity<List<QuestionDto>> findAll() {
		return ResponseEntity.ok(questionService.findAll());
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<QuestionDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(questionService.findById(id));
	}
	
	@Override
	@PostMapping
	public ResponseEntity<QuestionDto> create(@RequestBody CreateQuestionDto dto) {		
		QuestionDto question = questionService.createQuestion(dto.getQuestion(), dto.getDocumentId());
		return new ResponseEntity<QuestionDto>(question, HttpStatus.CREATED);
	}
	
}
