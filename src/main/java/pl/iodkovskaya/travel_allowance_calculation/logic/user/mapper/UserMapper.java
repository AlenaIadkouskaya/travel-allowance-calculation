package pl.iodkovskaya.travel_allowance_calculation.logic.user.mapper;

import org.springframework.stereotype.Component;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserRequestDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.dto.UserResponseDto;
import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.entity.UserEntity;

@Component
public class UserMapper {
    public UserEntity toEntity(UserRequestDto requestDto) {
        return new UserEntity(requestDto.getPesel(), requestDto.getFirstName(),
                requestDto.getSecondName(), requestDto.getPosition());
    }

    public UserResponseDto fromEntity(UserEntity entity) {
        return new UserResponseDto(entity.getPesel(), entity.getFirstName(),
                entity.getSecondName(), entity.getPosition());
    }
}
