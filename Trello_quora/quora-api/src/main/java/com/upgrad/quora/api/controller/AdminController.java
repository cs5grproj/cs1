package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.DeleteService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AdminController {

  @Autowired private DeleteService deleteService;

  /**
   * This endpoint deletes a user from the Quora Application. Only an admin is authorized to access
   * this endpoint.
   *
   * @param userId - Unique identifier of User in a standard UUID format
   * @param authorization - Mandatory user credentials in bearer http authentication scheme format.
   * @return - Delete the records from all the tables related to that user and return 'uuid' of the
   *     deleted user from 'users' table and message 'USER SUCCESSFULLY DELETED' in the JSON
   *     response with the corresponding HTTP status.
   * @throws AuthorizationFailedException
   * @throws UserNotFoundException
   */
  @RequestMapping(
      method = RequestMethod.DELETE,
      path = "/admin/user/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  //    @GetMapping(value = "/userprofile/{userId}", produces = "application/json")
  public ResponseEntity<UserDeleteResponse> userDelete(
      @PathVariable("userId") final String userId,
      @RequestHeader("authorization") final String authorization)
      throws AuthorizationFailedException, UserNotFoundException {

//    String bearerToken = null;
//    try {
//      bearerToken = authorization.split("Bearer ")[1];
//    } catch (ArrayIndexOutOfBoundsException e) {
//      bearerToken = authorization;
//    }

    String uuid = deleteService.deleteUser(userId, authorization);
    UserDeleteResponse userDeleteResponse =
        new UserDeleteResponse().id(uuid).status("USER SUCCESSFULLY DELETED");
    return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
  }
}
