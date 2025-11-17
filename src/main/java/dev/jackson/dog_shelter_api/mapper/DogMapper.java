package dev.jackson.dog_shelter_api.mapper;

import org.mapstruct.Mapper;

import dev.jackson.dog_shelter_api.dto.DogDTO;
import dev.jackson.dog_shelter_api.entity.Dog;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DogMapper {
    DogMapper INSTANCE = Mappers.getMapper(DogMapper.class);

    Dog toModel(DogDTO dogDTO);
    DogDTO toDTO(Dog dog);
}
