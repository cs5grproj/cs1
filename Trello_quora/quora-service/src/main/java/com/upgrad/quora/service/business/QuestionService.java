package com.upgrad.quora.service.business;


import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDAOImpl;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserDAOImpl userDAOImpl;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private CommonService commonService;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final String authorization, QuestionEntity questionEntity) throws AuthorizationFailedException {
        // Validate user token

        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }
        questionEntity.setUser(userAuthTokenEntity.getUser());
        return questionDao.createQuestion(questionEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(final String authorization, final String questionId, final QuestionEntity questionEntity) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }
        QuestionEntity receivedQuestionEntity = questionDao.getQuestionByUuid(questionId);
        if (receivedQuestionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (receivedQuestionEntity.getUser().getId() == (userAuthTokenEntity.getUser().getId())) {
            receivedQuestionEntity.setContent(questionEntity.getContent());
            questionDao.updateQuestion(receivedQuestionEntity);
            return receivedQuestionEntity;

        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String questionId, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        // Validate existence of user in the database
        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);
        QuestionEntity createdQuestionEntity = questionDao.getQuestionByUuid(questionId);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }
        if (createdQuestionEntity != null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if ((createdQuestionEntity.getUser().getId() == (userAuthTokenEntity.getUser().getId())) || (userAuthTokenEntity.getUser().getRole().equals("admin"))) {
            questionDao.deleteQuestion(createdQuestionEntity);
            return createdQuestionEntity;
        } else {
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(final String userId, final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }
        UserEntity userWithUuid = userDAOImpl.getUserByUuid(userId);
        if (userWithUuid == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid whose details are to be seen does not exist");
        }
        return questionDao.getQuestionsByUser(userWithUuid);

    }
}