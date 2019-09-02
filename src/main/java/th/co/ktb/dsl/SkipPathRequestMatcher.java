package th.co.ktb.dsl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkipPathRequestMatcher implements RequestMatcher {  
    private OrRequestMatcher matchers;
    private RequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<EndPointMethodPath> pathsToSkip, String processingPath) {
        List<RequestMatcher> m = pathsToSkip.
        		stream().
        			map(mp -> new AntPathRequestMatcher(mp.getPath(), mp.getMethod().toString())).
        				collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
    	Boolean matchExceptionalPath = matchers.matches(request);
        if (matchExceptionalPath) {
        	log.trace("request uri: "+request.getRequestURI()+" is match exceptional path: "+matchExceptionalPath);
            return false;
        }
        Boolean matchProcessPath = processingMatcher.matches(request) ? true : false;
    	log.trace("request uri: "+request.getRequestURI()+" is match processing path: "+matchProcessPath);
        return matchProcessPath;
    }
}