package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDAOImpl;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class ListAllQuestionService {

    @Autowired
    private UserDAOImpl userDAOImpl;
    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(final String authorizationToken) throws AuthorizationFailedException {
       UserAuthTokenEntity userAuthToken=userDAOImpl.getUserAuthToken(authorizationToken);
        if (userAuthToken == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }
        // return all the list of questions from the question DB
        return questionDao.getAllQuestions();
    }
}