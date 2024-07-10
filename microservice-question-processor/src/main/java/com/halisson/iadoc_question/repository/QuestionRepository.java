package com.halisson.iadoc_question.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.halisson.iadoc_question.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	Page<Question> findByQuestionStatusCodeAndDocumentIsNull(Integer questionStatusCode, Pageable pageable);
	
	Page<Question> findByQuestionStatusCodeAndDocumentDocumentStatusCode(
			Integer questionStatusCode, Integer documentStatusCode, Pageable pageable);
	
}
