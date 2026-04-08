package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;


@SpringBootTest
public class UserServiceTest {

	@Autowired
    private UserService userService; //bean réel lié (service à tester)

    @MockBean
    private UserRepository userRepository; //bean mocké

	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldDeleteUser() {
    
		//ARRANGE    GIVEN
		Long userId = 2L;

		//ACT WHEN
		userService.delete(userId);

		//ASSERT VERIFY
		Mockito.verify(userRepository, times(1)).deleteById(userId);

	}

	@Test
	public void shouldFindById() {
    
		//ARRANGE    GIVEN
        User userMock = new User();
        userMock.setId(1L);
        userMock.setEmail("alice.tortellini@truc.com");
		userMock.setFirstName("Alice");
        userMock.setLastName("tortellini");
        userMock.setPassword("pwdpwd");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(userMock));
        
        //ACT WHEN
        User user = userService.findById(1L);

        //ASSERT THEN
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("Alice", user.getFirstName());
        Assertions.assertEquals("tortellini", user.getLastName());
		Assertions.assertEquals("alice.tortellini@truc.com", user.getEmail());
		Assertions.assertEquals("pwdpwd", user.getPassword());
	}

}
