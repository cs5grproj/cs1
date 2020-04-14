package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class SignoutBusinessService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity signOut(final String authorizationToken) throws SignOutRestrictedException {
        UserAuthTokenEntity userAuthToken = null;
        userAuthToken = userDao.getUserAuthToken(authorizationToken);
        if (userAuthToken != null) {
            final ZonedDateTime now = ZonedDateTime.now();
            userAuthToken.setLogoutAt(now);
            userAuthToken = userDao.userSignOut(userAuthToken);
        } else {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed inp-p");
        }
        return userAuthToken;
    }
}
