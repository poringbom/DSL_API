package th.co.ktb.dsl.controller;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
import th.co.ktb.dsl.Utilities;
import th.co.ktb.dsl.mock.ServiceSQL;
import th.co.ktb.dsl.model.user.OpenIDFormData;
import th.co.ktb.dsl.model.user.OpenIDFormData.TempUser;

@Controller
@Slf4j
public class WebController {
	@Autowired ServiceSQL sql;
	
    @GetMapping("/openID")
    public String onenIDForm(Model model) {
        model.addAttribute("formData", new OpenIDFormData());
        return "mock-openid-authen";
    }
    
    @PostMapping("/openID")
    public String onenIDFormSubmit(
    		@ModelAttribute OpenIDFormData formData,
    		Model model
    	) throws JsonProcessingException  {
		String value = Utilities.getObjectMapper().writeValueAsString(formData);
		TempUser tempUser = new TempUser();
		tempUser.setData(value);
		log.info("save temp user info from openID into DB ");
		sql.addTempUser(tempUser);
		Integer refID = tempUser.getRefID();
		String returnURL = sql.getConfig("opendid.return.default.url");
		String queryStringTemplate = "?refID={0}";
		MessageFormat mf = new MessageFormat(queryStringTemplate);
		String queryString = mf.format(new Object[] {refID});
		String ret = "redirect:" + returnURL + queryString;
		log.info("return: {}",ret);
		return ret;
    }
}

