package com.openclassrooms.starterjwt.security;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest
public class UserDetailServiceImplTest {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;    //bean testé

    @MockBean
	private UserRepository userRepository;  //bean mockée

    @Test
	public void contextLoads() {
	}

    @Test
	public void loadUserByUserName_ShouldUserNameNotFound() {

		//ARRANGE   GIVEN
        User userMock = new User();
        userMock.setId(1L);
        userMock.setEmail("alice.tortellini@truc.com");
		userMock.setFirstName("Alice");
        userMock.setLastName("tortellini");
        userMock.setPassword("pwdpwd");
        userMock.setAdmin(false);

        Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));

        //ACT WHEN & ASSERT VERIFY
        Assertions.assertThrows( UsernameNotFoundException.class , 
            () -> userDetailsServiceImpl.loadUserByUsername("NonExistentUser"));

    }


    @Test
	public void loadUserByUserName_ShouldBeOk() {

		//ARRANGE   GIVEN
        User userMock = new User();
        userMock.setId(1L);
        userMock.setEmail("alice.tortellini@truc.com");
		userMock.setFirstName("Alice");
        userMock.setLastName("tortellini");
        userMock.setPassword("pwdpwd");
        userMock.setAdmin(false);
        
        Mockito.when(userRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userMock));
        
        //ACT WHEN 
        UserDetails user = userDetailsServiceImpl.loadUserByUsername(userMock.getEmail());

        //ASSERT VERIFY        
        Assertions.assertNotNull(user);
        Assertions.assertEquals("alice.tortellini@truc.com", user.getUsername());
		Assertions.assertEquals("pwdpwd", user.getPassword());
    }

}
