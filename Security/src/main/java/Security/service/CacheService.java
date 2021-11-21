package Security.service;

import Security.cache.CacheManagement;
import Security.security.UserPrincipal;
import Security.web.dto.UserDataForAnotherApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CacheService {


    @Autowired
    CacheManagement cacheManager;

    public void logOutUSerFromCache(String token) {

        cacheManager.removeValue(token);
    }

    public UserDataForAnotherApp getUserFromToken(String token) {
//Todos: think what to return UserPrincipal , this dto with string authorities or
        //list GrantedAuthority
        UserPrincipal loginUserIdFromToken = cacheManager.getLoginUserIdFromToken(token);
        String email = loginUserIdFromToken.getEmail();
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) loginUserIdFromToken.getAuthorities().stream().toList();
        List<String> authorities =
                loginUserIdFromToken.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String id = (String) loginUserIdFromToken.getAttributes().get("sub");
        UserDataForAnotherApp user = new UserDataForAnotherApp
                (email, loginUserIdFromToken.getAttribute("name"), id);
        user.getAuthorities().addAll(authorities);
        return user;

    }
}
