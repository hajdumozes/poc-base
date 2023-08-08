package poc.base.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import poc.base.entity.Entity;
import poc.base.repository.EntityRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityServiceTest {
    private static final int ENTITY_ID = 1;

    @InjectMocks
    EntityService underTest;

    @Mock
    EntityRepository repository;

    @Test
    void shouldReturnRepoResult_onFindAll() {
        // given
        List<Entity> entities = List.of(
                Entity.builder().id(ENTITY_ID).description("test entity").build()
        );
        when(repository.findAll()).thenReturn(entities);

        // when
        List<Entity> result = underTest.findAll();

        // then
        assertThat(result).isEqualTo(entities);
    }

    @Test
    void shouldReturnRepoResult_onFindById() {
        // given
        Entity entity = Entity.builder().id(ENTITY_ID).description("test entity").build();
        when(repository.findById(ENTITY_ID)).thenReturn(Optional.of(entity));

        // when
        Optional<Entity> result = underTest.findById(ENTITY_ID);

        // then
        assertThat(result).isNotEmpty().isEqualTo(Optional.of(entity));
    }

    @Test
    void shouldCallRepoSave_onPersist() {
        // given
        Entity entity = Entity.builder().id(ENTITY_ID).description("test entity").build();

        // when
        underTest.persist(entity);

        // then
        verify(repository).save(entity);
    }

    @Test
    void shouldCallRepoSave_onUpdate_givenExistingRecord() {
        // given
        Entity entity = Entity.builder().id(ENTITY_ID).description("test entity").build();
        when(repository.findById(ENTITY_ID)).thenReturn(Optional.of(entity));

        // when
        underTest.update(ENTITY_ID, entity);

        // then
        verify(repository).save(entity);
    }

    @Test
    void shouldNotCallRepoSave_onUpdate_givenNonExistingRecord() {
        // given
        Entity entity = Entity.builder().id(ENTITY_ID).description("test entity").build();

        // when
        underTest.update(ENTITY_ID, entity);

        // then
        verify(repository, times(0)).save(entity);

    }

    @Test
    void shouldCallRepoDelete_onDelete_givenExistingRecord() {
        // given
        Entity entity = Entity.builder().id(ENTITY_ID).description("test entity").build();
        when(repository.findById(ENTITY_ID)).thenReturn(Optional.of(entity));

        // when
        underTest.delete(ENTITY_ID);

        // then
        verify(repository).delete(entity);
    }

    @Test
    void shouldNotCallRepoDelete_onDelete_givenNonExistingRecord() {
        // when
        underTest.delete(ENTITY_ID);

        // then
        verify(repository, times(0)).delete(any());
    }
}