package poc.base.mapper;

import org.mapstruct.Mapper;
import poc.base.dto.EntityDto;
import poc.base.entity.Entity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    EntityDto toDto(Entity entity);

    Entity toEntity(EntityDto dto);

    List<EntityDto> toDtoList(List<Entity> source);

    List<Entity> toEntityList(List<EntityDto> source);
}
