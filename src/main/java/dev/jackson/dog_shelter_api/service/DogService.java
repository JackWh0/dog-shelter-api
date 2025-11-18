package dev.jackson.dog_shelter_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import dev.jackson.dog_shelter_api.exception.DogNotFoundException;
import org.springframework.stereotype.Service;

import dev.jackson.dog_shelter_api.dto.DogDTO;
import dev.jackson.dog_shelter_api.entity.Dog;
import dev.jackson.dog_shelter_api.exception.InvalidDataException;
import dev.jackson.dog_shelter_api.mapper.DogMapper;
import dev.jackson.dog_shelter_api.repository.DogRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DogService {

    private final DogRepository dogRepository;
    private final DogMapper dogMapper = DogMapper.INSTANCE;

    public DogService(DogRepository dogRepository){
        this.dogRepository = dogRepository;
    }

    @Transactional
    public DogDTO registerDog(DogDTO dogDTO){
        verifyIfIsAlreadyRegistered(dogDTO);
        verifyDogAge(dogDTO.age());
        verifyDogGender(dogDTO.gender());

        Dog dog = dogMapper.toModel(dogDTO);
        dog = setDataToUpperCase(dog);
        Dog registredDog = dogRepository.save(dog);
        log.info("Registered dog: {}", registredDog.getName());

        return dogMapper.toDTO(registredDog);
    }

    @Transactional
    public List<DogDTO> listAll(){
        return dogRepository.findAll().stream().map(dogMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public DogDTO findByName(String name) {
        Dog foundBeer = dogRepository.findByName(name.toUpperCase())
                .orElseThrow(() -> new DogNotFoundException("Dog named " + name + " was not found in the system"));
        return dogMapper.toDTO(foundBeer);
    }

    @Transactional
    public void deleteById(Long id) {
        checkIfDogHasRecord(id);
        log.info("Deleting Dog's record: id={}", id);
        dogRepository.deleteById(id);
    }

    @Transactional
    public void updateDogsRecord(Long id, DogDTO dogDTO) {
        if(id.equals(checkIfDogHasRecord(id).getId())) {
            verifyDogAge(dogDTO.age());
            verifyDogGender(dogDTO.gender());

            Dog dog = dogMapper.toModel(dogDTO);
            dog = setDataToUpperCase(dog);

            dog.setId(id);
            dogRepository.save(dog);
            log.info("Updating Dog's record: id={}", id);
        }
    }

    private void verifyIfIsAlreadyRegistered(DogDTO dogDTO){
        String dogName = dogDTO.name();
        Optional<Dog> optDog = dogRepository.findByName(dogName);
        if(optDog.isPresent()){
            String message = String.format("%s has already been registered", optDog.get().getName());
            throw new InvalidDataException(message);
        }
    }



    private void verifyDogAge(String dogAge) {
        List<String> allowedAges = List.of("puppy", "adult", "elderly");
        boolean isAAllowedAgeOption = allowedAges.stream().anyMatch(item -> item.equals(dogAge.toLowerCase()));
        if(!isAAllowedAgeOption){
            throw new InvalidDataException("Incorrect age option. It must be: puppy, adult or elderly");
        }
    }

    private void verifyDogGender(String dogGender) {
        List<String> allowedGenders = List.of("male", "female");
        boolean isAAllowedGenderOption = allowedGenders.stream().anyMatch(item -> item.equals(dogGender.toLowerCase()));
        if(!isAAllowedGenderOption){
            throw new InvalidDataException("Incorrect gender option. It must be: male or female");
        }
    }

    public Dog setDataToUpperCase(Dog dog) {
        dog.setName(dog.getName().toUpperCase());
        dog.setGender(dog.getGender().toUpperCase());
        dog.setAge(dog.getAge().toUpperCase());

        return dog;
    }

    public Dog checkIfDogHasRecord(Long id){
            return dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException("The dog's record was not found."));
    }
}
