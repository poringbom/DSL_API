package th.co.ktb.dsl.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
import th.co.ktb.dsl.DSLApiApplication;
import th.co.ktb.dsl.exception.ApiException;
import th.co.ktb.dsl.exception.BadRequestException;
import th.co.ktb.dsl.exception.ClientException;
import th.co.ktb.dsl.exception.ServerException;
import th.co.ktb.dsl.model.common.ApiResponseError;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ApiErrorHandlerController extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({ ApiException.class, Exception.class })
	public ResponseEntity<ApiResponseError> handleApiException (Exception ex) {
		HttpStatus status;
		if (ex instanceof BadRequestException) {
			status = HttpStatus.BAD_REQUEST;
			
		} else if (ex instanceof ClientException) {
			status = HttpStatus.CONFLICT;
			
		} else if (ex instanceof ServerException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			
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
		apiError.setTrace(filterStackTrace(ex).getStackTrace());
		return new ResponseEntity<Object>(apiError,status);
	}
	
	private ApiResponseError createApiResponseError(Throwable t) {
		if (t instanceof ApiException ) {
			ApiException e = (ApiException) t;
			filterStackTrace(t);
			if (log.isInfoEnabled()) {
				String trace = stackTraceToString(t);
				log.error(">> {}", trace);
			}
			ApiResponseError apiError =new ApiResponseError(e.getCode(), e.getMessage(), t.getStackTrace());
			return apiError;
		} else {
			return new ApiResponseError("500-000", t.getMessage(), filterStackTrace(t).getStackTrace());
		}
	}
	
    private final String ROOT_PACKAGE_NAME = DSLApiApplication.class.getPackage().getName();
    private Throwable filterStackTrace(Throwable t) {
    		DSLApiApplication.class.getPackage().getName();
		List<StackTraceElement> stList= new ArrayList<StackTraceElement>();
		if (t != null) {
			for(StackTraceElement s : t.getStackTrace()) {
				String clzName = s.getClassName();
				if (clzName.startsWith(ROOT_PACKAGE_NAME) && !clzName.contains("CGLIB$$")){
					stList.add(s);
				} 
			}
			t.setStackTrace(stList.toArray(new StackTraceElement[0]));
			if (t.getCause() != null) filterStackTrace(t.getCause());
		}	
		return t;
    }
	
	private String stackTraceToString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String sStackTrace = sw.toString(); // stack trace as a string
		return sStackTrace;
	}
}
