package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    /**
     * This method is to create answer for a question
     * This method is listening for a HTTP POST request as indicated by method= RequestMethod.POST , maps to a URL request of type '/question/{questionId}/answer/create' and consumes and produces Json.
     * This method receives Header authorization, question Id, object of AnswerRequest type with its attributes being set.
     * @thows AuthorizationFailedException, InvalidQuestionException
     */
    @RequestMapping(method= RequestMethod.POST, path="/question/{questionId}/answer/create", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId, final AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {
        //Declaring the object of AnswerEntity class and set its corresponding attributes using answerRequest
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setUuid(UUID.randomUUID().toString());
        //After setting the attributes of answerEntity, pass it to createAnswer() method, to add this object to a persistent context
        final AnswerEntity createdAnswer = answerService.createAnswer(questionId,authorization,answerEntity);
        //Declaring an object of answerResponse type and set its attributes using createdAnswer
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswer.getUuid()).status("ANSWER CREATED");
        //Returns the answer UUID and Status in the Answer Response
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    /**
     * This method is to edit an existing answer
     * Its a HTTP PUT request as indicated by method= RequestMethod.PUT , maps to a URL request of type '/answer/edit/{answerId}' and consumes and produces Json.
     * This method receives Header authorization, answer UUID to be edited, object of AnswerEditRequest type with its attributes being set.
     * @thows AuthorizationFailedException, AnswerNotFoundException
     */
    @RequestMapping(method = RequestMethod.PUT, path="/answer/edit/{answerId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(@RequestHeader("authorization") final String authorization, @PathVariable("answerId") final String answerId, final AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {
        //Declaring the object of AnswerEntity class and set its corresponding attributes using answerRequest
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerEditRequest.getContent());
        //After setting the attributes of answerEntity, pass it to editAnswer() method, to add this object to a persistent context
        final AnswerEntity editedAnswer = answerService.editAnswer(answerId,authorization,answerEntity);
        //Declaring an object of answerEditResponse type and set its attributes using editedAnswer
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(editedAnswer.getUuid()).status("ANSWER EDITED");
        //Returns the answer UUID and Status in the AnswerEditResponse
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);
    }

    /**
     * This method is to delete Answer
     * Its a HTTP DELETE request as indicated by method= RequestMethod.DELETE , maps to a URL request of type '/answer/delete/{answerId}' and produces Json.
     * This method receives Header authorization, answer UUID to be deleted
     * @thows AuthorizationFailedException, AnswerNotFoundException
     */
    @RequestMapping(method = RequestMethod.DELETE, path="/answer/delete/{answerId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> editAnswer(@RequestHeader("authorization") final String authorization, @PathVariable("answerId") final String answerId) throws AuthorizationFailedException, AnswerNotFoundException {
        final AnswerEntity deleteAnswer = answerService.deleteAnswer(answerId,authorization);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(deleteAnswer.getUuid()).status("ANSWER DELETED");
        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse,HttpStatus.OK);
    }

    /**
     * This method is to get all Answers for the specific question
     * Its a HTTP GET request as indicated by method= RequestMethod.GET , maps to a URL request of type '/answer/all/{questionId}' and produces Json.
     * This method receives Header authorization, question UUID
     * @thows AuthorizationFailedException, InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.GET, path="/answer/all/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswers(@RequestHeader("authorization") final String authorization,@PathVariable("questionId") final String questionId)throws AuthorizationFailedException, InvalidQuestionException{
        final List<AnswerEntity> receivedAllAnswers = answerService.getAllAnswers(questionId,authorization);
        List<AnswerDetailsResponse> answerDetailsResponse = new ArrayList<AnswerDetailsResponse>();
        for (AnswerEntity n : receivedAllAnswers) {
            AnswerDetailsResponse answer = new AnswerDetailsResponse();
            answer.id(n.getUuid()).answerContent(n.getAns()).questionContent(n.getQuestion().getContent());
            answerDetailsResponse.add(answer);
        }
        return new ResponseEntity<List<AnswerDetailsResponse>> (answerDetailsResponse, HttpStatus.OK);
    }
}
