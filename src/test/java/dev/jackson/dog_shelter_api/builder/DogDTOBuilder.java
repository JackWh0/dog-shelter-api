package dev.jackson.dog_shelter_api.builder;

import dev.jackson.dog_shelter_api.dto.DogDTO;
import dev.jackson.dog_shelter_api.enums.DogSize;
import lombok.Builder;

@Builder
public class DogDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "JAY";

    @Builder.Default
    private String gender = "MALE";

    @Builder.Default
    private String age = "ADULT";

    @Builder.Default
    private DogSize size = DogSize.GI;

    public DogDTO toDogDTO(){
        return new DogDTO(id, name, gender, age, size);
    }

    public DogDTO toDogDTO(String name){
        return new DogDTO(id, name, gender, age, size);
    }
}
