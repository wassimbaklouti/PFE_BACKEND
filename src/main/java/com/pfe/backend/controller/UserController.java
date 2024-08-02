package com.pfe.backend.controller;

import com.pfe.backend.domain.UserPrincipal;
import com.pfe.backend.domain.User;
import com.pfe.backend.domain.HttpResponse;
import com.pfe.backend.exception.ExceptionHandling;
import com.pfe.backend.exception.domain.*;
import com.pfe.backend.service.UserService;
import com.pfe.backend.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.pfe.backend.constant.FileConstant.*;
import static com.pfe.backend.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static com.pfe.backend.constant.UserImplConstant.EMAIL_SENT;
import static com.pfe.backend.constant.UserImplConstant.USER_DELETED_SUCCESSFULY;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/", "/user"})

public class UserController extends ExceptionHandling {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUsers() {
        long count = userService.getTotalUsers();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveUsers() {
        long count = userService.getActiveUsers();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/inactive/count")
    public ResponseEntity<Long> getInactiveUsers() {
        long count = userService.getInactiveUsers();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/not-locked-count")
    public ResponseEntity<Long> getNotLockedUsersCount() {
        long count = userService.getNotLockedUsers();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/locked-count")
    public ResponseEntity<Long> getLockedUsersCount() {
        long count = userService.getLockedUsers();
        return ResponseEntity.ok(count);
    }


	@GetMapping("/emails")
	public ResponseEntity<List<String>> getEmails() {
		List<String> emails = userService.getEmails();
		return ResponseEntity.ok(emails);
	}

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getPhoneNumber(),user.getCity());
        return new ResponseEntity<>(newUser, OK);
    }

	@GetMapping("/connected-user")
	public ResponseEntity<User> getConnectedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Log the authentication information
		System.out.println("Authentication: " + authentication);

		if (authentication == null || !authentication.isAuthenticated()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String username = authentication.getName();

		// Log the username retrieved from authentication
		System.out.println("Username: " + username);

		User connectedUser = userService.findUserByUsername(username);

		// Log the user object retrieved
		System.out.println("Connected User: " + connectedUser);

		if (connectedUser == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(connectedUser, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<User> addNewUser(@RequestParam("firstName") String firstName ,
										   @RequestParam("lastName") String lastName ,
										   @RequestParam("email") String username ,
										   @RequestParam("username") String email ,
										   @RequestParam("role") String role ,
										   @RequestParam("isActive") String isActive , 
										   @RequestParam("isNotLocked") String isNotLocked ,
										   @RequestParam(value ="profileImage", required = false ) MultipartFile profileImage ) throws UsernameExistException, EmailExistException, IOException , NotAnImageFileException {
		User newUser = userService.addNewUser(firstName, lastName, username, email, role, 
				Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNotLocked), profileImage) ;
		return new  ResponseEntity<>(newUser,OK);
	}
	
	
	@PostMapping("/update")
	public ResponseEntity<User> updateUser(@RequestParam("firstName") String firstName ,
										   @RequestParam("lastName") String lastName , 
										   @RequestParam("currentUsername") String currentUsername ,
										   @RequestParam("username") String username ,
										   @RequestParam("email") String email ,
										   @RequestParam("role") String role ,
										   @RequestParam("isActive") String isActive , 
										   @RequestParam("isNotLocked") String isNotLocked ,
										   @RequestParam("phoneNumber") long phoneNumber ,
										   @RequestParam("city") String city ,
										   @RequestParam(value ="profileImage", required = false ) MultipartFile profileImage ) throws  NotAnImageFileException , UsernameExistException, EmailExistException, IOException{
		User updateUser = userService.updateUser( currentUsername ,firstName, lastName, username, email, role, 
				Boolean.parseBoolean(isActive), Boolean.parseBoolean(isNotLocked), profileImage,phoneNumber,city) ;
		return new  ResponseEntity<>(updateUser, OK);
	}
	
	
	@GetMapping("/find/{username}")
	public  ResponseEntity<User> getUser (@PathVariable("username") String username) {
		User user = userService.findUserByUsername(username);
		return new  ResponseEntity<>(user, OK);
	}
	

	@GetMapping("/list")
	public  ResponseEntity<List<User>> getAllUser () {
		List <User> users = userService.getUsers();
		return new  ResponseEntity<>(users, OK);
	}
	
	@GetMapping("/resetPassword/{email}")
	public  ResponseEntity<HttpResponse> resetPassword (@PathVariable("email") String email) throws EmailNotFoundException {
		 userService.resetPassword(email);
		return  response( OK ,EMAIL_SENT + email) ;
	}
	
	@DeleteMapping("/delete/{username}")
	//@PreAuthorize("hasAnyAuthority('user:delete')")
	public ResponseEntity<HttpResponse> deleteUser (@PathVariable("username") String username) throws IOException{
		userService.deleteUser(username);
		return response(OK ,USER_DELETED_SUCCESSFULY) ; 
	}
	
	
	@PostMapping("/updateProfileImage")
	public ResponseEntity<User> updateProfileImage(@RequestParam("username") String username ,
												   @RequestParam(value ="profileImage") MultipartFile profileImage ) throws UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
		User user = userService.updateProfileImage(username, profileImage) ;
		return new  ResponseEntity<>(user,HttpStatus.OK);
	}
	
		
	@GetMapping(path = "/image/{username}/{fileName}" , produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getProfileImage(@PathVariable ("username") String username ,@PathVariable ("fileName") String fileName) throws IOException {
		return Files.readAllBytes(Paths.get(USER_FOLDER + username +FORWARD_SLASH +fileName )) ;
	}
	
	
	@GetMapping(path = "/image/profile/{username}" , produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getTempProfileImage(@PathVariable ("username") String username) throws IOException {
		URL url = new URL (TEMP_PROFILE_IMAGE_BASE_URL+username) ;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream() ;
		try (InputStream inputStream = url.openStream()) {
			int byteRead; 
			byte[] chunk = new byte[1024] ;
			while ((byteRead = inputStream.read(chunk)) > 0) {
				byteArrayOutputStream.write(chunk, 0, byteRead);
			}
		}
		return byteArrayOutputStream.toByteArray() ;
	 }
	
	
	
	private  ResponseEntity<HttpResponse> response (HttpStatus httpStatus , String message ){
		HttpResponse body = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase()	,message.toUpperCase() )  ;
		return new  ResponseEntity<>( body , httpStatus ) ;
	}
		
    
    

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
		System.out.println("JWT_TOKEN: " + jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}