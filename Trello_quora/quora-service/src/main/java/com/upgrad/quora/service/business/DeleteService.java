package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAOImpl;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DeleteService {

    @Autowired
    private UserDAOImpl userDAOImpl;

//    @Transactional(propagation = Propagation.REQUIRED)

    /**
     *
     * @param userId
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
//    public String getUserByUuid(final String userId, final String authorization) throws
//            AuthorizationFailedException, UserNotFoundException {
//
//        // Check for existence of user token
//        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);
//        if (userAuthTokenEntity == null) {
//            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
//        }
//
//        if(userAuthTokenEntity.getLogoutAt() != null) {
//            throw new AuthorizationFailedException("ATHR-002", "User is signed out.");
//        }
//
//        // Check for user existence in the database
//        UserEntity userEntity = userDAOImpl.getUserByUuid(userId);
//        if (userEntity == null) {
//            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
//        }
//        return userEntity.getUuid();
//    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteUser(final String userUuid, final String authorizationToken)
            throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorizationToken);

        // Validate existence of user in the database
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validate user sign out
        if (userAuthTokenEntity.getLogoutAt() ==  null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }

        // Validate role of the user is 'nonadmin'
        if (userAuthTokenEntity.getUser().getRole().equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }
        UserEntity userEntityToDelete = userDAOImpl.getUserByUuid(userUuid);

        // Check if user to be deleted is present in repository
        if (userEntityToDelete == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        } else {
            userDAOImpl.deleteUser(userUuid);
            return userUuid;
        }
    }
}
