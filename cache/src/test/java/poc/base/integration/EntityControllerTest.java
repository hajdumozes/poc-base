package poc.base.integration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import poc.base.dto.EntityDto;
import poc.base.entity.Entity;
import poc.base.integration.config.AbstractIntegrationTest;
import poc.base.mapper.EntityMapper;
import poc.base.repository.EntityRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static poc.base.service.EntityService.ENTITIES_CACHE_NAME;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntityControllerTest extends AbstractIntegrationTest {
    public static final String BASE_PATH = "/entities";

    @Autowired
    EntityRepository repository;

    @Autowired
    EntityMapper mapper;

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void init() {
        repository.deleteAll();
    }

    @Test
    void shouldStoreInListCache_onFindingAll() throws Exception {
        // given
        Entity saved = repository.save(Entity.builder().description("test entity").build());

        // when
        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk());

        // then
        assertEquals(getCachedList(), List.of(saved));
    }

    @Test
    void shouldStoreInCache_onFindingById() throws Exception {
        // given
        Entity storedEntity = Entity.builder().description("test entity").build();
        int persistedId = repository.save(storedEntity).getId();
        storedEntity.setId(persistedId);

        // when
        mockMvc.perform(get(String.format("%s/%s", BASE_PATH, persistedId)))
                .andExpect(status().isOk());

        // then
        assertEquals(getCachedEntity(storedEntity.getId()), Optional.of(storedEntity));
    }

    @Test
    void shouldModifyCaches_onPersisting() throws Exception {
        // given
        EntityDto entityToStore = EntityDto.builder().description("test entity").build();

        // when
        mockMvc.perform(post(BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(entityToStore)))
                .andExpect(status().isOk());

        // then
        Entity storedEntity = repository.findAll().get(0);
        assertEquals(getCachedList(), List.of());
        assertEquals(getCachedEntity(storedEntity.getId()), Optional.of(storedEntity));

    }

    @Test
    void shouldModifyCaches_onUpdating() throws Exception {
        // given
        Entity storedEntity = Entity.builder().description("test entity").build();
        int persistedId = repository.save(storedEntity).getId();
        EntityDto update = EntityDto.builder().id(persistedId).description("new description").build();

        // when
        mockMvc.perform(put(String.format("%s/%s", BASE_PATH, persistedId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(update)))
                .andExpect(status().isOk());

        // then
        Entity updatedEntity = repository.findById(persistedId).orElse(Entity.builder().build());
        assertEquals(getCachedList(), List.of());
        assertEquals(getCachedEntity(updatedEntity.getId()), Optional.of(updatedEntity));
    }

    @Test
    void shouldEmptyCaches_onDeleting() throws Exception {
        // given
        Entity storedEntity = Entity.builder().description("test entity").build();
        int persistedId = repository.save(storedEntity).getId();

        // when
        mockMvc.perform(delete(String.format("%s/%s", BASE_PATH, persistedId)))
                .andExpect(status().isOk());

        // then
        assertEquals(getCachedList(), List.of());
        assertEquals(getCachedEntity(persistedId), Optional.empty());
    }

    private Optional<Entity> getCachedEntity(Integer id) {
        return Optional.ofNullable(cacheManager.getCache(ENTITIES_CACHE_NAME)).map(c -> c.get(id, Entity.class));
    }

    private List<?> getCachedList() {
        return Optional.ofNullable(cacheManager.getCache(ENTITIES_CACHE_NAME)).map(c -> c.get("list", List.class))
                .orElse(List.of());
    }
}
