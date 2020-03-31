package com.fileUpload.FileUpload.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fileUpload.FileUpload.Repo.DocumentRepo;
import com.fileUpload.FileUpload.domain.Document;

import ch.qos.logback.core.util.ContentTypeUtil;

@RestController
@RequestMapping(value = "/file")
public class MyController {
	
	@Autowired
	private DocumentRepo docs;
	
	//post call will store the given file into DB and returns url for getting it.
	@PostMapping("/upload/db")
	public ResponseEntity uploadToDB(@RequestParam("file") MultipartFile file) {
		Document doc = new Document();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		doc.setDocName(fileName);
		try {
			doc.setFile(file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		docs.save(doc);
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/file/download/")
				.path(fileName).path("/db")
				.toUriString();
		return ResponseEntity.ok(fileDownloadUri);
	}
	
	
	// get call will extract all the contents presnt inside a file and returns as a result
	@GetMapping("/download/{fileName:.+}/db")
	public ResponseEntity downloadFromDB(@PathVariable String fileName) {
		Document document = docs.findByDocName(fileName);
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(document.getFile());
	}

}
