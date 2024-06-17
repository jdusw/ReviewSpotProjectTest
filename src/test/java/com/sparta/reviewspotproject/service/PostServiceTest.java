package com.sparta.reviewspotproject.service;

import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    PostRepository postRepository;
    @InjectMocks
    PostService postService;

    private User user;

    @BeforeEach
    public void mockUserSetup() {

        user = new User("NewUserId", "NewPassWord", "NewUserName", "email@gmail.com", UserStatus.MEMBER);
        user.setId(1L);

    }

    @Test
    @DisplayName("게시글 생성")
    void createPost() {
        // Given
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("제목")
                .contents("내용")
                .build();

        Post post = new Post(postRequestDto, user);
        post.setId(1L);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        PostResponseDto responseDto = postService.createPost(postRequestDto, user);

        // Then
        assertNotNull(responseDto);
        assertEquals(post.getId(),responseDto.getPostId());
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePost(){

        Long postId = 1L;

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("이제")
                .contents("이마도")
                .build();

        Post post = new Post(postRequestDto, user);
        post.setId(1L);

        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setPostId(1L);
        postResponseDto.setTitle("제목");
        postResponseDto.setContents("내용이겠지?");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        PostResponseDto responseDto = postService.update(postId, postRequestDto, user);

        assertNotNull(responseDto);
    }
}
