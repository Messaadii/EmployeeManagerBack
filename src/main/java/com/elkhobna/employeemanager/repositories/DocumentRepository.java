package com.elkhobna.employeemanager.repositories;

import com.elkhobna.employeemanager.entities.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {
}