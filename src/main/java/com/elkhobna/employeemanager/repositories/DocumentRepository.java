package com.elkhobna.employeemanager.repositories;

import com.elkhobna.employeemanager.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {

    @Query("""
        SELECT c FROM com.elkhobna.employeemanager.entities.Document c
        LEFT JOIN c.employee e
        LEFT JOIN c.category cat
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', CAST(:name as text), '%')))
           and (:employee IS NULL OR LOWER(e.cin) LIKE LOWER(CONCAT('%', CAST(:employee as text), '%')))
           and (:category IS NULL OR LOWER(cat.name) LIKE LOWER(CONCAT('%', CAST(:category as text), '%')))
    """)
    Page<Document> search(String name, String category, String employee, Pageable pageable);


    @Query("""
        SELECT COUNT(d) FROM com.elkhobna.employeemanager.entities.Document d
        LEFT JOIN d.category cat
        WHERE cat.name = :name
    """)
    Long countByCategory(@Param("name") String name);

    @Query("""
        SELECT COUNT(d) FROM com.elkhobna.employeemanager.entities.Document d
        LEFT JOIN d.employee e
        WHERE e.cin = :cin
    """)
    Long countByEmployee(@Param("cin") String cin);
}