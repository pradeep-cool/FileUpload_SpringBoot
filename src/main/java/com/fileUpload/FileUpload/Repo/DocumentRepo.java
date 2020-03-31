package com.fileUpload.FileUpload.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fileUpload.FileUpload.domain.Document;

public interface DocumentRepo extends JpaRepository<Document, Long>{

	Document findByDocName(String fileName);


}
