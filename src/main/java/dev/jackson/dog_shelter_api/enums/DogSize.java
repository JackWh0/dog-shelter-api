package dev.jackson.dog_shelter_api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DogSize {

    SM("SMALL"),
    ME("MEDIUM"),
    LA("LARGE"),
    GI("Giant");

    private final String description;
}
