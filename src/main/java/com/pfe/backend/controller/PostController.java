package com.pfe.backend.controller;

import com.pfe.backend.domain.Comment;
import com.pfe.backend.domain.Like;
import com.pfe.backend.domain.Post;
import com.pfe.backend.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.pfe.backend.constant.FileConstant.*;

@RestController
@RequestMapping({"/api/posts","/post"})
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostServiceImpl postService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(
            @RequestPart("username") String username,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile // Handle the image as optional
    ) {
        try {
            // Handle imageFile (save it to the server or cloud storage if present)
            /*String imageUrl = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                imageUrl = postService.savePostImage(imageFile); // Use the service's saveImage method
            }*/

            // Create the post using the image URL (if available)
            Post newPost = postService.createPost(username, title, content, imageFile);

            return ResponseEntity.ok(newPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint to get all posts by a user's username
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username) {
        List<Post> posts = postService.getPostsByUsername(username);
        return ResponseEntity.ok(posts);
    }

    // Endpoint to get all posts from all users
    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    @GetMapping(path = "/image/{postId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPostImage(@PathVariable("postId") String postId) {
        System.out.println(postId);
        try {
            // Construct the file path
            Path imagePath = Paths.get(POST_FOLDER + postId + ".jpg"); // Assuming JPG extension

            if (Files.exists(imagePath)) {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                return ResponseEntity.ok(imageBytes);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(value = "/update/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> updatePost(
            @PathVariable("postId") String postId,
            @RequestPart("username") String username,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile // Handle the image as optional
    ) {
        try {
            // Update the post
            Post updatedPost = postService.updatePost(postId, username, title, content, imageFile);
            System.out.println("updatinng ...");
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable String postId) {
        try {
            postService.deletePost(postId);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } catch (IOException e) {
            // Handle exceptions, such as issues with file deletion
            return new ResponseEntity<>("Failed to delete post due to an internal error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Post not found or could not be deleted", HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/comment")
    public ResponseEntity<Comment> addComment(
            @RequestParam String postId,
            @RequestParam String username,
            @RequestParam String content) {
        Comment comment = postService.addComment(postId, username, content);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable String postId) {
        List<Comment> comments = postService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/comments/update/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") String commentId, @RequestBody Comment updatedComment) {
        Optional<Comment> updated = postService.updateComment(commentId, updatedComment);
        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/comments/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") String commentId) {
        boolean deleted = postService.deleteComment(commentId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/like")
    public ResponseEntity<Like> addLike(
            @RequestParam String postId,
            @RequestParam String username) {
        Like like = postService.addLike(postId, username);
        return ResponseEntity.ok(like);
    }

    @GetMapping("/likes/{postId}")
    public ResponseEntity<List<Like>> getLikes(@PathVariable String postId) {
        List<Like> likes = postService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/has-liked")
    public ResponseEntity<Boolean> hasLiked(@RequestParam String postId, @RequestParam String username) {
        boolean hasLiked = postService.hasUserLikedPost(postId, username);
        return ResponseEntity.ok(hasLiked);
    }

    // API to unlike a post
    @PostMapping("/unlike")
    public ResponseEntity<String> unlikePost(@RequestParam String postId, @RequestParam String username) {
        postService.unlikePost(postId, username);
        return ResponseEntity.ok("Post unliked successfully");
    }


}
