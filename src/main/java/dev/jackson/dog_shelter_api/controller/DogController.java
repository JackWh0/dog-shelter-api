package dev.jackson.dog_shelter_api.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import dev.jackson.dog_shelter_api.dto.DogDTO;
import dev.jackson.dog_shelter_api.service.DogService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/api/v1/dogs")
public class DogController {
    
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping
    public ResponseEntity<List<DogDTO>> listAllDogs(){
        return ResponseEntity.ok().body(dogService.listAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<DogDTO> findByName(@PathVariable String name) {
        DogDTO beer = dogService.findByName(name);
        return ResponseEntity.ok().body(beer);
    }

    @PostMapping
    public ResponseEntity<DogDTO> registerDoString(@RequestBody @Valid DogDTO dogDTO, UriComponentsBuilder uriComponentsBuilder) {
        var dog = dogService.registerDog(dogDTO);
        var uri = uriComponentsBuilder.path("/dog/").buildAndExpand(dog.id()).toUri();
                                    
        return ResponseEntity.created(uri).body(dog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        dogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDogsInfo(@PathVariable Long id, @RequestBody @Valid DogDTO dogDTO) {
        dogService.updateDogsRecord(id, dogDTO);

        return ResponseEntity.ok().body("The dog's record has been updated.");
    }
}
