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

public class SignUpDtoTest {

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

    @DisplayName("Validation userId patter 에러")
    @Test
    void getUserIdPattern() {
        //given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUserId("악");
        signupRequestDto.setUserName("itmaname1");
        signupRequestDto.setPassword("Ttteessttt01!");
        signupRequestDto.setEmail("test@gmail.com");

        //When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        //Then
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("사용자 이름은 대소문자 포함 영문자와 숫자로 이루어진 10자에서 20자 사이여야 합니다.");
        });
    }


}
