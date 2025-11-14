package dev.jackson.dog_shelter_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jackson.dog_shelter_api.entity.Dog;
import java.util.Optional;


@Repository
public interface DogRepository extends JpaRepository<Dog, Long>{
    Optional<Dog> findByName(String name);

} 
