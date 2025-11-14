package dev.jackson.dog_shelter_api.mapper;

import org.mapstruct.Mapper;

import dev.jackson.dog_shelter_api.dto.DogDTO;
import dev.jackson.dog_shelter_api.entity.Dog;

@Mapper(componentModel = "spring")
public interface DogMapper {
    Dog toModel(DogDTO dogDTO);
    DogDTO toDTO(Dog dog);
}
