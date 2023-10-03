package poc.base.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CacheProvider implements Function<String, Cache> {
    CacheManager cacheManager;

    @Override
    public Cache apply(String name) {
        return cacheManager.getCache(name);
    }
}
