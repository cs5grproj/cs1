package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerEditRequest;
import com.upgrad.quora.api.model.AnswerEditResponse;
import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @RequestMapping(method= RequestMethod.POST, path="/question/{questionId}/answer/create", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") final String authorization, @PathVariable("questionId") final String questionId, final AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerRequest.getAnswer());
        answerEntity.setUuid(UUID.randomUUID().toString());
        final AnswerEntity createdAnswer = answerService.createAnswer(questionId,authorization,answerEntity);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswer.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path="/answer/edit/{answerId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(@RequestHeader("authorization") final String authorization, @PathVariable("answerId") final String answerId, final AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAns(answerEditRequest.getContent());
        final AnswerEntity editedAnswer = answerService.editAnswer(answerId,authorization,answerEntity);
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(editedAnswer.getUuid()).status("ANSWER EDITED");
        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);
    }
}
