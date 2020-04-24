package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDAO;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnswerService {
  @Autowired private AnswerDAO answerDao;
  @Autowired private CommonService commonService;

  /**
   * This method is to create Answer.
   *
   * @param questionId
   * @param authorizationToken
   * @param answerEntity object with unique randomly generated answer UUID and answer content passed
   *     in the Answer Request
   * @return AnswerEntity object with the created answer record details in database
   * @throws AuthorizationFailedException if authorization is failed
   * @throws InvalidQuestionException if question id passed to the method doesn't exists in answer
   *     table
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public AnswerEntity createAnswer(
      final String questionId, final String authorizationToken, final AnswerEntity answerEntity)
      throws AuthorizationFailedException, InvalidQuestionException {
    // Checking the user is authorized and if authorized user is logged in.
    // Validate User Authorization status is implemented as a common function in Common Service
    UserAuthTokenEntity userAuthTokenEntity =
        commonService.getUserAuthorizationStatus(authorizationToken);
    answerEntity.setUser(userAuthTokenEntity.getUser());
    QuestionEntity questionEntity = answerDao.getQuestion(questionId);
    if (questionEntity == null) {
      throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
    }
    answerEntity.setQuestion(questionEntity);
    answerEntity.setDate(ZonedDateTime.now());
    return answerDao.createAnswer(answerEntity);
  }

  /**
   * This method is to edit Answer.
   *
   * @param answerId
   * @param authorizationToken
   * @param answerEntity object - contains the answer content which is passed in the edit answer
   *     request
   * @return AnswerEntity object of the edited answer record from answer table
   * @throws AnswerNotFoundException if answer uuid passed in the request doesn't exist in answer
   *     table
   * @throws AuthorizationFailedException if authorization is failed
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public AnswerEntity editAnswer(
      final String answerId, final String authorizationToken, final AnswerEntity answerEntity)
      throws AuthorizationFailedException, AnswerNotFoundException {
    // Checking the user is authorized and if authorized user is logged in.
    // Validate User Authorization status is implemented as a common function in Common Service
    UserAuthTokenEntity userAuthTokenEntity =
        commonService.getUserAuthorizationStatus(authorizationToken);
    AnswerEntity existingAnswerEntity = answerDao.getAnswerById(answerId);
    if (existingAnswerEntity == null) {
      throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
    }
    if (existingAnswerEntity.getUser().getId() == userAuthTokenEntity.getUser().getId()) {
      existingAnswerEntity.setAns(answerEntity.getAns());
      existingAnswerEntity.setDate(ZonedDateTime.now());
      return answerDao.updateAnswer(existingAnswerEntity);
    } else {
      throw new AuthorizationFailedException(
          "ATHR-003", "Only the answer owner can edit the answer");
    }
  }

  /**
   * This method is to delete Answer.
   *
   * @param answerId
   * @param authorizationToken
   * @return AnswerEntity object of the deleted answer record from answer table
   * @throws AnswerNotFoundException if answer uuid passed in the request doesn't exist in answer
   *     table
   * @throws AuthorizationFailedException if authorization is failed
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public AnswerEntity deleteAnswer(final String answerId, final String authorizationToken)
      throws AuthorizationFailedException, AnswerNotFoundException {
    // Checking the user is authorized and if authorized user is logged in.
    // Validate User Authorization status is implemented as a common function in Common Service
    UserAuthTokenEntity userAuthTokenEntity =
        commonService.getUserAuthorizationStatus(authorizationToken);
    AnswerEntity existingAnswerEntity = answerDao.getAnswerById(answerId);
    if (existingAnswerEntity == null) {
      throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
    }
    if ((existingAnswerEntity.getUser().getId() == userAuthTokenEntity.getUser().getId())
        || userAuthTokenEntity.getUser().getRole().equals("admin")) {
      answerDao.deleteAnswer(existingAnswerEntity);
      return existingAnswerEntity;
    } else {
      throw new AuthorizationFailedException(
          "ATHR-003", "Only the answer owner or admin can delete the answer");
    }
  }

  /**
   * This method is to get all Answers for the required question
   *
   * @param questionId
   * @param authorizationToken
   * @return List of AnswerEntity objects that contains the all the answers from answer table for
   *     the specific function
   * @throws AuthorizationFailedException if authorization is failed
   * @throws InvalidQuestionException if question id passed to the method doesn't exists in answer
   *     table
   */
  @Transactional(propagation = Propagation.REQUIRED)
  public List<AnswerEntity> getAllAnswers(final String questionId, final String authorizationToken)
      throws AuthorizationFailedException, InvalidQuestionException {
    UserAuthTokenEntity userAuthTokenEntity =
        commonService.getUserAuthorizationStatus(authorizationToken);
    QuestionEntity questionEntity = answerDao.getQuestion(questionId);
    if (questionEntity == null) {
      throw new InvalidQuestionException(
          "QUES-001", "The question with entered uuid whose details are to be seen does not exist");
    }
    return answerDao.getAnswerByQuestionId(questionEntity);
  }
}
