package com.openclassrooms.starterjwt.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.openclassrooms.starterjwt.models.User;

/*
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
         scripts = "classpath:../integration_test_before.sql",
         config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
         scripts = "classpath:../integration_test_after.sql",
         config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
})
*/
//@TestContainers
@SpringBootTest
//@DataJpaTest     //@ActiveProfiles("test")
//N@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTest {

	@Autowired
    private UserRepository userRepository; //bean réel bean testé

    @Test
	public void findByEmail() {//renvoie un user

		//ARRANGE   GIVEN
		Long id = 1L;
		User userMock = new User();
        userMock.setId(id);
        userMock.setEmail("alice.tortellini@truc.com");
		userMock.setFirstName("Alice");
        userMock.setLastName("tortellini");
        userMock.setPassword("pwdpwd");
		userRepository.saveAndFlush(userMock);  //quelle BDD est utilisée? dans la BDD de prod... (update)

		//ACT WHEN
		Optional<User> userFound = userRepository.findByEmail("alice.tortellini@truc.com");

		//ASSERT VERIFY
		assertNotNull(userFound);
		Assertions.assertEquals(id, userFound.get().getId());
        Assertions.assertEquals("Alice", userFound.get().getFirstName());
        Assertions.assertEquals("tortellini", userFound.get().getLastName());
		Assertions.assertEquals("alice.tortellini@truc.com", userFound.get().getEmail());
		Assertions.assertEquals("pwdpwd", userFound.get().getPassword());

	}

    @Test
	public void existsByEmail() {//renvoie un boolean
		
		//ARRANGE   GIVEN
		Long id = 1L;
		User userMock = new User();
        userMock.setId(id);
        userMock.setEmail("alice.tortellini@truc.com");
		userMock.setFirstName("Alice");
        userMock.setLastName("tortellini");
        userMock.setPassword("pwdpwd");
		userRepository.saveAndFlush(userMock);   //où est ce sauvegardé ? dans la BDD de prod...

 		//ACT WHEN
		Boolean userFound = userRepository.existsByEmail("alice.tortellini@truc.com");

		//ASSERT VERIFY
		assertNotNull(userFound);
		Assertions.assertEquals(userFound,true);

	}
/*
	@Test
	public void shouldDetect2Email(){
		
		//ARRANGE   GIVEN
		Long id1 = 12L;
		Long id2 = 13L;
		User userMock1 = new User();
        //userMock1.setId(id1);
        userMock1.setEmail("alice.tortellini@truc.com");
		userMock1.setFirstName("Alice");
        userMock1.setLastName("tortellini");
        userMock1.setPassword("pwdpwd1");

		User userMock2 = new User();
        //userMock2.setId(id2);
        userMock2.setEmail("alice.tortellini@truc.com");
		userMock2.setFirstName("paul");
        userMock2.setLastName("truman");
        userMock2.setPassword("pwdpwd2");

		//ACT WHEN
		userRepository.saveAndFlush(userMock1);   //où est ce sauvegardé ?
		userRepository.saveAndFlush(userMock2);   //où est ce sauvegardé ?

		Assertions.assertThrows( IncorrectResultSizeDataAccessException.class , 
            () -> userRepository.saveAndFlush(userMock2));

	}
*/
}