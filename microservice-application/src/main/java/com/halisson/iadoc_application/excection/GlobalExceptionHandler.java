package com.halisson.iadoc_application.excection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler({StorageFileNotFoundException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleStorageFileNotFound(StorageFileNotFoundException exc) {
		log.error(exc.getMessage(), exc);
		return exc.getMessage();
	}
	
	@ExceptionHandler({NotFoundException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String handleNotFound(NotFoundException exc) {
		log.error(exc.getMessage(), exc);
		return exc.getMessage();
	}
	
	@ExceptionHandler({AlreadyExistsException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String handleAlreadyExists(AlreadyExistsException exc) {
		log.error(exc.getMessage(), exc);
		return exc.getMessage();
	}
	
	@ExceptionHandler({RuntimeException.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRuntimeException(RuntimeException exc) {
		log.error(exc.getMessage(), exc);
        return exc.getMessage();
    }
}
