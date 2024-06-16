package com.sparta.reviewspotproject.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostRequestDtoTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void tearDown() {
        validatorFactory.close();
    }

    @DisplayName("Title & Content 생성 테스트")
    @Test
    void getTitle(){
        //Given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("제목")
                .contents("내용")
                .build();

        //When & Then
        assertEquals("제목",postRequestDto.getTitle());
        assertEquals("내용",postRequestDto.getContents());
    }

    @DisplayName("Validation content blank 에러")
    @Test
    void getContentBlank(){
        //Given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("제목")
                .contents("")
                .build();
        //When
        Set<ConstraintViolation<PostRequestDto>> violations = validator.validate(postRequestDto);

        //Then
        assertThat(violations).isNotEmpty();
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다");
        });
    }
}
