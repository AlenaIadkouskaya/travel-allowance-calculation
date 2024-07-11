package pl.iodkovskaya.travel_allowance_calculation.logic.user.service;

import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    void addUser(UserRequestDto requestDto);

    List<UserResponseDto> findAllUser();

    UserResponseDto findUserByPesel(Long pesel);
}
