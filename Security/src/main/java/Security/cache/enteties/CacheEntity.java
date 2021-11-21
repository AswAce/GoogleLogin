package Security.cache.enteties;

import Security.security.UserPrincipal;

import java.time.LocalDateTime;


public class CacheEntity {
    private UserPrincipal userPrincipal;
    private LocalDateTime expiryDate;

    public CacheEntity(UserPrincipal userPrincipal, LocalDateTime expiryDate) {
        this.userPrincipal = userPrincipal;
        this.expiryDate = expiryDate;
    }

    public UserPrincipal getUserPrincipal() {
        return userPrincipal;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
