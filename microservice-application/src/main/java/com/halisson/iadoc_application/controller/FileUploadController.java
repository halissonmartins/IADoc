package com.halisson.iadoc_application.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.halisson.iadoc_application.controller.interfaces.IFileUploadController;
import com.halisson.iadoc_application.service.DocumentService;

@RestController
@RequestMapping("upload")
public class FileUploadController implements IFileUploadController {

	private final DocumentService documentService;

	public FileUploadController(
			DocumentService documentService) {
		this.documentService = documentService;
	}
	
	@Override
	@PostMapping
	public ResponseEntity<?> handleFilesUpload(@RequestParam("files") MultipartFile[] files,
			RedirectAttributes redirectAttributes) {
		
		documentService.saveAndStoreBatch(files);
		
		var filesNames = Arrays.asList(files).stream()
				.map(MultipartFile::getOriginalFilename)
				.collect(Collectors.joining(", "));		
		
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + filesNames + "!");
		
		return ResponseEntity.accepted().build();
	}

}
