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

    @Test
    void getAll() {
        //Given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUserId("testidee01");
        signupRequestDto.setPassword("Testpassword1!");
        signupRequestDto.setUserName("test");
        signupRequestDto.setEmail("test@gmail.com");

        //When & Then
        assertEquals("testidee01", signupRequestDto.getUserId());
        assertEquals("Testpassword1!", signupRequestDto.getPassword());
        assertEquals("test", signupRequestDto.getUserName());
        assertEquals("test@gmail.com", signupRequestDto.getEmail());
    }

    @DisplayName("Validation userId patter 에러")
    @Test
    void getUserIdPattern() {
        //given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUserId("악");
        signupRequestDto.setPassword("Ttteessttt01!");
        signupRequestDto.setUserName("test");
        signupRequestDto.setEmail("test@gmail.com");

        //When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        //Then
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("사용자 이름은 대소문자 포함 영문자와 숫자로 이루어진 10자에서 20자 사이여야 합니다.");
        });
    }

    @DisplayName("Validation password patter 에러")
    @Test
    void getPassWordPattern () {
        //Given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUserId("tteesstt01");
        signupRequestDto.setPassword("nooooooo");
        signupRequestDto.setUserName("test");
        signupRequestDto.setEmail("test@gmail.com");

        //When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        //Then
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("비밀번호는 대소문자 영문, 숫자, 특수문자를 최소 1글자씩 포함하며 최소 10자 이상이어야 합니다.");
        });
    }

    @DisplayName("Validation userName blank 에러")
    @Test
    void getUserNameBlank () {
        //given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUserId("tteesstt01");
        signupRequestDto.setPassword("Ttteessttt01!");
        signupRequestDto.setUserName("");
        signupRequestDto.setEmail("test@gmail.com");

        //When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        //Then
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이름을 입력해주세요");
        });
    }

    @DisplayName("Validation email blank 에러")
    @Test
    void getEmailBlank () {
        //given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUserId("testidee01");
        signupRequestDto.setPassword("Testpassword1!");
        signupRequestDto.setUserName("test");
        signupRequestDto.setEmail("");

        //When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        //Then
        assertThat(violations).isNotEmpty();
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("Email을 입력해주세요.");
        });
    }

    @DisplayName("Validation email 에러")
    @Test
    void getEmailError () {
        //given
        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setUserId("testidee01");
        signupRequestDto.setPassword("Testpassword1!");
        signupRequestDto.setUserName("test");
        signupRequestDto.setEmail("testgmail.com");

        //When
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        //Then
        violations.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("유효한 이메일을 입력해주세요.");
        });
    }
}
