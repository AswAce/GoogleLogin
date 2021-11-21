package Security.cache;

import Security.cache.enteties.CacheEntity;
import Security.config.AppProperties;
import Security.db.repository.UserRepository;
import Security.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class CacheManagement {
    private static Map<String, CacheEntity> cache = new LinkedHashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppProperties appProperties;

    public void putElement(String key, Date expireDate, UserPrincipal user) {

        cache.putIfAbsent(key, new CacheEntity
                (user, appProperties.convertToLocalDateTimeViaInstant(expireDate)));

    }


    public boolean checkIfCacheContainsKey(String key) {
        return cache.containsKey(key);
    }

    public void removeValue(String key) {
        cache.remove(key);

    }

    public UserPrincipal getLoginUserIdFromToken(String token) {
        return cache.get(token).getUserPrincipal();
    }

    public void cleanWholeCache() {
        cache.clear();
    }

    public void removeExpireValues() {
        cache.forEach((k, cacheEntity) -> {
            if (cacheEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
                removeValue(k);
            }
        });
    }
}
