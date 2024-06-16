package com.sparta.reviewspotproject.entity;

import com.sparta.reviewspotproject.dto.ProfileRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sparta.reviewspotproject.entity.UserStatus.MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserEntityTest {
    @DisplayName("User")
    @Test
    void Constructor(){
        //Given & When
        User user = new User("userId","password", "userName","email", MEMBER);

        //Then
        assertEquals("userId", user.getUserId());
        assertEquals("password", user.getPassword());
        assertEquals("userName", user.getUserName());
        assertEquals("email", user.getEmail());
        assertEquals(MEMBER, user.getUserStatus());
    }

    @DisplayName("UserName UpDate Test")
    @Test
    void UserNameUpDate(){
        //Given
        User user = new User("userId","password", "userName","email", MEMBER);
        ProfileRequestDto profileRequestDto = new ProfileRequestDto();
        profileRequestDto.setUserName("userName1");
        profileRequestDto.setTagLine("이제 그만 되주라 제발");

        //When
        user.update(profileRequestDto);

        //Then
        assertEquals("userName1",user.getUserName());
        assertEquals("이제 그만 되주라 제발",user.getTagLine());
    }
}
