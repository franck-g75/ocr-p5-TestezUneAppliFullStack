package com.openclassrooms.starterjwt.services;

import org.mockito.Mockito;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.openclassrooms.starterjwt.Constantes;
import com.openclassrooms.starterjwt.models.Teacher;

import com.openclassrooms.starterjwt.repository.TeacherRepository;

@SpringBootTest
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService; //bean réel lié

    @MockBean
    private TeacherRepository teacherRepository; //bean mocké

	@Test
	public void shouldReturnAllTeachers() {

        //ARRANGE    GIVEN
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        Teacher first = new Teacher();
        first.setId(1L);
        first.setFirstName(Constantes.STRING_ALICE);
        first.setLastName(Constantes.STRING_TAGLIONI);
        first.setCreatedAt( localDateTimeNow );
        first.setUpdatedAt( localDateTimeNow );
        Teacher second = new Teacher(2L,Constantes.STRING_BADINTER,Constantes.STRING_BOB,localDateTimeNow,localDateTimeNow);
        List<Teacher> mockTeachers = List.of(
            first,second
        );

        Mockito.when(teacherRepository.findAll()).thenReturn(mockTeachers);

        //ACT WHEN
        List<Teacher> result = teacherService.findAll();

        //ASSERT THEN
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(Constantes.STRING_ALICE, result.get(0).getFirstName());
        Assertions.assertEquals(Constantes.STRING_BOB, result.get(1).getFirstName());
        Assertions.assertEquals(localDateTimeNow, result.get(0).getCreatedAt());
        Assertions.assertEquals(localDateTimeNow, result.get(0).getUpdatedAt());
        
        //s'asurer que la méthode findAll a bien été appelée une fois
        Mockito.verify(teacherRepository, times(1)).findAll();

	}

	@Test
	public void shouldFindTeacherById() {
        
        //ARRANGE    GIVEN
        Teacher first = new Teacher();
        first.setId(Constantes.LONG_UN);
        first.setFirstName(Constantes.STRING_ALICE);
        first.setLastName(Constantes.STRING_TAGLIONI);
        
        Mockito.when(teacherRepository.findById(Constantes.LONG_UN)).thenReturn(Optional.of(first));
        
        //ACT WHEN
        Teacher teacher = teacherService.findById(Constantes.LONG_UN);

        //ASSERT THEN
        Assertions.assertNotNull(teacher);
        Assertions.assertEquals(Constantes.LONG_UN, teacher.getId());
        Assertions.assertEquals(Constantes.STRING_ALICE, teacher.getFirstName());
        Assertions.assertEquals(Constantes.STRING_TAGLIONI, teacher.getLastName());


	}
}









