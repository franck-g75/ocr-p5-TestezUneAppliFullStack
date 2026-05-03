package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassrooms.starterjwt.Constantes;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;


@SpringBootTest
public class UserServiceUnitTest {

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
        userMock.setId(Constantes.LONG_UN);
        userMock.setEmail(Constantes.STRING_EMAIL_YOGA);
		userMock.setFirstName(Constantes.STRING_ALICE);
        userMock.setLastName(Constantes.STRING_TAGLIONI);
        userMock.setPassword(Constantes.STRING_PWD_NOT_CRYPTE);
        Mockito.when(userRepository.findById(Constantes.LONG_UN)).thenReturn(Optional.of(userMock));
        
        //ACT WHEN
        User user = userService.findById(Constantes.LONG_UN);

        //ASSERT THEN
        Assertions.assertNotNull(user);
        Assertions.assertEquals(Constantes.LONG_UN, user.getId());
        Assertions.assertEquals(Constantes.STRING_ALICE, user.getFirstName());
        Assertions.assertEquals(Constantes.STRING_TAGLIONI, user.getLastName());
		Assertions.assertEquals(Constantes.STRING_EMAIL_YOGA, user.getEmail());
		Assertions.assertEquals(Constantes.STRING_PWD_NOT_CRYPTE, user.getPassword());
	}

}
