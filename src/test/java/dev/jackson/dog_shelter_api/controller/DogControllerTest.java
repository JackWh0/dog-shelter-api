package dev.jackson.dog_shelter_api.controller;

import dev.jackson.dog_shelter_api.builder.DogDTOBuilder;
import dev.jackson.dog_shelter_api.dto.DogDTO;
import dev.jackson.dog_shelter_api.exception.DogNotFoundException;
import dev.jackson.dog_shelter_api.service.DogService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static dev.jackson.dog_shelter_api.utils.JsonConvertionUtils.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DogControllerTest {

    private static final String API_URL_PATH = "/api/v1/dogs";
    private static final long VALID_ID = 1L;
    private static final long INVALID_ID = 2L;

    private MockMvc mockMvc;

    @Mock
    private DogService dogService;

    @InjectMocks
    private DogController dogController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dogController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTMethodIsCalledThenADogRecordIsRegistered() throws Exception {
        //given
        DogDTO dogDTO = DogDTOBuilder.builder().build().toDogDTO();

        //when
        when(dogService.registerDog(dogDTO)).thenReturn(dogDTO);

        //then
        mockMvc.perform(post(API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", Matchers.is(dogDTO.name())))
                .andExpect(jsonPath("$.age", Matchers.is(dogDTO.age())))
                .andExpect(jsonPath("$.size", Matchers.is(dogDTO.size().toString())));
    }

    @Test
    void whenPOSTMethodIsCalledWithoutARequiredFieldThenItShouldReturnAnError() throws Exception {
        //given
        DogDTO dogDTO = DogDTOBuilder.builder().build().toDogDTO(null);

        //then
        mockMvc.perform(post(API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetMethodIsCalledWithValidNameThenItReturnsARecord() throws Exception {
        //given
        DogDTO dogDTO = DogDTOBuilder.builder().build().toDogDTO();

        when(dogService.findByName(dogDTO.name())).thenReturn(dogDTO);

        mockMvc.perform(get(API_URL_PATH + "/" + dogDTO.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is(dogDTO.name())))
                .andExpect(jsonPath("$.age", Matchers.is(dogDTO.age())))
                .andExpect(jsonPath("$.size", Matchers.is(dogDTO.size().toString())));
    }

    @Test
    void whenGetMethodIsCalledWithInvalidNameThenItShouldThrowAnException() throws Exception {
        DogDTO dogDTO = DogDTOBuilder.builder().build().toDogDTO();

        when(dogService.findByName(dogDTO.name())).thenThrow(DogNotFoundException.class);

        try {
            mockMvc.perform(get(API_URL_PATH + "/" + dogDTO.name())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            System.out.println("gotcha");
        }
    }

    @Test
    void whenGetMethodIsCalledWithoutParamsThenItReturnsOkStatus() throws Exception {
        //given
        DogDTO dogDTO = DogDTOBuilder.builder().build().toDogDTO();

        when(dogService.listAll()).thenReturn(Collections.singletonList(dogDTO));

        mockMvc.perform(get(API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Matchers.is(dogDTO.name())))
                .andExpect(jsonPath("$[0].age", Matchers.is(dogDTO.age())))
                .andExpect(jsonPath("$[0].size", Matchers.is(dogDTO.size().toString())));
    }

    @Test
    void whenDeleteMethodIsCalledWithValidIDThenItReturnsNoContentStatus() throws Exception {
        doNothing().when(dogService).deleteById(VALID_ID);

        mockMvc.perform(delete(API_URL_PATH + "/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteMethodIsCalledWithInvalidIDThenItShouldThrowAnException() throws Exception {
        try {
            doThrow(DogNotFoundException.class).when(dogService).deleteById(INVALID_ID);

            mockMvc.perform(delete(API_URL_PATH + "/" + INVALID_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            System.out.println("gotcha");
        }
    }

    @Test
    void whenPUTMethodIsCalledWithValidIDThenOkStatusIsReturned() throws Exception {
        //given
        DogDTO dogDTO = DogDTOBuilder.builder().build().toDogDTO();

        //when
        doNothing().when(dogService).updateDogsRecord(VALID_ID, dogDTO);

        //then
        mockMvc.perform(put(API_URL_PATH + "/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void whenPUTMethodIsCalledWithInvalidIDThenItShouldThrowAnException() throws Exception {
        try {
            //given
            DogDTO dogDTO = DogDTOBuilder.builder().build().toDogDTO();

            //when
            doThrow(new DogNotFoundException("")).when(dogService).updateDogsRecord(INVALID_ID, dogDTO);

            //then
            mockMvc.perform(put(API_URL_PATH + "/" + INVALID_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(dogDTO)))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            System.out.println("gotcha");
        }
    }
}
