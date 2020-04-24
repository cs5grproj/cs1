package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAOImpl;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SigninService {

  @Autowired private UserDAOImpl userDAOImpl;

  @Autowired private PasswordCryptographyProvider passwordCryptographyProvider;

  /**
   * Method to authenticate user provided username/password and return an access token
   *
   * @param userName - Username as provided by user
   * @param password - Password as provided by user
   * @return
   * @throws AuthenticationFailedException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public UserAuthTokenEntity authenticate(final String userName, final String password)
      throws AuthenticationFailedException {
    UserEntity userEntity = userDAOImpl.getUserByName(userName);

    if (userEntity == null) {
      throw new AuthenticationFailedException("ATH-001", "This username does not exist");
    }

    final String encryptedPassword =
        passwordCryptographyProvider.encrypt(password, userEntity.getSalt());

    userEntity = userDAOImpl.authenticateUser(userName, encryptedPassword);

    if (userEntity != null) {
      UserAuthTokenEntity userAuthToken = new UserAuthTokenEntity();
      userAuthToken.setUser(userEntity);
      final ZonedDateTime now = ZonedDateTime.now();
      final ZonedDateTime expiresAt = now.plusHours(8);
      String uuid = userEntity.getUuid();
      JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);

      userAuthToken.setAccessToken(jwtTokenProvider.generateToken(uuid, now, expiresAt));
      userAuthToken.setLoginAt(now);
      userAuthToken.setExpiresAt(expiresAt);
      userAuthToken.setUuid(uuid);
      userDAOImpl.createAuthToken(userAuthToken);

      return userAuthToken;
    } else {
      throw new AuthenticationFailedException("ATH-002", "Password Failed");
    }
  }
}
