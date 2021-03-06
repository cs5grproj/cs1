package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

  @Autowired private CommonService commonService;

  /**
   * This endpoint fetches details of any user in the Quora Application.
   *
   * @param userId - Unique identifier of User in a standard UUID format
   * @param authorization - Mandatory user credentials in bearer http authentication scheme format.
   * @return - Returns all the details of the user from the database in the JSON response with the
   *     corresponding HTTP status.
   * @throws AuthorizationFailedException
   * @throws UserNotFoundException
   */
  @RequestMapping(
      method = RequestMethod.GET,
      path = "/userprofile/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  //    @GetMapping(value = "/userprofile/{userId}", produces = "application/json")
  public ResponseEntity<UserDetailsResponse> userProfile(
      @PathVariable("userId") final String userId,
      @RequestHeader("authorization") final String authorization)
      throws AuthorizationFailedException, UserNotFoundException {

    //        String bearerToken = null;
    //
    //        try {
    //            bearerToken = authorization.split("Bearer ")[1];
    //        } catch (ArrayIndexOutOfBoundsException e) {
    //            bearerToken = authorization;
    //        }

    final UserEntity userEntity = commonService.getUserByUuid(userId, authorization);
    UserDetailsResponse userDetailsResponse =
        new UserDetailsResponse()
            .firstName(userEntity.getFirstName())
            .lastName(userEntity.getLastName())
            .userName(userEntity.getUserName())
            .emailAddress(userEntity.getEmail())
            .country(userEntity.getCountry())
            .aboutMe(userEntity.getAboutMe())
            .dob(userEntity.getDob())
            .contactNumber(userEntity.getContactNumber());
    return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
  }
}
