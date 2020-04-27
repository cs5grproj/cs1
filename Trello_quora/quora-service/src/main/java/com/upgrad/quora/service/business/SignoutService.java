package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAOImpl;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SignoutService {

  @Autowired private UserDAOImpl userDAOImpl;

  /**
   * Method to sign-out a user. If user is already signed-in, the logout_at field in the user_auth schema is updated with the logout time else throws an exception
   *
   * @param authorizationToken - Accept authorization token as an argument
   * @return - returns obect of type UserAuthTokenEntity
   * @throws SignOutRestrictedException
   **/
  @Transactional(propagation = Propagation.REQUIRED)
  public UserAuthTokenEntity signOut(final String authorizationToken)
      throws SignOutRestrictedException {
    UserAuthTokenEntity userAuthToken = userDAOImpl.getUserAuthToken(authorizationToken);
    if (userAuthToken != null) {
      final ZonedDateTime now = ZonedDateTime.now();
      userAuthToken.setLogoutAt(now);
      userAuthToken = userDAOImpl.userSignOut(userAuthToken);
    } else {
      throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
    }
    return userAuthToken;
  }
}
