package dev.jackson.dog_shelter_api.service;

import dev.jackson.dog_shelter_api.builder.DogDTOBuilder;
import dev.jackson.dog_shelter_api.dto.DogDTO;
import dev.jackson.dog_shelter_api.entity.Dog;
import dev.jackson.dog_shelter_api.exception.DogNotFoundException;
import dev.jackson.dog_shelter_api.exception.InvalidDataException;
import dev.jackson.dog_shelter_api.mapper.DogMapper;
import dev.jackson.dog_shelter_api.repository.DogRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class DogServiceTest {
    private static final Long VALID_DOG_ID = 1L;
    private static final Long INVALID_DOG_ID = 2L;

    @Mock
    private DogRepository dogRepository;

    @Spy
    private DogMapper dogMapper = DogMapper.INSTANCE;

    @InjectMocks
    private DogService dogService;

    @Test
    @DisplayName("When a valid dog's record is given then it should allow the registration")
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
    @DisplayName("When a duplicated dog's record is given then it should throw an exception")
    void whenADuplicatedDogRecordIsInformedThenItShouldThrowAnException(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();
        Dog duplicatedDogRecord = dogMapper.toModel(expectedDogDTO);

        //When
        when(dogRepository.findByName(expectedDogDTO.name())).thenReturn(Optional.of(duplicatedDogRecord));

        assertThrows(InvalidDataException.class, () -> dogService.registerDog(expectedDogDTO));
    }

    @Test
    @DisplayName("When a valid dog's name is given then it should return a record")
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
    @DisplayName("When an invalid dog's name is given then it throws an exception")
    void whenInvalidDogsNameIsGivenThenItThrowsAnException(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();

        //when
        when(dogRepository.findByName(expectedDogDTO.name())).thenReturn(Optional.empty());

        //then
        assertThrows(DogNotFoundException.class, () -> dogService.findByName(expectedDogDTO.name()));
    }

    @Test
    @DisplayName("When ListAll() is called then should return a list of all registered records")
    void whenListAllIsCalledThenReturnAListOfRecords(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();
        Dog expectedDogRecord = dogMapper.toModel(expectedDogDTO);

        //when
        when(dogRepository.findAll()).thenReturn(Collections.singletonList(expectedDogRecord));


        //then
        List<DogDTO> foundRecords = dogService.listAll();

        //using hamcrest
        MatcherAssert.assertThat(foundRecords, Matchers.is(Matchers.not(Matchers.empty())));
        //using junit
        assertEquals(expectedDogDTO, foundRecords.getFirst());
    }

    @Test
    @DisplayName("When ListAll() is called without records then should return an empty list")
    void whenListAllIsCalledThenReturnAnEmptyListOfRecords(){
        //when
        when(dogRepository.findAll()).thenReturn(Collections.emptyList());

        //then
        List<DogDTO> foundRecords = dogService.listAll();

        MatcherAssert.assertThat(foundRecords, Matchers.is(Matchers.empty()));
    }

    @Test
    @DisplayName("When deleteById() is called with valid id, then the record should be deleted")
    void whenDeleteByIdMethodIsCalledWithValidIDThenTheRecordShouldBeDeleted(){
        //Given
        DogDTO expectedDeletedDogDTO = DogDTOBuilder.builder().build().toDogDTO();
        Dog expectedDeletedDogRecord = dogMapper.toModel(expectedDeletedDogDTO);

        //when
        when(dogRepository.findById(VALID_DOG_ID)).thenReturn(Optional.of(expectedDeletedDogRecord));
        doNothing().when(dogRepository).deleteById(VALID_DOG_ID);

        dogService.deleteById(VALID_DOG_ID);

        verify(dogRepository, times(1)).findById(VALID_DOG_ID);
        verify(dogRepository, times(1)).deleteById(VALID_DOG_ID);
    }

    @Test
    @DisplayName("When an invalid dog's id is given to delete then it throws an exception")
    void whenDeleteByIdMethodIsCalledWithInvalidIDThenTheRecordShouldBeDeleted(){
        //when
        when(dogRepository.findById(INVALID_DOG_ID)).thenReturn(Optional.empty());

        //then
        assertThrows(DogNotFoundException.class, () -> dogService.deleteById(INVALID_DOG_ID));
    }

    @Test
    @DisplayName("When a valid dog's id is given then it should allow the update")
    void whenAValidDogsIdIsGivenThenItAllowsToUpdateRecord(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();
        Dog expectedRegistreredDog = dogMapper.toModel(expectedDogDTO);

        //When
        when(dogRepository.findById(VALID_DOG_ID)).thenReturn(Optional.of(expectedRegistreredDog));

        //Then
        dogService.updateDogsRecord(VALID_DOG_ID, expectedDogDTO);
        verify(dogRepository, times(1)).findById(expectedDogDTO.id());
        verify(dogRepository).save(any(Dog.class));
    }

    @Test
    @DisplayName("When an invalid dog's id is given then it should throw an exception")
    void whenAnInvalidDogsIdIsGivenThenItThrowsAnException(){
        //Given
        DogDTO expectedDogDTO = DogDTOBuilder.builder().build().toDogDTO();

        //When
        when(dogRepository.findById(INVALID_DOG_ID)).thenReturn(Optional.empty());

        //Then
        assertThrows(DogNotFoundException.class, () -> dogService.updateDogsRecord(INVALID_DOG_ID, expectedDogDTO));
    }
}
