package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.service.business.SigninBusinessService;
import com.upgrad.quora.service.business.SignoutBusinessService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.business.CreateQuestionService;
import com.upgrad.quora.service.business.ListAllQuestionService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController{
    @RequestMapping(method = RequestMethod.POST, path = "/question/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization,final QuestionRequest questionRequest) throws AuthorizationFailedException {
        String bearerToken = null;
        QuestionResponse questionResponse = null;

        try {
            bearerToken = authorization.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = authorization;
        }

        QuestionEntity createdQuestion = CreateQuestionService.createQuestion(bearerToken);
        if (userAuthToken != null) {
            questionResponse = new QuestionResponse().id(userAuthToken.getUuid()).message("QUESTION CREATED");
        }
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

    /*
        endpoint to get all questions
     */

    @RequestMapping(method = RequestMethod.GET path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException{
    //public ResponseEntity<QuestionResponse> getAllQuestions(String bearerAuthorization) throws AuthorizationFailedException{
        String bearerToken = null;
        QuestionResponse questionResponse = null;
        try {
            bearerToken = authorization.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = authorization;
        }
        // getting a list of all questions here..
        final List<QuestionEntity> questionEntityList = ListAllQuestionService.getAllQuestions(bearerToken);
        QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
        for(Iterator<QuestionEntity> question = questionEntityList.iterator(); question.hasNext(); ) {
            questionDetailsResponse.content(id(question.next().getUuid()).content(question.next().getContent());
        }

        return new ResponseEntity<QuestionDetailsResponse>(questionDetailsResponse, HttpStatus.OK);

    }

}



