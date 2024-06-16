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

public class CommentDtoTest {
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

    @Test
    void getContent(){
        //Given
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents("내용 테스트");

        //When & Then
        assertEquals("내용 테스트", commentRequestDto.getContents());
    }

    @DisplayName("Validation contents blank 에러")
    @Test
    void getContentBlank(){
        //Given
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents("");

        //When
        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(commentRequestDto);

        //Then
        assertThat(violations).isNotEmpty();
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("내용을 입력해주세요.");
        });
    }
}
