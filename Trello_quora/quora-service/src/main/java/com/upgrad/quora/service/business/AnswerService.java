package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final String questionId, final String authorizationToken, final AnswerEntity answerEntity) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = answerDao.getLogInUserAuthToken(authorizationToken);
        if (userAuthTokenEntity == null) {
            userAuthTokenEntity = answerDao.getUserAuthToken(authorizationToken);
            if (userAuthTokenEntity == null) {
                throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
            } else {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
            }
        }
        QuestionEntity questionEntity = answerDao.getQuestion(questionId);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        answerEntity.setQuestion(questionEntity);
        answerEntity.setUser(userAuthTokenEntity.getUser());
        return answerDao.createAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(final String answerId, final String authorizationToken, final AnswerEntity answerEntity) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthTokenEntity userAuthTokenEntity = answerDao.getLogInUserAuthToken(authorizationToken);
        if (userAuthTokenEntity == null) {
            userAuthTokenEntity = answerDao.getUserAuthToken(authorizationToken);
            if (userAuthTokenEntity == null) {
                throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
            } else {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
            }
        }

        AnswerEntity existingAnswerEntity = answerDao.getAnswerById(answerId);
        if(existingAnswerEntity == null){
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        if(existingAnswerEntity.getUser().getId() == userAuthTokenEntity.getUser().getId()){
               answerEntity.setQuestion(existingAnswerEntity.getQuestion());
               answerEntity.setUser(userAuthTokenEntity.getUser());
               answerEntity.setUuid(existingAnswerEntity.getUuid());
                answerEntity.setDate(ZonedDateTime.now());
                return answerDao.updateAnswer(answerEntity);
           }
       else {
           throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswers(final String authorizationToken, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = answerDao.getLogInUserAuthToken(authorizationToken);
        if (userAuthTokenEntity == null) {
            userAuthTokenEntity = answerDao.getUserAuthToken(authorizationToken);
            if (userAuthTokenEntity == null) {
                throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
            } else {
                throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
            }
        }
        QuestionEntity questionEntity = answerDao.getQuestion(questionId);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001","The question with entered uuid whose details are to be seen does not exist");
        }
        return answerDao.getAnswerByQuestionId(questionEntity);
    }
}
