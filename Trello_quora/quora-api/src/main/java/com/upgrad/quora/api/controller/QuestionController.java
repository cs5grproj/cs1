package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    /**
     * This method is used to create a question in the application
     *
     * @param authorization requests the access token of the signed in user in the authorization
     *                        field of the Request Header
     * @param questionRequest  requests the content for the question
     * @return  returns the 'uuid' of the created question  and message 'QUESTION CREATED'.
     * @throws AuthorizationFailedException
     */

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/question/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(
            @RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest)
            throws AuthorizationFailedException {
        QuestionEntity questionEntity = new QuestionEntity();
        //sets the content of question
        questionEntity.setContent(questionRequest.getContent());
        //sets the 'UUID' of the question
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(ZonedDateTime.now());

        final QuestionEntity createdQuestion = questionService.createQuestion(authorization, questionEntity);

        QuestionResponse questionResponse = new QuestionResponse().
                id(createdQuestion.getUuid()).
                status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    /**
     * This method is used to fetch all the questions that have posted in the application by any user
     *
     * @param authorization requests the access token of the signed in user in the authorization
     *                      field of the Request Header
     * @return  returns 'uuid' and 'content' of all the questions from the database
     * @throws AuthorizationFailedException
     */

    @RequestMapping(
            method = RequestMethod.GET, path = "/question/all",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(
            @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException {
        List<QuestionEntity> allReceivedQuestions = questionService.getAllQuestions(authorization);
        //declaring a list to store all questions of any user
        List<QuestionDetailsResponse> questionDetailsResponsesList = new ArrayList<>();
        for (QuestionEntity questionEntity : allReceivedQuestions) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().
                    id(questionEntity.getUuid()).content(questionEntity.getContent());
            //adding all the received questions to the list
            questionDetailsResponsesList.add(questionDetailsResponse);
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsResponsesList, HttpStatus.OK);
    }

    /**
     * This method is used to edit a question that has been posted by a user
     *
     * @param authorization requests the access token of the signed in user in the authorization
     *                            field of the Request Header
     * @param questionId  requests the question id which is to be edited
     * @param questionEditRequest  requests the content of the question
     * @return  returns 'uuid' of the edited question and message 'QUESTION EDITED'
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @RequestMapping(
            method = RequestMethod.PUT,
            path = "/question/edit/{questionId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("questionId") final String questionId,
            final QuestionEditRequest questionEditRequest)
            throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = new QuestionEntity();
        //sets the content of the question which is to be edited
        questionEntity.setContent(questionEditRequest.getContent());
        final QuestionEntity editedQuestion = questionService.editQuestion(authorization, questionId, questionEntity);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse()
                .id(editedQuestion.getUuid()).
                        status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    /**
     * This endpoint is used to delete a question that has been posted by a user
     *
     * @param authorization requests access token of the signed in user as a string in the authorization
     *                      field of the Request Header
     * @param questionId  Requests the questionId for the corresponding question which is to be deleted
     * @return returns 'uuid' of the deleted question and message 'QUESTION DELETED'
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/question/delete/{questionId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("questionId") final String questionId)
            throws AuthorizationFailedException, InvalidQuestionException {
        final QuestionEntity deleteQuestion = questionService.deleteQuestion(questionId, authorization);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(deleteQuestion.getUuid()).status("Question DELETED");
        //returns the uuid of the question deleted
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

    /**
     * This method is used to fetch all the questions posted by a specific user
     *
     * @param authorization  requests access token of the signed in user as a string in the authorization
     *                      field of the Request Header
     * @param userId  requests the UUID of the corresponding user whose questions are to be retrieved from
     *                      the database
     * @return  returns 'uuid' and 'content' of all the questions posed by the corresponding user from the database
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */

    @RequestMapping(
            method = RequestMethod.GET,
            path = "question/all/{userId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(
            @RequestHeader("authorization") final String authorization,
            @PathVariable("userId") final String userId)
            throws AuthorizationFailedException, UserNotFoundException {
        List<QuestionEntity> allQuestions = questionService.getAllQuestionsByUser(userId, authorization);
        // Declaring a List to store all the questions of corresponding user whose questions are to be retrieved
        List<QuestionDetailsResponse> questionDetailsByUserResponseList = new ArrayList<>();
        for (QuestionEntity question : allQuestions) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().
                    id(question.getUuid()).content(question.getContent());
            //Adding all the questions to the list
            questionDetailsByUserResponseList.add(questionDetailsResponse);

        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionDetailsByUserResponseList, HttpStatus.OK);
    }

}


