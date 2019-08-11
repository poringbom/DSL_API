package th.co.ktb.dsl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@SuppressWarnings("unused")
@Api
//@RestController
//@RequestMapping("/test")
public class TestAPI {
	
	@ApiOperation("Get opeation.")
	@ApiResponses(value = {
	        @ApiResponse(code = 200, message = "Successful"),
	        @ApiResponse(code = 401, message = "No authorization"),
	        @ApiResponse(code = 404, message = "Not found"),
	        @ApiResponse(code = 500, message = "Failure.")
	})
	@GetMapping("/")
	public String getTest(
			@ApiParam(value="Param of operation.", allowEmptyValue=false, defaultValue="World", required=true) 
			@RequestParam String name) {
		return "Hello "+name;
	}
}
