package com.sparta.reviewspotproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.reviewspotproject.config.WebSecurityConfig;
import com.sparta.reviewspotproject.dto.CommentRequestDto;
import com.sparta.reviewspotproject.dto.CommentResponseDto;
import com.sparta.reviewspotproject.dto.PostRequestDto;
import com.sparta.reviewspotproject.entity.Comment;
import com.sparta.reviewspotproject.entity.Post;
import com.sparta.reviewspotproject.entity.User;
import com.sparta.reviewspotproject.entity.UserStatus;
import com.sparta.reviewspotproject.repository.CommentRepository;
import com.sparta.reviewspotproject.security.UserDetailsImpl;
import com.sparta.reviewspotproject.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.sparta.reviewspotproject.entity.UserStatus.MEMBER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)

public class CommentControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @MockBean
    CommentRepository commentRepository;

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
    @DisplayName("댓글 등록")
    void CommentPost() throws Exception {

        this.mockUserSetup();

        //Given
        Long postId = 1L;
        String contents = "이 세상 속에서 반복되는";

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents(contents);

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(1L);
        commentResponseDto.setContents(contents);
        commentResponseDto.setCreatedAt(LocalDateTime.now());
        commentResponseDto.setModifiedAt(LocalDateTime.now());
        commentResponseDto.setCommentLikesCount(0);

        String postInfo = objectMapper.writeValueAsString(commentRequestDto);

        //When
        when(commentService.createComment(anyLong(), any(CommentRequestDto.class), any(User.class))).thenReturn(commentResponseDto);

        //Then
        mvc.perform(post("/api/comment/{postId}",postId)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.contents").value(contents))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andExpect(jsonPath("$.commentLikesCount").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 등록 실패")
    void CommentPostFali() throws Exception {

        this.mockUserSetup();

        Long postId = null;

        //Given
        CommentRequestDto commentRequestDto = new CommentRequestDto();

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(1L);
        commentResponseDto.setContents("");
        commentResponseDto.setCreatedAt(LocalDateTime.now());
        commentResponseDto.setModifiedAt(LocalDateTime.now());
        commentResponseDto.setCommentLikesCount(0);

        String postInfo = objectMapper.writeValueAsString(commentRequestDto);

        when(commentService.createComment(anyLong(), any(CommentRequestDto.class), any(User.class))).thenReturn(commentResponseDto);

        //Then
        mvc.perform(post("/api/comment/{postId}",postId)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정")
    void CommentUpdate() throws Exception {
        this.mockUserSetup();

        Long commentId = 1L;
        String contents = "다시만난 우리의";

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents(contents);

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(1L);
        commentResponseDto.setContents(contents);
        commentResponseDto.setCreatedAt(LocalDateTime.of(2024, 06, 15, 23, 30, 23));
        commentResponseDto.setModifiedAt(LocalDateTime.now());
        commentResponseDto.setCommentLikesCount(0);

        String postInfo = objectMapper.writeValueAsString(commentRequestDto);

        when(commentService.updateComment(anyLong(), any(CommentRequestDto.class), any(User.class))).thenReturn(commentResponseDto);

        mvc.perform(put("/api/comment/{commentId}", commentId)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.contents").value(contents))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.modifiedAt").exists())
                .andExpect(jsonPath("$.commentLikesCount").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 실패")
    void CommentUpdateFali() throws Exception {
        this.mockUserSetup();

        Long commentId = 1L;
        String contents = "";

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents(contents);

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(1L);
        commentResponseDto.setContents(contents);
        commentResponseDto.setCreatedAt(LocalDateTime.of(2024, 06, 15, 23, 30, 23));
        commentResponseDto.setModifiedAt(LocalDateTime.now());
        commentResponseDto.setCommentLikesCount(0);

        String postInfo = objectMapper.writeValueAsString(commentRequestDto);

        when(commentService.updateComment(anyLong(), any(CommentRequestDto.class), any(User.class))).thenReturn(commentResponseDto);

        mvc.perform(put("/api/comment/{commentId}", commentId)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제")
    void CommentDelete() throws Exception {
        this.mockUserSetup();

        Long commentId = 1L;

        when(commentService.deleteComment(anyLong(), any(User.class))).thenReturn(ResponseEntity.ok("댓글이 삭제되었습니다"));

        mvc.perform(delete("/api/comment/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.createdAt").doesNotExist())
                .andExpect(jsonPath("$.modifiedAt").doesNotExist())
                .andExpect(jsonPath("$.commentLikesCount").doesNotExist())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 실패")
    void CommentDeleteFail() throws Exception {
        this.mockUserSetup();

        Long commentId = null;

        when(commentService.deleteComment(anyLong(), any(User.class))).thenReturn(ResponseEntity.ok("댓글이 삭제되었습니다"));

        mvc.perform(delete("/api/comment/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 조회")
    void CommentAll() throws Exception {
        this.mockUserSetup();

        Long postId = 1L;

        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContents("contents");

        User user = new User("NewUserId", "NewPassWord", "NewUserName", "email@gmail.com", MEMBER);

        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("New Title")
                .contents("New Content")
                .build();

        Post post = new Post(postRequestDto, user);

        Comment comment = new Comment(commentRequestDto, post, user);

        CommentResponseDto commentResponseDto1 = new CommentResponseDto(comment);
        commentResponseDto1.setId(1L);
        commentResponseDto1.setContents("내가 비눗방울 부는 방법을 아르켜줄게");
        commentResponseDto1.setCreatedAt(LocalDateTime.now());
        commentResponseDto1.setModifiedAt(LocalDateTime.now());
        commentResponseDto1.setCommentLikesCount(0);

        CommentResponseDto commentResponseDto2 = new CommentResponseDto(comment);
        commentResponseDto2.setId(1L);
        commentResponseDto2.setContents("와 너 정말 잘한다");
        commentResponseDto2.setCreatedAt(LocalDateTime.now());
        commentResponseDto2.setModifiedAt(LocalDateTime.now());
        commentResponseDto2.setCommentLikesCount(0);

        CommentResponseDto commentResponseDto3 = new CommentResponseDto(comment);
        commentResponseDto3.setId(1L);
        commentResponseDto3.setContents("나랑 친구할래?");
        commentResponseDto3.setCreatedAt(LocalDateTime.now());
        commentResponseDto3.setModifiedAt(LocalDateTime.now());
        commentResponseDto3.setCommentLikesCount(0);

        List<CommentResponseDto> comments = Arrays.asList(commentResponseDto1, commentResponseDto2, commentResponseDto3);

        String postInfo = objectMapper.writeValueAsString(comments);

        when(commentService.getAllComment(anyLong())).thenReturn(comments);

        mvc.perform(get("/api/comments/{postId}", postId)
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].contents").value("내가 비눗방울 부는 방법을 아르켜줄게"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].modifiedAt").exists())
                .andExpect(jsonPath("$[0].commentLikesCount").value(0))
                .andExpect(status().isOk())
                .andDo(print());
    }
}




