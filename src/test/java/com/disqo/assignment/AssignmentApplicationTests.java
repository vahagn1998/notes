package com.disqo.assignment;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = AssignmentApplication.class)
@ExtendWith(MockitoExtension.class)
public class AssignmentApplicationTests {

}
