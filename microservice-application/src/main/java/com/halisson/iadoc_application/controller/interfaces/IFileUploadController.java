package com.halisson.iadoc_application.controller.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File Upload", description = "Endpoint for manager upload of PDF files.")
public interface IFileUploadController {

	@Operation(summary = "Save the PDF files in format Multipart")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "The PDF file was uploaded successfully."),
			@ApiResponse(responseCode = "404", description = "Unable to upload PDF file."),
	})
	ResponseEntity<?> handleFilesUpload(MultipartFile[] files, RedirectAttributes redirectAttributes);

}