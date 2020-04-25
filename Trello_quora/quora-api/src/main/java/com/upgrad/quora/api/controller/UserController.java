package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SigninService;
import com.upgrad.quora.service.business.SignoutService;
import com.upgrad.quora.service.business.SignupService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
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
public class UserController {

  @Autowired private SignupService signupService;
  @Autowired private SigninService signinService;
  @Autowired private SignoutService signoutService;

  /**
   * This endpoint is used to register a new user in the Quora Application
   *
   * @param signupUserRequest - Request containing user signup information
   * @return - Return the 'uuid' of the registered user and message 'USER SUCCESSFULLY REGISTERED'
   *     in the JSON response with the corresponding HTTP status.
   * @throws SignUpRestrictedException
   */
  @RequestMapping(
      method = RequestMethod.POST,
      path = "/user/signup",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupUserResponse> signUpUser(final SignupUserRequest signupUserRequest)
      throws SignUpRestrictedException {
    final UserEntity userEntity = new UserEntity();
    userEntity.setUuid(UUID.randomUUID().toString());
    userEntity.setFirstName(signupUserRequest.getFirstName());
    userEntity.setLastName(signupUserRequest.getLastName());
    userEntity.setUserName(signupUserRequest.getUserName());
    userEntity.setEmail(signupUserRequest.getEmailAddress());
    userEntity.setPassword(signupUserRequest.getPassword());
    userEntity.setSalt("1234abc");
    userEntity.setCountry(signupUserRequest.getCountry());
    userEntity.setAboutMe(signupUserRequest.getAboutMe());
    userEntity.setDob(signupUserRequest.getDob());
    userEntity.setContactNumber(signupUserRequest.getContactNumber());
    userEntity.setRole("nonadmin");

    final UserEntity createdUserEntity = signupService.signUpUser(userEntity);
    SignupUserResponse userResponse =
        new SignupUserResponse()
            .id(createdUserEntity.getUuid())
            .status("USER SUCCESSFULLY REGISTERED");
    return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
  }

  /**
   * This endpoint is used for user authentication and sign in
   *
   * @param authorization - Requests for the User credentials to be passed in the authorization
   *     field of header as part of Basic authentication
   * @return - Return the 'uuid' of the authenticated user from 'users' table and message 'SIGNED IN
   *     SUCCESSFULLY' in the JSON response with the corresponding HTTP status.
   * @throws AuthenticationFailedException
   */
  @RequestMapping(
      method = RequestMethod.POST,
      path = "/user/signin",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SigninResponse> signin(
      @RequestHeader("authorization") final String authorization)
      throws AuthenticationFailedException {

    byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
    String decodedText = new String(decode);
    String[] decodedArray = decodedText.split(":");

    UserAuthTokenEntity userAuthToken =
        signinService.authenticate(decodedArray[0], decodedArray[1]);
    UserEntity user = userAuthToken.getUser();

    SigninResponse signinResponse =
        new SigninResponse().id(user.getUuid()).message("SIGNED IN SUCCESSFULLY");

    HttpHeaders headers = new HttpHeaders();
    headers.add("access-token", userAuthToken.getAccessToken());
    return new ResponseEntity<SigninResponse>(signinResponse, headers, HttpStatus.OK);
  }

  /**
   * This endpoint is used to sign out a user from the Quora Application.
   *
   * @param authorization - Request the access token of the signed in user in the authorization
   *     field of the Request Header
   * @return - On logout, update the LogoutAt time of the user and return the 'uuid' of the signed
   *     out user and message 'SIGNED OUT SUCCESSFULLY' in the JSON response with the corresponding
   *     HTTP status.
   * @throws SignOutRestrictedException
   */
  @RequestMapping(
      method = RequestMethod.POST,
      path = "/user/signout",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignoutResponse> signout(
      @RequestHeader("authorization") final String authorization)
      throws SignOutRestrictedException {

    String bearerToken;
    SignoutResponse signoutResponse = null;

    try {
      bearerToken = authorization.split("Bearer ")[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      bearerToken = authorization;
    }

    UserAuthTokenEntity userAuthToken = signoutService.signOut(bearerToken);
    if (userAuthToken != null) {
      signoutResponse =
          new SignoutResponse().id(userAuthToken.getUuid()).message("SIGNED OUT SUCCESSFULLY");
    }
    return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
  }
}
