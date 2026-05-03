package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
public class SessionServiceUnitTest {

	@Mock 
	private SessionRepository sessionRepository;  //classe mockée
 
	@Mock 
	private UserRepository userRepository;  //classe mockée

	@InjectMocks
	private SessionService sessionService;    //classe testée


    @Test
	public void create_shouldPersistSession() {
		
		//ARRANGE GIVEN
		Long id = 1L;
		LocalDateTime localDateTimeNow = LocalDateTime.now();
		Teacher teacher = new Teacher(1L,"BADINTER","Robert",localDateTimeNow,localDateTimeNow);
		Session input = new Session();
		Session saved = new Session(id,"nom du cours",
									new GregorianCalendar(2025,12,27).getTime(),
									"description du cours",teacher,null,localDateTimeNow,localDateTimeNow);

		input.setId(id);
		input.setDate(new GregorianCalendar(2025,12,27).getTime());
		input.setDescription("description du cours");
		input.setName("nom du cours");
		input.setCreatedAt(localDateTimeNow);
		input.setUpdatedAt(localDateTimeNow);
		input.setTeacher(teacher);

		Mockito.when(sessionRepository.save(any(Session.class))).thenReturn(saved);
		
		
		//ACT WHEN
		Session session = sessionService.create(input);


		//ASSERT VERIFY
		assertNotNull(session);
		Assertions.assertEquals(saved.getId(), session.getId());
		Assertions.assertEquals(saved.getDate(), session.getDate());
		Assertions.assertEquals(saved.getDescription(), session.getDescription());
		Assertions.assertEquals(saved.getName(),session.getName());
		Assertions.assertEquals(saved.getCreatedAt(), session.getCreatedAt());
        Assertions.assertEquals(saved.getUpdatedAt(), session.getUpdatedAt());
		Assertions.assertEquals(saved.getTeacher(), session.getTeacher());
        
	}

	@Test
	public void delete_shouldCallSessionRepositoryDeleteById() {
		//ARRANGE    GIVEN
		Long sessionId = 2L;

		//ACT WHEN
		sessionService.delete(sessionId);

		//ASSERT VERIFY
		Mockito.verify(sessionRepository, times(1)).deleteById(sessionId);

	}

	@Test
	public void findAll_shouldFindEverySessions() {
		//ARRANGE    GIVEN
        Session first = new Session();
        first.setId(1L);
        first.setName("Pilate");
        first.setDescription("Pilate descriptions");
		first.setDate(new GregorianCalendar(2026,4,11).getTime());

        Session second = new Session();
        second.setId(2L);
        second.setName("Reflexo");
        second.setDescription("Reflexo description");
		second.setDate(new GregorianCalendar(2025,12,27).getTime());
        List<Session> mockSessions = List.of(
            first,second
        );

        Mockito.when(sessionRepository.findAll()).thenReturn(mockSessions);

        //ACT WHEN
        List<Session> result = sessionService.findAll();

        //ASSERT THEN
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Reflexo", result.get(1).getName());
        Assertions.assertEquals("Reflexo description", result.get(1).getDescription());
		Assertions.assertEquals(new GregorianCalendar(2025,12,27).getTime(), result.get(1).getDate());
        //s'asurer que la méthode findAll a bien été appelée une fois
        Mockito.verify(sessionRepository, times(1)).findAll();

    }

	@Test
	public void getById_shouldFindSession() {
		//ARRANGE    GIVEN
        Session sessionMock = new Session();
        sessionMock.setId(1L);
        sessionMock.setName("Nom session");
		sessionMock.setDescription("Description de la session");
        sessionMock.setDate(new GregorianCalendar(2025,12,27).getTime());

        Mockito.when(sessionRepository.findById(1L)).thenReturn(Optional.of(sessionMock));
        
        //ACT WHEN
        Session session = sessionService.getById(1L);

        //ASSERT THEN
        Assertions.assertNotNull(session);
        Assertions.assertEquals(1L, session.getId());
        Assertions.assertEquals("Nom session", session.getName());
        Assertions.assertEquals("Description de la session", session.getDescription());
		Assertions.assertEquals(new GregorianCalendar(2025,12,27).getTime(), session.getDate());

	}

	@Test
	public void update_shouldPersistSession() {
		
		//ARRANGE GIVEN
		Long id = 1L;
		Session input = new Session();
		Session saved = new Session();

		input.setId(id);
		input.setDate(new GregorianCalendar(2025,12,27).getTime());
		input.setDescription("description du cours");
		input.setName("nom du cours");

		saved.setId(id);
		saved.setDate(new GregorianCalendar(2025,12,27).getTime());
		saved.setDescription("description du cours");
		saved.setName("nom du cours");
		
		Mockito.when(sessionRepository.save(input)).thenReturn(saved);

		//ACT WHEN
		Session session = sessionService.update(id, input);
		
		//ASSERT VERIFY
		assertNotNull(session);
		Assertions.assertEquals(saved.getId(), session.getId());
		Assertions.assertEquals(saved.getDate(), session.getDate());
		Assertions.assertEquals(saved.getDescription(), session.getDescription());
		Assertions.assertEquals(saved.getName(),session.getName());

	}

	@Test
	public void participate_participationShouldReturnNotFoundExeption() {
		
		//ARRANGE GIVEN
		Long id = 1L;

		Session session = new Session();
		session.setId(1L);
		session.setDate(new GregorianCalendar(2025,12,27).getTime());
		session.setDescription("description du cours");
		session.setName("nom du cours");
		
		User user = new User();
		user.setId(id);
		user.setFirstName("toi");
		user.setLastName("et moi");

		Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

		//ACT WHEN & ASSERT VERIFY
		Assertions.assertThrows( NotFoundException.class , () -> sessionService.participate(id,null));
		Assertions.assertThrows( NotFoundException.class , () -> sessionService.participate(null,id));
		Assertions.assertThrows( NotFoundException.class , () -> sessionService.participate(null,null));

	}

	@Test
	public void participate_participationShouldReturnBadRequestExeption() {
		
		//ARRANGE GIVEN
		Long id = 1L;

		Session session = new Session();
		session.setId(1L);
		session.setDate(new GregorianCalendar(2025,12,27).getTime());
		session.setDescription("description du cours");
		session.setName("nom du cours");
		List<User> lu = new ArrayList<User>();
		session.setUsers(lu);
		
		User user = new User();
		user.setId(id);
		user.setFirstName("toi");
		user.setLastName("et moi");

		Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

		//ACT WHEN & ASSERT VERIFY
		sessionService.participate(id,id);
		Assertions.assertThrows( BadRequestException.class , () -> sessionService.participate(id,id));

	}

    @Test
	public void participate_participationShouldBeOk() {
		
		//ARRANGE GIVEN
		Long id = 1L;

		Session session = new Session();
		session.setId(1L);
		session.setDate(new GregorianCalendar(2025,12,27).getTime());
		session.setDescription("description du cours");
		session.setName("nom du cours");
		List<User> lu = new ArrayList<User>();
		session.setUsers(lu);

		User user = new User();
		user.setId(id);
		user.setFirstName("toi");
		user.setLastName("et moi");

		Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

		//ACT WHEN
		sessionService.participate(1L,1L);
		
		//ASSERT VERIFY
		assertNotNull(session);
		Assertions.assertEquals(session.getId(), 1L);
		Assertions.assertEquals(session.getDate(), new GregorianCalendar(2025,12,27).getTime());
		Assertions.assertEquals(session.getDescription(), "description du cours");
		Assertions.assertEquals(session.getName(),"nom du cours");
		Assertions.assertEquals(session.getUsers().size(), 1);

	}








	@Test
	public void noLongerParticipate_shouldSendBadRequest_WhenNoParticipation() {

		//ARRANGE GIVEN
		Long id = 1L;

		Session session = new Session();
		session.setId(id);
		session.setDate(new GregorianCalendar(2025,12,28).getTime());
		session.setDescription("description du cours");
		session.setName("nom du cours");
		List<User> lu = new ArrayList<User>();
		session.setUsers(lu);

		User user = new User();
		user.setId(id);
		user.setFirstName("toi");
		user.setLastName("et moi");

		Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
		
		//Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

		//ACT WHEN & ASSERT VERIFY
		Assertions.assertThrows( BadRequestException.class , () -> sessionService.noLongerParticipate(id,id));
	}
	

	@Test
	public void noLongerParticipate_shouldSendNotFound_WhenSessionNotFound() {

		//ARRANGE GIVEN
		Long id1 = 1L;
		Long id2 = 2L;
		
		Session session = new Session();
		session.setId(id1);
		session.setDate(new GregorianCalendar(2025,12,28).getTime());
		session.setDescription("description du cours");
		session.setName("nom du cours");
		List<User> lu = new ArrayList<User>();
		session.setUsers(lu);
		
		Mockito.when(sessionRepository.findById(id1)).thenReturn(Optional.of(session));

		//ACT WHEN & ASSERT VERIFY
		Assertions.assertThrows( NotFoundException.class , () -> sessionService.noLongerParticipate(id2,id1));
	}

	@Test
	public void noLongerParticipate_shouldBeOk() {

		//ARRANGE GIVEN
		Long id = 1L;
		
		
		Session session = new Session();
		session.setId(id);
		session.setDate(new GregorianCalendar(2025,12,28).getTime());
		session.setDescription("description du cours");
		session.setName("nom du cours");		

		User user = new User();
		user.setId(id);
		user.setFirstName("toi");
		user.setLastName("et moi");

		List<User> lu = new ArrayList<User>();
		lu.add(user);
		session.setUsers(lu);

		Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(session));

		//ACT WHEN 
		sessionService.noLongerParticipate(id,id);

		//ASSERT VERIFY
		Assertions.assertTrue( session.getUsers().size()==0 );

		
	}
}
