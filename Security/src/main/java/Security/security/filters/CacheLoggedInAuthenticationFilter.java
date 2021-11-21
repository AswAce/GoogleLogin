package Security.security.filters;

import Security.cache.CacheManagement;
import Security.config.AppProperties;
import Security.exceptions.OAuth2AuthenticationProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CacheLoggedInAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private CacheManagement cacheManagement;

    @Autowired
    private AppProperties appProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = appProperties.getJwtFromRequest(request);
        if (!cacheManagement.checkIfCacheContainsKey(jwt)) {
            throw new OAuth2AuthenticationProcessingException("User is not logged in");
        }

        filterChain.doFilter(request, response);
    }
}


