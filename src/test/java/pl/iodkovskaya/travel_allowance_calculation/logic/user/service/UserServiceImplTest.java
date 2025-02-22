package pl.iodkovskaya.travel_allowance_calculation.logic.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserResponseDto;

import java.util.List;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void should_add_user() {
        // GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        UserRequestDto userRequestDto = new UserRequestDto(pesel, firstName, secondName, position);

        // WHEN
        userService.addUser(userRequestDto);

        // THEN
        assertThat(userRepository.findAll()).isNotEmpty();
    }

    @Test
    void should_find_all_users() {
        // GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        UserRequestDto userRequestDto = new UserRequestDto(pesel, firstName, secondName, position);
        userService.addUser(userRequestDto);

        // WHEN
        List<UserResponseDto> allUsers = userService.findAllUser();

        // THEN
        assertThat(allUsers).isNotEmpty();
    }

    @Test
    void should_find_user_by_pesel() {
        // GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        UserRequestDto userRequestDto = new UserRequestDto(pesel, firstName, secondName, position);
        userService.addUser(userRequestDto);

        // WHEN
        UserResponseDto userByPesel = userService.findUserByPesel(pesel);

        // THEN
        assertThat(userByPesel.getPesel()).isEqualTo(pesel);
    }

}