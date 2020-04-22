package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDAO;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AnswerService {
    @Autowired
    private AnswerDAO answerDao;
    @Autowired
    private CommonService commonService;
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final String questionId, final String authorizationToken, final AnswerEntity answerEntity) throws AuthorizationFailedException, InvalidQuestionException {
        //Checking the user is authorized and if authorized user is logged in.
        //Validate User Authorization status is implemented as a common function in Common Service
        UserAuthTokenEntity userAuthTokenEntity = commonService.getUserAuthorizationStatus(authorizationToken);
        answerEntity.setUser(userAuthTokenEntity.getUser());
        QuestionEntity questionEntity = answerDao.getQuestion(questionId);
        if(questionEntity == null){
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }
        answerEntity.setQuestion_id(questionEntity);
        answerEntity.setDate(ZonedDateTime.now());
        return answerDao.createAnswer(answerEntity);
    }
}

