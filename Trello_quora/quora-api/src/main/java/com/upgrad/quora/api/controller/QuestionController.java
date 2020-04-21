package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.service.business.SigninBusinessService;
import com.upgrad.quora.service.business.SignoutBusinessService;
import com.upgrad.quora.service.business.SignupBusinessService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
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


    @RequestMapping(method = RequestMethod.POST, path = "/question/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
// think of inputparameters for this method
        String bearerToken = null;
        SignoutResponse signoutResponse = null;

        try {
            bearerToken = authorization.split("Bearer ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            bearerToken = authorization;
        }

        UserAuthTokenEntity userAuthToken = signoutBusinessService.signOut(bearerToken);
        if (userAuthToken != null) {
            signoutResponse = new SignoutResponse().id(userAuthToken.getUuid()).message("SIGN OUT SUCCESSFULLY");
        }
        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> getAllQuestions(String bearerAuthorization) throws AuthorizationFailedException{
        // ListAllQuestionService.getAllQuestions(bearerAuthorization)
        final UserEntity userEntity = userBusinessService.getUserByUuid(userId, bearerAuthorization);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName()).userName(userEntity.getUserName()).emailAddress(userEntity.getEmail())
                .country(userEntity.getCountry()).aboutMe(userEntity.getAboutMe()).dob(userEntity.getDob()).contactNumber(userEntity.getContactNumber());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);

    }

}



