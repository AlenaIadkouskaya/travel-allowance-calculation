package pl.iodkovskaya.travel_allowance_calculation.logic.user.service;

import pl.iodkovskaya.travel_allowance_calculation.logic.user.model.entity.UserEntity;

public interface UserReaderService {
    UserEntity findUserByPesel(Long pesel);
}
