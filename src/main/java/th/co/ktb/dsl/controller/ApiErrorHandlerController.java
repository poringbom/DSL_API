package th.co.ktb.dsl.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.exception.ApiException;
import th.co.ktb.dsl.exception.BadRequestException;
import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.exception.ServerException;
import th.co.ktb.dsl.mock.TestableException;
import th.co.ktb.dsl.model.common.ApiResponseError;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ApiErrorHandlerController extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ ApiException.class, Exception.class })
	public ResponseEntity<ApiResponseError> handleApiException (Exception ex) {
		log.info("handleApiException() error instanceof {}",((UndeclaredThrowableException)ex).getUndeclaredThrowable().getClass());
		log.info("TestableException = {}",(((UndeclaredThrowableException)ex).getUndeclaredThrowable() instanceof TestableException));
		HttpStatus status;

		if (ex instanceof BadRequestException) {
			status = HttpStatus.BAD_REQUEST;
			
		} else if (ex instanceof ClientException) {
			status = HttpStatus.CONFLICT;
			
		} else if (ex instanceof ServerException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			
		} else if (((UndeclaredThrowableException)ex).getUndeclaredThrowable() instanceof TestableException) {
			TestableException t = (TestableException) ((UndeclaredThrowableException)ex).getUndeclaredThrowable();
			ResponseEntity<ApiResponseError> ret;
			if (t.getApiResponseError() != null) {
				ret = new ResponseEntity<ApiResponseError>(t.getApiResponseError(), HttpStatus.valueOf(t.getStatusCode()));
			} else {
				ApiResponseError error = createApiResponseError(t.getCause());
				ret = new ResponseEntity<ApiResponseError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return ret;
			
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		ResponseEntity<ApiResponseError> response = new ResponseEntity<ApiResponseError>(createApiResponseError(ex),status);
		return response;
	}
	
	@Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, 
            HttpHeaders headers,HttpStatus status, WebRequest request) {
		super.handleExceptionInternal(ex, body, headers, status, request);
		String code = status.value() +"-000";
		ApiResponseError apiError = new ApiResponseError(code,ex.getMessage());
		apiError.setTrace(Utilities.filterStackTrace(ex).getStackTrace());
		return new ResponseEntity<Object>(apiError,status);
	}
	
	private ApiResponseError createApiResponseError(Throwable t) {
		if (t instanceof ApiException ) {
			ApiException e = (ApiException) t;
			Utilities.filterStackTrace(t);
			if (log.isInfoEnabled()) {
				String trace = stackTraceToString(t);
				log.error(">> {}", trace);
			}
			ApiResponseError apiError =new ApiResponseError(e.getCode(), e.getMessage(), t.getStackTrace());
			return apiError;
		} else {
			return new ApiResponseError("500-000", t.getMessage(), Utilities.filterStackTrace(t).getStackTrace());
		}
	}
	
	private String stackTraceToString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String sStackTrace = sw.toString(); // stack trace as a string
		return sStackTrace;
	}
}
