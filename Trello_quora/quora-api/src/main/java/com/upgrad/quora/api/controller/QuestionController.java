package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.CreateQuestionService;
import com.upgrad.quora.service.business.ListAllQuestionService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CreateQuestionService createQuestionService;
    @Autowired
    private ListAllQuestionService listAllQuestionService;

    //create Question
    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest) throws AuthorizationFailedException {
        String bearerToken = null;
        QuestionResponse questionResponse = null;

        try {
            bearerToken = authorization.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = authorization;
        }
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUuid(UUID.randomUUID().toString());
        QuestionEntity createdQuestion = createQuestionService.createQuestion(bearerToken);
        questionResponse = new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

   //getAllQuestions
    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        //public ResponseEntity<QuestionDetailsResponse> getAllQuestions(String bearerAuthorization) throws AuthorizationFailedException{
        String bearerToken = null;
        QuestionResponse questionResponse = null;
        try {
            bearerToken = authorization.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = authorization;
        }
        // getting a list of all questions here..
        final List<QuestionEntity> questionEntityList = listAllQuestionService.getAllQuestions(bearerToken);
        List<QuestionDetailsResponse> questionDetailsResponseList = new ArrayList<>();
        for (Iterator<QuestionEntity> question = questionEntityList.iterator(); question.hasNext(); ) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(question.next().getUuid()).content(question.next().getContent());
            questionDetailsResponseList.add(questionDetailsResponse);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponseList, HttpStatus.OK);

    }

    //editQuestion
    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId, final QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionEditRequest.getContent());
        final QuestionEntity editedQuestion = questionService.editQuestion(authorization, questionId, questionEntity);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(editedQuestion.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    //deleteQuestion
    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        final QuestionEntity deleteQuestion = questionService.deleteQuestion(questionId, authorization);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(deleteQuestion.getUuid()).status("Question DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    //getALLQuestionsByUser
    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization, @PathVariable("userId") final String userId) throws AuthorizationFailedException, UserNotFoundException {
        //stores all the retrieved questions in "allQuestions"
        List<QuestionEntity> allQuestions = questionService.getAllQuestionsByUser(userId, authorization);
        List<QuestionDetailsResponse> questionDetailsByUserResponseList = new ArrayList<>();
        for (QuestionEntity question : allQuestions) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(question.getUuid()).content(question.getContent());
            questionDetailsByUserResponseList.add(questionDetailsResponse);

        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsByUserResponseList, HttpStatus.OK);

    }

}


