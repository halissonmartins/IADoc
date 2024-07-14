package com.halisson.iadoc_application.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {

	public BaseController() {
		super();
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
		return "uploadForm";
	}

}
