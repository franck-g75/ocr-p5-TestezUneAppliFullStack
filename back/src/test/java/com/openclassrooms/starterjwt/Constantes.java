package com.openclassrooms.starterjwt;

import java.time.LocalDateTime;
import java.util.List;

import com.openclassrooms.starterjwt.models.User;

public class Constantes{

    public static final LocalDateTime localDateTime = LocalDateTime.of(2026, 04, 11, 11, 30, 15);

    public static final Long LONG_UN = 1L;
    public static final Long LONG_DEUX = 2L;
    public static final Long LONG_TROIS = 3L;
    public static final Long LONG_QUATRE = 4L;

    public static final List<Long> EMPTY_LONG_LIST = List.of();
    public static final List<User> EMPTY_USER_LIST = List.of();

    public static final String STRING_PILATE = "pilate";
    public static final String STRING_VENEZ = "Venez nombreux à mon cours de pilates...";    

    public static final String STRING_ALICE = "Alice";
    public static final String STRING_TAGLIONI = "TAGLIONI";
    public static final String STRING_BOB = "Bob";
    public static final String STRING_BADINTER = "BADINTER"; 
    public static final String STRING_FRANCK = "Franck";
    public static final String STRING_GUINDEUIL= "GUINDEUIL"; 
    
    public static final String STRING_EMAIL_YOGA = "yoga@studio.com";    

    public static final String STRING_PWD_CRYPTE = "$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq";
    public static final String STRING_PWD_NOT_CRYPTE = "test!1234";

}