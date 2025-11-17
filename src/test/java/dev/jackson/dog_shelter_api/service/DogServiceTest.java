package dev.jackson.dog_shelter_api.service;

import dev.jackson.dog_shelter_api.builder.DogDTOBuilder;
import dev.jackson.dog_shelter_api.dto.DogDTO;
import dev.jackson.dog_shelter_api.entity.Dog;
import dev.jackson.dog_shelter_api.exception.DogNotFoundException;
import dev.jackson.dog_shelter_api.exception.InvalidDataException;
import dev.jackson.dog_shelter_api.mapper.DogMapper;
import dev.jackson.dog_shelter_api.repository.DogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class DogServiceTest {

    private static final Long INVALID_DOG_ID = 1L;

    @Mock
    private DogRepository dogRepository;

    @Spy
    private DogMapper dogMapper = DogMapper.INSTANCE;

    @InjectMocks
    private DogService dogService;

    @Test
    void whenTheDogsRecordIsInformedThenItShouldAllowTheRegistration(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();
        Dog expectedRegistreredDog = dogMapper.toModel(expectedDogDTO);

        //When
        when(dogRepository.findByName(expectedDogDTO.name())).thenReturn(Optional.empty());
        when(dogRepository.save(any(Dog.class))).thenReturn(expectedRegistreredDog);

        //Then
        DogDTO actualRegisteredDog = dogService.registerDog(expectedDogDTO);

        assertEquals(expectedRegistreredDog.getId(), actualRegisteredDog.id());
        assertEquals(expectedRegistreredDog.getName(), actualRegisteredDog.name());
        assertEquals((expectedRegistreredDog.getGender()), actualRegisteredDog.gender());
    }

    @Test
    void whenADuplicatedDogRecordIsInformedThenItShouldThrowAnException(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();
        Dog duplicatedDogRecord = dogMapper.toModel(expectedDogDTO);

        //When
        when(dogRepository.findByName(expectedDogDTO.name())).thenReturn(Optional.of(duplicatedDogRecord));

        assertThrows(InvalidDataException.class, () -> dogService.registerDog(expectedDogDTO));
    }

    @Test
    void whenValidDogsNameIsGivenThenARecordIsReturned(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();
        Dog expecteDogRecord = dogMapper.toModel(expectedDogDTO);

        //when
        when(dogRepository.findByName(expecteDogRecord.getName())).thenReturn(Optional.of(expecteDogRecord));

        //then
        DogDTO foundDogDTO = dogService.findByName(expectedDogDTO.name());

        assertEquals(expectedDogDTO, foundDogDTO);
    }

    @Test
    void whenInvalidDogsNameIsGivenThenItThrowsAnException(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();

        //when
        when(dogRepository.findByName(expectedDogDTO.name())).thenReturn(Optional.empty());

        //then
        assertThrows(DogNotFoundException.class, () -> dogService.findByName(expectedDogDTO.name()));


    }
}
