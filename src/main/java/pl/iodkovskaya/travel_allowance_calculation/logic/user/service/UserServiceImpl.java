package pl.iodkovskaya.travel_allowance_calculation.logic.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.exception.UserException;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.mapper.UserMapper;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.entity.UserEntity;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void addUser(final UserRequestDto requestDto) {
        userRepository.save(userMapper.toEntity(requestDto));
    }

    @Override
    public List<UserResponseDto> findAllUser() {
        return userRepository.findAll().stream()
                .map(entity -> userMapper.fromEntity(entity))
                .toList();
    }

    @Override
    public UserResponseDto findUserByPesel(final Long pesel) {
        UserEntity userEntity = userRepository.findByPesel(pesel)
                .orElseThrow(() -> new UserException("Can not find user with this pesel: " + pesel));
        return userMapper.fromEntity(userEntity);
    }
}
