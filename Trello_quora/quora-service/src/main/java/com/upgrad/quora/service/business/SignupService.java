package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAOImpl;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {

  @Autowired private UserDAOImpl userDAOImpl;

  @Autowired private UserService userService;

  /**
   * This method validates existence of a user in the User schema and either signs up a user or throws appropriate exceptions
   *
   * @param userEntity - Accept object of type UserEntity as an argument
   * @return - returns userEntity object
   * @throws SignUpRestrictedException
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity signUpUser(UserEntity userEntity) throws SignUpRestrictedException {
    if (userDAOImpl.getUserByName(userEntity.getUserName()) != null) {
      throw new SignUpRestrictedException(
          "SGR-001", "Try any other Username, this Username has already been taken");
    } else if (userDAOImpl.getUserByEmail(userEntity.getEmail()) != null) {
      throw new SignUpRestrictedException(
          "SGR-002", "This user has already been registered, try with any other emailId");
    }
    return userService.signUpUser(userEntity);
  }
}
