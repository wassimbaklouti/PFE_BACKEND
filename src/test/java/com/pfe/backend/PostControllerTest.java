package com.pfe.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.anyLong;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.pfe.backend.controller.PostController;
import com.pfe.backend.domain.Comment;
import com.pfe.backend.domain.Like;
import com.pfe.backend.domain.Post;
import com.pfe.backend.service.PostServiceImpl;

public class PostControllerTest {

    @Mock
    private PostServiceImpl postService;  // Mocking PostServiceImpl

    @Mock
    private Post mockPost;  // Mocking Post object

    @InjectMocks
    private PostController postController; // Injecting the mocked service into PostController

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePost_Success() throws Exception {
        // Arrange
        String username = "user1";
        String title = "Test Title";
        String content = "Test Content";
        MultipartFile imageFile = null; // No image file

        // Mock the service call
        when(postService.createPost(username, title, content, imageFile)).thenReturn(mockPost);

        // Act
        ResponseEntity<Post> response = postController.createPost(username, title, content, imageFile);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPost, response.getBody());
    }

    @Test
    public void testCreatePost_WithImage_Success() throws Exception {
        // Arrange
        String username = "user1";
        String title = "Test Title";
        String content = "Test Content";
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "test.jpg", "image/jpeg", "test image content".getBytes());
    
        // Mock the service call
        when(postService.createPost(username, title, content, imageFile)).thenReturn(mockPost);
    
        // Act
        ResponseEntity<Post> response = postController.createPost(username, title, content, imageFile);
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPost, response.getBody());
    }
    

    @Test
    public void testGetPostsByUsername_Success() throws Exception {
        // Arrange
        String username = "user1";
        List<Post> mockPosts = Arrays.asList(mockPost, mockPost); // Mocked list of posts

        // Mock the service call
        when(postService.getPostsByUsername(username)).thenReturn(mockPosts);

        // Act
        ResponseEntity<List<Post>> response = postController.getPostsByUsername(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPosts, response.getBody());
    }

    @Test
    public void testGetAllPosts_Success() throws Exception {
        // Arrange
        List<Post> mockPosts = Arrays.asList(mockPost, mockPost); // Mocked list of posts

        // Mock the service call
        when(postService.getAllPosts()).thenReturn(mockPosts);

        // Act
        ResponseEntity<List<Post>> response = postController.getAllPosts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPosts, response.getBody());
    }


    @Test
    public void testUpdatePost_Success() throws Exception {
        // Arrange
        String postId = "post1";
        String username = "user1";
        String title = "Updated Title";
        String content = "Updated Content";
        MultipartFile imageFile = null; // No new image file

        // Mock the service call
        when(postService.updatePost(postId, username, title, content, imageFile)).thenReturn(mockPost);

        // Act
        ResponseEntity<Post> response = postController.updatePost(postId, username, title, content, imageFile);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPost, response.getBody());
    }

    @Test
    public void testUpdatePost_WithImage_Success() throws Exception {
        // Arrange
        String postId = "post1";
        String username = "user1";
        String title = "Updated Title";
        String content = "Updated Content";
        MockMultipartFile imageFile = new MockMultipartFile("imageFile", "updated.jpg", "image/jpeg", "updated image content".getBytes());

        // Mock the service call
        when(postService.updatePost(postId, username, title, content, imageFile)).thenReturn(mockPost);

        // Act
        ResponseEntity<Post> response = postController.updatePost(postId, username, title, content, imageFile);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPost, response.getBody());
    }
    @Test
    public void testAddComment_Success() {
        // Arrange
        String postId = "post1";
        String username = "user1";
        String content = "This is a comment";
        Comment mockComment = mock(Comment.class);  // Mock the Comment object

        // Mock the service call
        when(postService.addComment(postId, username, content)).thenReturn(mockComment);

        // Act
        ResponseEntity<Comment> response = postController.addComment(postId, username, content);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockComment, response.getBody());
    }

    @Test
    public void testGetComments_Success() {
        // Arrange
        String postId = "post1";
        List<Comment> mockComments = Arrays.asList(mock(Comment.class), mock(Comment.class));  // Mock the list of comments

        // Mock the service call
        when(postService.getCommentsByPostId(postId)).thenReturn(mockComments);

        // Act
        ResponseEntity<List<Comment>> response = postController.getComments(postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockComments, response.getBody());
    }
    @Test
    public void testUpdateComment_Success() {
        // Arrange
        String commentId = "comment1";
        Comment updatedComment = mock(Comment.class);  // Mock the updated comment
        Optional<Comment> mockUpdatedComment = Optional.of(updatedComment);

        // Mock the service call
        when(postService.updateComment(commentId, updatedComment)).thenReturn(mockUpdatedComment);

        // Act
        ResponseEntity<Comment> response = postController.updateComment(commentId, updatedComment);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedComment, response.getBody());
    }

    @Test
    public void testUpdateComment_NotFound() {
        // Arrange
        String commentId = "comment1";
        Comment updatedComment = mock(Comment.class);  // Mock the updated comment

        // Mock the service call to return empty (comment not found)
        when(postService.updateComment(commentId, updatedComment)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Comment> response = postController.updateComment(commentId, updatedComment);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testDeleteComment_Success() {
        // Arrange
        String commentId = "comment1";

        // Mock the service call
        when(postService.deleteComment(commentId)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = postController.deleteComment(commentId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteComment_NotFound() {
        // Arrange
        String commentId = "comment1";

        // Mock the service call to return false (comment not found)
        when(postService.deleteComment(commentId)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = postController.deleteComment(commentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testAddLike_Success() {
        // Arrange
        String postId = "post1";
        String username = "user1";

        // Mock Like
        Like mockLike = mock(Like.class);

        // Mock the service call
        when(postService.addLike(postId, username)).thenReturn(mockLike);

        // Act
        ResponseEntity<Like> response = postController.addLike(postId, username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockLike, response.getBody());
    }
    @Test
    public void testGetLikes_Success() {
        // Arrange
        String postId = "post1";

        // Mock list of Likes
        List<Like> mockLikes = Arrays.asList(
            mock(Like.class),
            mock(Like.class)
        );

        // Mock the service call
        when(postService.getLikesByPostId(postId)).thenReturn(mockLikes);

        // Act
        ResponseEntity<List<Like>> response = postController.getLikes(postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockLikes, response.getBody());
    }

    @Test
    public void testHasLiked_Success() {
        // Arrange
        String postId = "post1";
        String username = "user1";

        // Mock the result
        boolean mockHasLiked = true;

        // Mock the service call
        when(postService.hasUserLikedPost(postId, username)).thenReturn(mockHasLiked);

        // Act
        ResponseEntity<Boolean> response = postController.hasLiked(postId, username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockHasLiked, response.getBody());
    }

    @Test
    public void testHasLiked_Failure() {
        // Arrange
        String postId = "post1";
        String username = "user1";

        // Mock the result
        boolean mockHasLiked = false;

        // Mock the service call
        when(postService.hasUserLikedPost(postId, username)).thenReturn(mockHasLiked);

        // Act
        ResponseEntity<Boolean> response = postController.hasLiked(postId, username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockHasLiked, response.getBody());
    }
    @Test
    public void testUnlikePost_Success() {
        // Arrange
        String postId = "post1";
        String username = "user1";

        // No return value, just a method call
        doNothing().when(postService).unlikePost(postId, username);

        // Act
        ResponseEntity<String> response = postController.unlikePost(postId, username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post unliked successfully", response.getBody());
    }

}

