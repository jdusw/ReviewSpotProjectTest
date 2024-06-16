package com.sparta.reviewspotproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.reviewspotproject.config.WebSecurityConfig;
import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.dto.PostResponseDto;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.PostRepository;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
import com.sparta.reviewspotproject.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.sparta.reviewspotproject.entity.UserStatus.MEMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {PostController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)

public class PostControllerTest {


    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @MockBean
    PostRepository postRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        String userId = "tteesstt01";
        String password = "Ttteessttt01!";
        String userName = "test";
        String email = "test@gmail.com";
        UserStatus userStatus = MEMBER;

        User testUser = new User(userId, password, userName, email, userStatus);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());

    }

    @Test
    @DisplayName("게시글 등록")
    void postPost() throws Exception {
        this.mockUserSetup();

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("이게 맞는걸까")
                .contents("이렇게하는걸까..?")
                .build();

        String postInfo = objectMapper.writeValueAsString(postRequestDto);

        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setPostId(1L);
        postResponseDto.setUserId(1L);
        postResponseDto.setTitle(postRequestDto.getTitle());
        postResponseDto.setContents(postRequestDto.getContents());
        postResponseDto.setCreateAt(LocalDateTime.now());
        postResponseDto.setModifiedAt(LocalDateTime.now());
        postResponseDto.setPostLikesCount(0);

        //When
        when(postService.createPost(any(PostRequestDto.class), any(User.class))).thenReturn(postResponseDto);

        //Then
        mvc.perform(post("/api/posts")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.title").value("이게 맞는걸까"))
                .andExpect(jsonPath("$.contents").value("이렇게하는걸까..?"))
                .andExpect(jsonPath("$.createAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andExpect(jsonPath("$.postLikesCount").value(0))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 등록 실패")
    void postPostFali() throws Exception {
        this.mockUserSetup();

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("이게 맞는걸까")
                .contents("")
                .build();

        String postInfo = objectMapper.writeValueAsString(postRequestDto);

        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setPostId(1L);
        postResponseDto.setUserId(1L);
        postResponseDto.setTitle(postRequestDto.getTitle());
        postResponseDto.setContents(postRequestDto.getContents());
        postResponseDto.setCreateAt(LocalDateTime.now());
        postResponseDto.setModifiedAt(LocalDateTime.now());
        postResponseDto.setPostLikesCount(0);

        //When
        when(postService.createPost(any(PostRequestDto.class), any(User.class))).thenReturn(postResponseDto);

        //Then
        mvc.perform(post("/api/posts")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    void updatePost() throws Exception {
        this.mockUserSetup();

        Long postId = 1L;

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("이게 맞는걸까")
                .contents("이렇게하는걸까..?")
                .build();

        String postInfo = objectMapper.writeValueAsString(postRequestDto);

        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setPostId(postId);
        postResponseDto.setUserId(1L);
        postResponseDto.setTitle(postRequestDto.getTitle());
        postResponseDto.setContents(postRequestDto.getContents());
        postResponseDto.setCreateAt(LocalDateTime.of(2024, 06, 16, 3, 12, 23));
        postResponseDto.setModifiedAt(LocalDateTime.now());
        postResponseDto.setPostLikesCount(0);

        //When
        when(postService.update(any(Long.class), any(PostRequestDto.class), any(User.class))).thenReturn(postResponseDto);

        //Then
        mvc.perform(put("/api/posts/{postId}", postId)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() throws Exception {
        this.mockUserSetup();

        Long postId = 1L;

        mvc.perform(delete("/api/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 실패")
    void deletePostFail() throws Exception {
        this.mockUserSetup();

        Long postId = null;

        mvc.perform(delete("/api/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }


    @Test
    @DisplayName("게시글 조회")
    void getPost() throws Exception {
        this.mockUserSetup();

        Long postId = 1L;

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("이제는 될 때가 되었습니다")
                .contents("되라고")
                .build();

        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setPostId(1L);
        postResponseDto.setUserId(1L);
        postResponseDto.setTitle(postRequestDto.getTitle());
        postResponseDto.setContents(postRequestDto.getContents());
        postResponseDto.setCreateAt(LocalDateTime.now());
        postResponseDto.setModifiedAt(LocalDateTime.now());
        postResponseDto.setPostLikesCount(0);

        when(postService.getPost(any(Long.class))).thenReturn(postResponseDto);

        mvc.perform(get("/api/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.title").value("이제는 될 때가 되었습니다"))
                .andExpect(jsonPath("$.contents").value("되라고"))
                .andExpect(jsonPath("$.createAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andExpect(jsonPath("$.postLikesCount").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 전체 조회")
    void getPosts() throws Exception {
        this.mockUserSetup();

        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setPostId(1L);
        postResponseDto.setUserId(1L);
        postResponseDto.setTitle(".");
        postResponseDto.setContents(".");
        postResponseDto.setCreateAt(LocalDateTime.now());
        postResponseDto.setModifiedAt(LocalDateTime.now());
        postResponseDto.setPostLikesCount(0);

        PostResponseDto postResponseDto2 = new PostResponseDto();
        postResponseDto2.setPostId(1L);
        postResponseDto2.setUserId(1L);
        postResponseDto2.setTitle(".");
        postResponseDto2.setContents(".");
        postResponseDto2.setCreateAt(LocalDateTime.now());
        postResponseDto2.setModifiedAt(LocalDateTime.now());
        postResponseDto2.setPostLikesCount(0);

        List<PostResponseDto> posts = Arrays.asList(postResponseDto, postResponseDto2);

        String postInfo = objectMapper.writeValueAsString(posts);

        when(postService.getPosts()).thenReturn(posts);

        mvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postInfo)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 전체 조회 중 계시글이 없을 경우")
    void getPostX() throws Exception {
        this.mockUserSetup();

        List<PostResponseDto> posts = Collections.emptyList();

        String postInfo = objectMapper.writeValueAsString(posts);

        when(postService.getPosts()).thenReturn(posts);

        mvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postInfo)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}

