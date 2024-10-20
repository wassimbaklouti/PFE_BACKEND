package com.pfe.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.pfe.backend.controller.UserController;
import com.pfe.backend.domain.DateTravail;
import com.pfe.backend.domain.User;
import com.pfe.backend.domain.UserPrincipal;
import com.pfe.backend.exception.domain.UserNotFoundException;
import com.pfe.backend.service.StripeService;
import com.pfe.backend.service.UserService;
import com.pfe.backend.utility.JWTTokenProvider;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.Map;
import java.util.HashMap;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JWTTokenProvider jwtTokenProvider;


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private StripeService stripeService;

    @BeforeEach
    public void setUp() {
        assertNotNull(userController);
        assertNotNull(userService);
        assertNotNull(modelMapper);
        assertNotNull(authenticationManager);
        assertNotNull(jwtTokenProvider);
        SecurityContextHolder.clearContext();
        assertNotNull(stripeService);
    }
    @Test
    public void testRegister_Success() throws Exception {
        // Mock user data
        String firstName = "test";
        String lastName = "usser";
        String username = "testUser";
        String email = "testUser@example.com";
        String role = "ROLE_USER";
        long phoneNumber = 1234567890L;
        String city = "Sfax";
        String expertise = "Software Engineering";
        DateTravail datetravail = new DateTravail(null, null);
        String mockPassword = "testUser";  // Add a mock password

        // Create a User instance for the expected return value
        User expectedUser = new User();
        expectedUser.setFirstName(firstName);
        expectedUser.setLastName(lastName);
        expectedUser.setUsername(username);
        expectedUser.setEmail(email);
        expectedUser.setRole(role);
        expectedUser.setPhoneNumber(phoneNumber);
        expectedUser.setCity(city);
        expectedUser.setExpertise(expertise);
        expectedUser.setDatetravail(datetravail);
        expectedUser.setPassword(mockPassword);  // Set the mock password
        expectedUser.setUserId("mockUserId"); // Set a mock user ID if needed
        expectedUser.setJoinDate(new Date()); // Set other fields as needed

        // Mock user service behavior
        Mockito.when(userService.register(
                firstName,
                lastName,
                username,
                email,
                role,
                phoneNumber,
                city,
                expertise,
                datetravail
        )).thenReturn(expectedUser);

        // Perform the request (using a new User object)
        User userToRegister = new User();
        userToRegister.setFirstName(firstName);
        userToRegister.setLastName(lastName);
        userToRegister.setUsername(username);
        userToRegister.setEmail(email);
        userToRegister.setRole(role);
        userToRegister.setPhoneNumber(phoneNumber);
        userToRegister.setCity(city);
        userToRegister.setExpertise(expertise);
        userToRegister.setDatetravail(datetravail);

        ResponseEntity<User> responseEntity = userController.register(userToRegister);

        // Assert response status and body
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUser, responseEntity.getBody());
    }

    @Test
    public void testLogin_Success() throws Exception {
        // Setup test data
        String username = "testUser";
        String password = "testUser";

        // Create a User instance to be returned by the mocked user service
        User loginUser = new User();
        loginUser.setUsername(username);
        loginUser.setPassword(password);

        // Mock authentication manager behavior
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Use lenient stubbing for userService
        lenient().when(userService.findUserByUsername(username)).thenReturn(loginUser);

        // Create a UserPrincipal object based on the loginUser
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);

        // Mock JWT token generation to return a dynamic value based on the userPrincipal
        when(jwtTokenProvider.generateJwtToken(any(UserPrincipal.class))).thenAnswer(invocation -> {
            UserPrincipal principal = invocation.getArgument(0);
            return "token_for_" + principal.getUsername(); // Dynamically create the token based on the username
        });

        // Perform the login request
        ResponseEntity<User> responseEntity = userController.login(loginUser);

        // Log response for debugging
        System.out.println("Response Status: " + responseEntity.getStatusCode());
        System.out.println("Response Body: " + responseEntity.getBody());

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(loginUser, responseEntity.getBody());
    }


    @Test
    public void testGetConnectedUser_Success() {
        // Mock successful authentication
        String username = "testUser";


        // Set up the security context with a mock authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(authentication.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create a fully mocked User object
        User connectedUser = mock(User.class);
        when(connectedUser.getUsername()).thenReturn(username);

        // Mock userService to return the mocked User
        when(userService.findUserByUsername(username)).thenReturn(connectedUser);

        // Perform the request to get the connected user
        ResponseEntity<User> responseEntity = userController.getConnectedUser();

        // Debugging output
        System.out.println("Response Status: " + responseEntity.getStatusCode());
        System.out.println("Response Body: " + responseEntity.getBody());

        // Assert successful response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(connectedUser.getUsername(), responseEntity.getBody().getUsername());
    }




    @Test
    public void testGetConnectedUser_Unauthorized() {
        // Clear the security context
        SecurityContextHolder.getContext().setAuthentication(null);

        // Perform the request without passing a token
        ResponseEntity<User> responseEntity = userController.getConnectedUser();

        // Assert unauthorized response
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testGetTotalUsers_Success() {
        // Mock the expected count of users
        long expectedCount = 100L;
        when(userService.getTotalUsers()).thenReturn(expectedCount);

        // Perform the request
        ResponseEntity<Long> responseEntity = userController.getTotalUsers();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCount, responseEntity.getBody());
    }

    @Test
    public void testGetActiveUsers_Success() {
        // Mock the expected count of active users
        long expectedCount = 75L;
        when(userService.getActiveUsers()).thenReturn(expectedCount);

        // Perform the request
        ResponseEntity<Long> responseEntity = userController.getActiveUsers();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCount, responseEntity.getBody());
    }

    @Test
    public void testGetInactiveUsers_Success() {
        // Mock the expected count of inactive users
        long expectedCount = 25L;
        when(userService.getInactiveUsers()).thenReturn(expectedCount);

        // Perform the request
        ResponseEntity<Long> responseEntity = userController.getInactiveUsers();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCount, responseEntity.getBody());
    }

    @Test
    public void testGetNotLockedUsersCount_Success() {
        // Mock the expected count of not locked users
        long expectedCount = 90L;
        when(userService.getNotLockedUsers()).thenReturn(expectedCount);

        // Perform the request
        ResponseEntity<Long> responseEntity = userController.getNotLockedUsersCount();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCount, responseEntity.getBody());
    }

    @Test
    public void testGetLockedUsersCount_Success() {
        // Mock the expected count of locked users
        long expectedCount = 10L;
        when(userService.getLockedUsers()).thenReturn(expectedCount);

        // Perform the request
        ResponseEntity<Long> responseEntity = userController.getLockedUsersCount();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCount, responseEntity.getBody());
    }

    @Test
    public void testGetEmails_Success() {
        // Mock the expected list of emails
        List<String> expectedEmails = List.of("user1@example.com", "user2@example.com");
        when(userService.getEmails()).thenReturn(expectedEmails);

        // Perform the request
        ResponseEntity<List<String>> responseEntity = userController.getEmails();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedEmails, responseEntity.getBody());
    }

    @Test
    public void testAddNewUser_Success() throws Exception {
        // Create a mock User object
        User mockUser = mock(User.class);

        // Mock the userService behavior with argument matchers
        when(userService.addNewUser("First", "Last", "testUser", "testUser@example.com", "ROLE_USER", 1234567890L, "City", null, "Expertise"))
                .thenReturn(mockUser);

        // Perform the request through the controller
        ResponseEntity<User> responseEntity = userController.addNewUser(
                "First", "Last", "testUser", "testUser@example.com", "ROLE_USER",
                1234567890L, "City", "Expertise", null);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUser, responseEntity.getBody());

        // Verify that the service method was called with correct parameters
        verify(userService).addNewUser(
                eq("First"), eq("Last"), eq("testUser"), eq("testUser@example.com"),
                eq("ROLE_USER"), eq(1234567890L), eq("City"), eq(null), eq("Expertise"));
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        // Create a mock User object
        User mockUser = mock(User.class);

        // Mock the MultipartFile
        MultipartFile mockFile = mock(MultipartFile.class);

        // Mock the userService behavior with exact parameter values
        lenient().when(userService.updateUser(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyBoolean(), anyBoolean(), any(), anyLong(), anyString(), anyString())).thenReturn(mockUser);

        // Perform the request through the controller
        ResponseEntity<User> responseEntity = userController.updateUser(
                "First",
                "Last",
                "currentUsername",
                "updatedUser",
                "updatedUser@example.com",
                "ROLE_USER",
                "true",
                "true",
                1234567890L,
                "City",
                "Expertise",
                mockFile);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUser, responseEntity.getBody());

        // Verify that the service method was called with correct parameters
        verify(userService).updateUser(
                eq("currentUsername"),
                eq("First"),
                eq("Last"),
                eq("updatedUser"),
                eq("updatedUser@example.com"),
                eq("ROLE_USER"),
                eq(true),
                eq(true),
                eq(mockFile),
                eq(1234567890L),
                eq("City"),
                eq("Expertise"));
    }

    @Test
    public void testGetUser_Success() {
        // Mock username and user object
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        // Mock userService behavior
        when(userService.findUserByUsername(username)).thenReturn(user);

        // Perform the request
        ResponseEntity<User> responseEntity = userController.getUser(username);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    public void testGetAllUsers_Success() {
        // Mock a list of users
        List<User> users = List.of(new User(), new User());
        when(userService.getUsers()).thenReturn(users);

        // Perform the request
        ResponseEntity<List<User>> responseEntity = userController.getAllUser();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());
    }

    @Test
    public void testGetUsersByAdminRole_Success() {
        // Mock a list of users with admin role
        List<User> adminUsers = List.of(new User(), new User());
        when(userService.getUsersByAdminRole()).thenReturn(adminUsers);

        // Perform the request
        ResponseEntity<List<User>> responseEntity = userController.getUsersByAdminRole();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(adminUsers, responseEntity.getBody());
    }

    @Test
    public void testGetUsersByUserRole_Success() {
        // Mock a list of users with user role
        List<User> normalUsers = List.of(new User(), new User());
        when(userService.getUsersByUserRole()).thenReturn(normalUsers);

        // Perform the request
        ResponseEntity<List<User>> responseEntity = userController.getUsersByUserRole();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(normalUsers, responseEntity.getBody());
    }

    @Test
    public void testGetUsersByHandymanRole_Success() {
        // Mock a list of users with handyman role
        List<User> handymanUsers = List.of(new User(), new User());
        when(userService.getUsersByHandymanRole()).thenReturn(handymanUsers);

        // Perform the request
        ResponseEntity<List<User>> responseEntity = userController.getUsersByHandymanRole();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(handymanUsers, responseEntity.getBody());
    }

    @Test
    public void testGetUsersByPropertyOwnerRole_Success() {
        // Mock a list of users with property owner role
        List<User> propertyOwnerUsers = List.of(new User(), new User());
        when(userService.getUsersByPropertyOwnerRole()).thenReturn(propertyOwnerUsers);

        // Perform the request
        ResponseEntity<List<User>> responseEntity = userController.getUsersByPropertyOwnerRole();

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(propertyOwnerUsers, responseEntity.getBody());
    }


    @Test
    public void testRateHandyman_Success() throws UserNotFoundException {
        // Mock username and rating
        String username = "handymanUser";
        int rating = 5;

        // Mock userService behavior
        User ratedUser = new User(); // Create a User object as expected response
        when(userService.rateHandyman(username, rating)).thenReturn(ratedUser);

        // Perform the request
        ResponseEntity<User> responseEntity = userController.rateHandyman(username, rating);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ratedUser, responseEntity.getBody());
    }

    @Test
    public void testRateHandyman_InvalidRating() throws UserNotFoundException {
        // Test with an invalid rating
        String username = "handymanUser";
        int invalidRating = 6; // Out of bounds

        // Perform the request
        ResponseEntity<User> responseEntity = userController.rateHandyman(username, invalidRating);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateCardDetails_Success() {
        // Mock request parameters
        String username = "testUser";
        String cardNumber = "4111111111111111";
        String cardExpire = "12/23";

        // Mock userService behavior
        User updatedUser = new User(); // Create a User object as expected response
        when(userService.updateCardDetails(username, cardNumber, cardExpire)).thenReturn(Optional.of(updatedUser));

        // Perform the request
        ResponseEntity<User> responseEntity = userController.updateCardDetails(username, cardNumber, cardExpire);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedUser, responseEntity.getBody());
    }

    @Test
    public void testUpdateCardDetails_UserNotFound() {
        // Mock request parameters
        String username = "testUser";
        String cardNumber = "4111111111111111";
        String cardExpire = "12/23";

        // Mock userService behavior to return an empty Optional
        when(userService.updateCardDetails(username, cardNumber, cardExpire)).thenReturn(Optional.empty());

        // Perform the request
        ResponseEntity<User> responseEntity = userController.updateCardDetails(username, cardNumber, cardExpire);

        // Assert the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
