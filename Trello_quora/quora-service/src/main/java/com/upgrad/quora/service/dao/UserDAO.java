package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;

public interface UserDAO {
    public UserEntity signUpUser(UserEntity userEntity);

    public UserEntity getUserByName(final String userName);

    public UserEntity getUserByEmail(final String email);

    public UserEntity getUserByUuid(final String userUuid);

    public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity);

    public void updateUser(final UserEntity updatedUserEntity);

    public UserEntity authenticateUser(final String userName, final String password);

    public UserAuthTokenEntity getUserAuthToken(final String accessToken);

    public UserAuthTokenEntity userSignOut(final UserAuthTokenEntity userAuthTokenEntity);

}