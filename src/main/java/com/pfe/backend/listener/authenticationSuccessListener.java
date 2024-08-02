package com.pfe.backend.listener;

import com.pfe.backend.domain.UserPrincipal;
import com.pfe.backend.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class authenticationSuccessListener {
	
	private  LoginAttemptService loginAttemptService ;
	
	@Autowired
	public authenticationSuccessListener(LoginAttemptService loginAttemptService) {
		this.loginAttemptService = loginAttemptService;
	}
	
	@EventListener
	public void onAuthenticationSuccess (AuthenticationSuccessEvent event ) throws ExecutionException {
		Object principal = event.getAuthentication().getPrincipal();
		if (principal instanceof UserPrincipal) {
			UserPrincipal user = (UserPrincipal ) event.getAuthentication().getPrincipal();
			loginAttemptService.removeUserFromLoginAttemptCache(user.getUsername());
		}
	}

}
