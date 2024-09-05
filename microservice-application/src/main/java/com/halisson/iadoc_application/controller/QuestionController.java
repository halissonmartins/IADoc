package com.halisson.iadoc_application.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.halisson.iadoc_application.controller.interfaces.IQuestionController;
import com.halisson.iadoc_application.dto.CreateQuestionDto;
import com.halisson.iadoc_application.dto.QuestionDto;
import com.halisson.iadoc_application.service.QuestionService;

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
	public ResponseEntity<Page<QuestionDto>> findAll(
			@RequestParam(defaultValue = "0") Integer pageNumber,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		
		Page<QuestionDto> response = questionService.findAll(pageNumber, pageSize);
		
		return ResponseEntity.ok(
				response.map(
						r -> r.add( 
								linkTo(methodOn(this.getClass()).findById(r.getId())).withSelfRel() )));
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<QuestionDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(
				questionService.findById(id).add( 
						linkTo(methodOn(this.getClass()).findAll(null, null)).withRel("questions") ));
	}
	
	@Override
	@PostMapping
	public ResponseEntity<QuestionDto> create(@RequestBody CreateQuestionDto dto) {		
		QuestionDto question = questionService.createQuestion(dto.getQuestion(), dto.getDocumentId());
		return new ResponseEntity<QuestionDto>(question, HttpStatus.CREATED);
	}
	
}
