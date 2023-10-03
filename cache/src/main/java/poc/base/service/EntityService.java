package poc.base.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import poc.base.entity.Entity;
import poc.base.repository.EntityRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EntityService {
    public static final String ENTITIES_CACHE_NAME = "entities";

    EntityRepository repository;
    CacheProvider cacheProvider;

    @Cacheable(value = ENTITIES_CACHE_NAME, key = "'list'")
    public List<Entity> findAll() {
        return repository.findAll();
    }

    @Cacheable(value = ENTITIES_CACHE_NAME, key = "#id")
    public Optional<Entity> findById(Integer id) {
        return repository.findById(id);
    }

    @CacheEvict(value = ENTITIES_CACHE_NAME, key = "'list'")
    public void persist(Entity entity) {
        Entity saved = repository.save(entity);
        cacheProvider.apply(ENTITIES_CACHE_NAME).put(saved.getId(), saved);
    }

    @CacheEvict(value = ENTITIES_CACHE_NAME, key = "'list'")
    public void update(Integer id, Entity entity) {
        repository.findById(id)
                .ifPresent(existing -> update(entity, existing));
    }

    @Caching(evict = {
            @CacheEvict(value = ENTITIES_CACHE_NAME, key = "#id"),
            @CacheEvict(value = ENTITIES_CACHE_NAME, key = "'list'")
    })
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    private void update(Entity entity, Entity existing) {
        BeanUtils.copyProperties(entity, existing, "id");
        Entity updated = repository.save(existing);
        cacheProvider.apply(ENTITIES_CACHE_NAME).put(existing.getId(), updated);
    }
}
