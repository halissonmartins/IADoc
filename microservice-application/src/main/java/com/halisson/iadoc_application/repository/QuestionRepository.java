package com.halisson.iadoc_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.halisson.iadoc_application.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	
}
