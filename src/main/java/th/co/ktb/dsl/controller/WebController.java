package th.co.ktb.dsl.controller;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String onenIDForm(Model model, HttpServletRequest request) {
        model.addAttribute("formData", new OpenIDFormData());
        model.addAttribute("platform", request.getParameter("platform"));
        return "mock-openid-authen";
    }
    
    @PostMapping("/openID")
    public String onenIDFormSubmit(
    		@ModelAttribute OpenIDFormData formData,
    		@RequestParam(value="action") String action,
    		@RequestParam(value="platform") String platform,
    		Model model
    	) throws JsonProcessingException  {
    		log.info("onenIDFormSubmit() ");
    		log.info("submit action: {}, platform: {}",action, platform==null?"default":platform);
    	
		String value = Utilities.getObjectMapper().writeValueAsString(formData);
		TempUser tempUser = new TempUser();
		tempUser.setData(value);
		log.info("save temp user info from openID into DB ");
		sql.addTempUser(tempUser);
		Integer refID = tempUser.getRefID();
		String returnURL = null;
		if (platform != null) {
			if ("web".equalsIgnoreCase(platform)) {
				returnURL = sql.getConfig("opendid.return.web.url");
			} else if ("andriod".equalsIgnoreCase(platform)) {
				returnURL = sql.getConfig("opendid.return.android.url");
			} else if ("ios".equalsIgnoreCase(platform)) {
				returnURL = sql.getConfig("opendid.return.ios.url");
			} 
			
			if (returnURL == null) {
				log.info("not found config return url for platform: {} use default",platform);
				returnURL = sql.getConfig("opendid.return.default.url");
			}
			
		}
		String queryStringTemplate = "?status={0}";
		if ("Accept".equalsIgnoreCase(action)) {
			queryStringTemplate += "&AccessToken={1}";
		}
		MessageFormat mf = new MessageFormat(queryStringTemplate);
		String queryString = mf.format(new Object[] {action,refID});
		String ret = "redirect:" + returnURL + queryString;
		log.info("return: {}",ret);
		return ret;
    }
}

