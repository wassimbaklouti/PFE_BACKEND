package com.pfe.backend.service;

import com.pfe.backend.domain.Comment;
import com.pfe.backend.domain.Like;
import com.pfe.backend.domain.Post;
import com.pfe.backend.domain.User;
import com.pfe.backend.repository.CommentRepository;
import com.pfe.backend.repository.LikeRepository;
import com.pfe.backend.repository.PostRepository;
import com.pfe.backend.repository.UserRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.pfe.backend.constant.FileConstant.*;

@Service
@Transactional
public class PostServiceImpl {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    // Créer un nouveau post avec une image
    public Post createPost(String username, String title, String content, MultipartFile image) throws IOException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        Post newPost = new Post(title, content, null, new Date(), username);

        // Save the post first to generate its ID
        Post savedPost = postRepository.save(newPost);

        // Handle image saving
        String imageUrl = savePostImage(savedPost.getId(), image);
        savedPost.setImage_url(imageUrl);

        return postRepository.save(savedPost);
    }

    // Récupérer tous les posts d'un utilisateur par son username
    public List<Post> getPostsByUsername(String username) {
        return postRepository.findByUsername(username);
    }

    // Récupérer tous les posts de tous les utilisateurs
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Method to handle saving the post image
    private String savePostImage(String postId, MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null; // No image uploaded
        }

        String imageFolder = POST_FOLDER + postId; // Define where the post image should be saved
        Path postFolderPath = Paths.get(imageFolder).toAbsolutePath().normalize();

        if (!Files.exists(postFolderPath)) {
            Files.createDirectories(postFolderPath);
            LOGGER.info("Created directory: " + postFolderPath);
        }

        String fileName = postId + DOT + JPG_EXTENSION; // Define the image filename (using post ID)
        Path imagePath = postFolderPath.resolve(fileName);
        Files.copy(image.getInputStream(), imagePath, REPLACE_EXISTING); // Save the image

        // Return the URL for accessing the image
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(POST_IMAGE_PATH + postId + FORWARD_SLASH + fileName)
                .toUriString();
    }

    public Post updatePost(String postId, String username, String title, String content, MultipartFile image) throws IOException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        // Fetch the existing post
        Optional<Post> existingPostOpt = postRepository.findById(postId);
        if (!existingPostOpt.isPresent()) {
            throw new RuntimeException("Post non trouvé");
        }

        Post existingPost = existingPostOpt.get();
        existingPost.setTitle(title);
        existingPost.setContent(content);
        existingPost.setUpdatedAt(new Date());

        // Handle image updating if a new image is provided
        if (image != null && !image.isEmpty()) {
            String imageUrl = savePostImage(existingPost.getId(), image); // Save new image
            existingPost.setImage_url(imageUrl);
        }

        // Save the updated post
        return postRepository.save(existingPost);
    }


    // Delete post method (optionally handle image deletion)
    public void deletePost(String postId) throws IOException {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            // Delete post images from the file system
            //Path postFolderPath = Paths.get(POST_FOLDER + postId).toAbsolutePath().normalize();
            //FileUtils.deleteDirectory(new File(postFolderPath.toString()));
            postRepository.delete(post);
        }
    }

    public Comment addComment(String postId, String username, String content) {
        Comment comment = new Comment(postId, username, content, new Date());
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }

    public Optional<Comment> updateComment(String commentId, Comment updatedComment) {
        return commentRepository.findById(commentId).map(comment -> {
            comment.setContent(updatedComment.getContent());
            comment.setDate(updatedComment.getDate());
            return commentRepository.save(comment);
        });
    }

    public boolean deleteComment(String commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        }
        return false;
    }
    public Like addLike(String postId, String username) {
        Like like = new Like(postId, username);
        return likeRepository.save(like);
    }

    public List<Like> getLikesByPostId(String postId) {
        return likeRepository.findByPostId(postId);
    }

    public boolean hasUserLikedPost(String postId, String username) {
        List<Like> likes = getLikesByPostId(postId);

        // Check if the username exists in the list of likes
        return likes.stream()
                .anyMatch(like -> like.getUsername().equals(username));
    }

    // Unlike the post (remove the like from the list)
    public void unlikePost(String postId, String username) {
        List<Like> likes = getLikesByPostId(postId);

        // Find the like associated with the username
        Like likeToRemove = likes.stream()
                .filter(like -> like.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User has not liked this post"));

        // Remove the like from the repository
        likeRepository.delete(likeToRemove);
    }

}
