package dev.jackson.dog_shelter_api.dto;

import dev.jackson.dog_shelter_api.enums.DogSize;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DogDTO(
    Long id,
    @NotNull @Size(min = 2, max = 20, message = "must be between {min} and {max} characters long") String name,
    @NotNull @Size(min=4, max = 6, message = "must be male or female") String gender,
    @NotNull @Size(min = 5, max = 7, message = "must be puppy, adult or elderly") String age,
    @NotNull DogSize size
){}