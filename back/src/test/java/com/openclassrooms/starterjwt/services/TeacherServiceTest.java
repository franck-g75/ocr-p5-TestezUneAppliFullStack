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
        first.setFirstName("Alice");
        first.setLastName("TAGLIONI");
        first.setCreatedAt( localDateTimeNow );
        first.setUpdatedAt( localDateTimeNow );
        Teacher second = new Teacher(2L,"BADINTER","Robert",localDateTimeNow,localDateTimeNow);
        List<Teacher> mockTeachers = List.of(
            first,second
        );

        Mockito.when(teacherRepository.findAll()).thenReturn(mockTeachers);

        //ACT WHEN
        List<Teacher> result = teacherService.findAll();

        //ASSERT THEN
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Alice", result.get(0).getFirstName());
        Assertions.assertEquals("Robert", result.get(1).getFirstName());
        Assertions.assertEquals(localDateTimeNow, result.get(0).getCreatedAt());
        Assertions.assertEquals(localDateTimeNow, result.get(0).getUpdatedAt());
        
        //s'asurer que la méthode findAll a bien été appelée une fois
        Mockito.verify(teacherRepository, times(1)).findAll();

	}

	@Test
	public void shouldFindById() {
        
        //ARRANGE    GIVEN
        Teacher first = new Teacher();
        first.setId(1L);
        first.setFirstName("Alice");
        first.setLastName("tortellini");
        
        Mockito.when(teacherRepository.findById(1L)).thenReturn(Optional.of(first));
        
        //ACT WHEN
        Teacher teacher = teacherService.findById(1L);

        //ASSERT THEN
        Assertions.assertNotNull(teacher);
        Assertions.assertEquals(1L, teacher.getId());
        Assertions.assertEquals("Alice", teacher.getFirstName());
        Assertions.assertEquals("tortellini", teacher.getLastName());


	}
}









