package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAOImpl;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

  @Autowired private UserDAOImpl userDAOImpl;

  /**
   * Fetch user record from the User schema and throw appropriate exceptions
   *
   * @param userId - Accept userId as argument
   * @param authorization - Accept authentication token as argument
   * @return - returns object of type UserEntity
   * @throws AuthorizationFailedException
   * @throws UserNotFoundException
   */
  public UserEntity getUserByUuid(final String userId, final String authorization)
      throws AuthorizationFailedException, UserNotFoundException {

    // Validate user token
    UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);

    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }

    // Validate user logout
    if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR-002", "User is signed out.Sign in first to get user details");
    }

    // Validate existence of UUID
    UserEntity userEntity = userDAOImpl.getUserByUuid(userId);
    if (userEntity == null) {
      throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
    }
    return userEntity;
  }

  /**
   * This method is to authorize the user by validating the user token and whether the user is still logged in.
   *
   * @param authorization - Accept authentication token as argument
   * @return - Return UserAuthTokenEntity object
   * @throws AuthorizationFailedException
   */
  public UserAuthTokenEntity getUserAuthorizationStatus(final String authorization)
      throws AuthorizationFailedException {

    UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);

    if (userAuthTokenEntity == null) {
      throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
    }

    if (userAuthTokenEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(
          "ATHR-002", "User is signed out.Sign in first to get user details");
    }
    return userAuthTokenEntity;
  }
}
