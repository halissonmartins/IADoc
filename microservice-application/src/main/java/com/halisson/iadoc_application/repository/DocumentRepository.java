package com.halisson.iadoc_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.halisson.iadoc_application.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
