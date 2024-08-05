package com.pfe.backend.service;

import com.pfe.backend.domain.User;
import com.pfe.backend.exception.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    User register(String firstName, String lastName, String username, String email , long phoneNumber , String city) throws UserNotFoundException, UsernameExistException, EmailExistException;

    List<User> getUsers();

    User findUserByUsername(String username);



    User findUserByEmail(String email);
    
    User addNewUser (String firstName , String lastName , String username , String email ,String role ,long phoneNumber, String city, MultipartFile profileImage ) throws UsernameExistException, EmailExistException, IOException , NotAnImageFileException;

	User updateUser ( String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNotLoked  , boolean isActive , MultipartFile profileImage,long phoneNumber,String city ) throws NotAnImageFileException ,UsernameExistException, EmailExistException, IOException ;

	void deleteUser (String usename ) throws IOException ;
	 List<String> getEmails();

		void resetPassword (String email ) throws EmailNotFoundException ;
	                                                                            
	User updateProfileImage (String username , MultipartFile profileImage) throws UsernameExistException, EmailExistException, IOException , NotAnImageFileException ;

	long getTotalUsers();

	long getActiveUsers();

	long getInactiveUsers();

	long getNotLockedUsers();

	long getLockedUsers();

	
    
}
