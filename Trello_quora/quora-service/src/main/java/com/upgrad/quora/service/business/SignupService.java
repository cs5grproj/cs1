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
   * @param userEntity
   * @return
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
