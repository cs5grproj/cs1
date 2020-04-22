package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDAO;
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
    private AnswerDAO answerDao;
    @Autowired
    private CommonService commonService;
    @Transactional(propagation = Propagation.REQUIRED)
    //Create Answer
    public AnswerEntity createAnswer(final String questionId, final String authorizationToken, final AnswerEntity answerEntity) throws AuthorizationFailedException, InvalidQuestionException {
        //Checking the user is authorized and if authorized user is logged in.
        //Validate User Authorization status is implemented as a common function in Common Service
        UserAuthTokenEntity userAuthTokenEntity = commonService.getUserAuthorizationStatus(authorizationToken);
        answerEntity.setUser(userAuthTokenEntity.getUser());
        QuestionEntity questionEntity = answerDao.getQuestion(questionId);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        answerEntity.setQuestion(questionEntity);
        answerEntity.setDate(ZonedDateTime.now());
        return answerDao.createAnswer(answerEntity);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    //Edit Answer
    public AnswerEntity editAnswer(final String answerId, final String authorizationToken, final AnswerEntity answerEntity) throws AuthorizationFailedException, AnswerNotFoundException {
        //Checking the user is authorized and if authorized user is logged in.
        //Validate User Authorization status is implemented as a common function in Common Service
        UserAuthTokenEntity userAuthTokenEntity = commonService.getUserAuthorizationStatus(authorizationToken);
        AnswerEntity existingAnswerEntity = answerDao.getAnswerById(answerId);
        if(existingAnswerEntity == null){
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        if(existingAnswerEntity.getUser().getId() == userAuthTokenEntity.getUser().getId()){
            existingAnswerEntity.setAns(answerEntity.getAns());
            existingAnswerEntity.setDate(ZonedDateTime.now());
            return answerDao.updateAnswer(existingAnswerEntity);
        }
        else {
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    //Delete Answer
    public AnswerEntity deleteAnswer(final String answerId, final String authorizationToken) throws AuthorizationFailedException, AnswerNotFoundException {
        //Checking the user is authorized and if authorized user is logged in.
        //Validate User Authorization status is implemented as a common function in Common Service
        UserAuthTokenEntity userAuthTokenEntity = commonService.getUserAuthorizationStatus(authorizationToken);
        AnswerEntity existingAnswerEntity = answerDao.getAnswerById(answerId);
        if(existingAnswerEntity == null){
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        if((existingAnswerEntity.getUser().getId() == userAuthTokenEntity.getUser().getId()) || userAuthTokenEntity.getUser().getRole().equals("admin")){
            answerDao.deleteAnswer(existingAnswerEntity);
            return existingAnswerEntity;
        }
        else {
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }
    }

    //Get All Answers
    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswers(final String questionId, final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = commonService.getUserAuthorizationStatus(authorizationToken);
        QuestionEntity questionEntity = answerDao.getQuestion(questionId);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001","The question with entered uuid whose details are to be seen does not exist");
        }
        return answerDao.getAnswerByQuestionId(questionEntity);
    }
}

