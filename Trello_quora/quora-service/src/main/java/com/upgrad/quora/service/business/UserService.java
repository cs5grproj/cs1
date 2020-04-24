package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAOImpl;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  @Autowired private UserDAOImpl userDAOImpl;

  @Autowired private PasswordCryptographyProvider cryptographyProvider;

  /**
   * @param userEntity
   * @return
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public UserEntity signUpUser(final UserEntity userEntity) {
    String password = userEntity.getPassword();
    if (password == null) {
      userEntity.setPassword("quora@123");
    }
    String[] encryptedText = cryptographyProvider.encrypt(userEntity.getPassword());
    userEntity.setSalt(encryptedText[0]);
    userEntity.setPassword(encryptedText[1]);
    return userDAOImpl.signUpUser(userEntity);
  }
}
