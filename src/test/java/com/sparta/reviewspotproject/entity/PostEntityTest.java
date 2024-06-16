package com.sparta.reviewspotproject.entity;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sparta.reviewspotproject.entity.UserStatus.MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostEntityTest {

    @DisplayName("PostUpdate Test")
    @Test
    void PostUpdate(){
        //Given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("이건 제목")
                .contents("이건 내용")
                .build();

        User user = new User();
        user.setUserId("Raquel");
        user.setPassword("NewPassword");
        user.setUserName("NewUserName");
        user.setEmail("email@gmail.com");
        user.setUserStatus(MEMBER);


        Post post = new Post(postRequestDto,user);
        post.setTitle("title");
        post.setContents("contents");
        post.setUser(new User());

        //When
        post.update(postRequestDto,user);

        //Then
        assertEquals("이건 제목",postRequestDto.getTitle());
        assertEquals("이건 내용",postRequestDto.getContents());
        assertEquals("Raquel",user.getUserId());
        assertEquals("NewPassword",user.getPassword());
        assertEquals("NewUserName",user.getUserName());
        assertEquals("email@gmail.com",user.getEmail());
        assertEquals(MEMBER,user.getUserStatus());
    }

}
