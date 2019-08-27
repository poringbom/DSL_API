package th.co.ktb.dsl;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathRequestMatcher implements RequestMatcher {  
    private OrRequestMatcher processingMatcher;

    public PathRequestMatcher(List<String> pathsToSkip) {
        List<RequestMatcher> m = pathsToSkip.
        		stream().
        			map(path -> new AntPathRequestMatcher(path)).
        				collect(Collectors.toList());
        processingMatcher = new OrRequestMatcher(m);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        Boolean matchProcessPath = processingMatcher.matches(request) ? true : false;
        log.info("request uri: "+request.getRequestURI()+" is match processing path: "+matchProcessPath);
        return matchProcessPath;
    }
}