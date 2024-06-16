package com.sparta.reviewspotproject.entity;

import com.sparta.reviewspotproject.dto.CommentRequestDto;
import com.sparta.reviewspotproject.dto.PostRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sparta.reviewspotproject.entity.UserStatus.MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentEntityTest {

    @DisplayName("Update")
    @Test
    void CommentUpdate(){
        //Given
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents("New Contents");

        User user = new User("NewUserId", "NewPassWord", "NewUserName","email@gmail.com", MEMBER);

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("New Title")
                .contents("New Content")
                .build();

        Post post = new Post(postRequestDto,user);

        Comment comment = new Comment(commentRequestDto,post,user);
        comment.setContents("contents");

        //When
        comment.update(commentRequestDto);

        //Then
        assertEquals("New Contents",comment.getContents());
    }
}
