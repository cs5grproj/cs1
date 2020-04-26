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

    /**
     * Method to create question
     *
     * @param authorization  access token of signed in user
     * @param questionEntity question which is to be stored
     * @return returns created question details
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final String authorization, QuestionEntity questionEntity)
            throws AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);

        // Validates user authorization
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validates if the authorized user is logged out
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        questionEntity.setUser(userAuthTokenEntity.getUser());
        return questionDao.createQuestion(questionEntity);
    }

    /**
     * Method to fetch all questions of any user
     *
     * @param authorization access token of signed in user
     * @return returns list of all the questions
     * @throws AuthorizationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(final String authorization) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);

        // Validates user authorization
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validates if the authorized user is logged out
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }
        List<QuestionEntity> questionEntityList = questionDao.getAllQuestions();
        return questionEntityList;
    }

    /**
     * Method to edit the question
     *
     * @param authorization  access token of signed in user
     * @param questionId     question id of question which is to be edited
     * @param questionEntity question entity with content to edit question
     * @return question entity with created question details
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(
            final String authorization, final String questionId, final QuestionEntity questionEntity)
            throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);

        // Validates user authorization
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validates if the authorized user is logged out
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit the question");
        }
        QuestionEntity receivedQuestionEntity = questionDao.questionByUuid(questionId);
        if (receivedQuestionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        //validates the authorization of user to edit the question
        if (receivedQuestionEntity.getUser().getId() == (userAuthTokenEntity.getUser().getId())) {
            receivedQuestionEntity.setContent(questionEntity.getContent());
            questionDao.updateQuestion(receivedQuestionEntity);
            return receivedQuestionEntity;

        } else {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Only the question owner can edit the question");
        }
    }

    /**
     * Method to delete the question
     *
     * @param questionId    question id which is to be deleted
     * @param authorization access token of signed in user
     * @return question entity of deleted question
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String questionId, final String authorization)
            throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);

        // Validates user authorization
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validates if the authorized user is logged out
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to delete a question");
        }
        QuestionEntity createdQuestionEntity = questionDao.questionByUuid(questionId);
        if (createdQuestionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

        //validates the authorization of user to delete the question
        if ((createdQuestionEntity.getUser().getId().equals(userAuthTokenEntity.getUser().getId()))
                || (userAuthTokenEntity.getUser().getRole().equals("admin"))) {
            questionDao.deleteQuestion(createdQuestionEntity);
            return createdQuestionEntity;

        } else {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Only the question owner or admin can delete the question");
        }

    }

    /**
     * Method to fetch all the questions posted by a user
     *
     * @param userId        userId of user whose questions to be retrieved
     * @param authorization access token of signed in user
     * @return returns questions posted by a user
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(final String userId, final String authorization)
            throws AuthorizationFailedException, UserNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = userDAOImpl.getUserAuthToken(authorization);

        // Validates user authorization
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validates if the authorized user is logged out
        if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(
                    "ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
        }
        UserEntity userWithUuid = userDAOImpl.getUserByUuid(userId);

        //Validates the existence of the user with corresponding 'uuid'
        if (userWithUuid == null) {
            throw new UserNotFoundException(
                    "USR-001", "User with entered uuid whose details are to be seen does not exist");
        }
        return questionDao.getQuestionsByUser(userWithUuid);
    }
}