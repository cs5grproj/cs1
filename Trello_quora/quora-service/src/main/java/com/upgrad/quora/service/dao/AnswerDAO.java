package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDAO {
  @PersistenceContext private EntityManager entityManager;
  // Function to return the Question Entity object based on the question uuid passed. If doesn't
  // exists, null is returned
  public QuestionEntity getQuestion(final String uuid) {
    try {
      return entityManager
          .createNamedQuery("questionByUuid", QuestionEntity.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }
  // Function to return the Answer Entity object after creating the answer record
  public AnswerEntity createAnswer(AnswerEntity answerEntity) {
    entityManager.persist(answerEntity);
    return answerEntity;
  }
  // Function to return the Answer Entity object based on the answer uuid passed. If doesn't exists,
  // null is returned
  public AnswerEntity getAnswerById(final String uuid) {
    try {
      return entityManager
          .createNamedQuery("answerByUuid", AnswerEntity.class)
          .setParameter("uuid", uuid)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  // Function to update the answer with the answer entity details passed
  public AnswerEntity updateAnswer(final AnswerEntity answerEntity) {
    return entityManager.merge(answerEntity);
  }

  // Function to delete the answer with the answer entity details passed
  public void deleteAnswer(final AnswerEntity answerEntity) {
    entityManager.remove(answerEntity);
  }

  // Function to get all the answers for the question uuid passed. If no result found, return null
  public List<AnswerEntity> getAnswerByQuestionId(final QuestionEntity questionId) {
    try {
      List<AnswerEntity> resultList =
          entityManager
              .createNamedQuery("answersByQuestionId", AnswerEntity.class)
              .setParameter("question", questionId)
              .getResultList();
      return resultList;
    } catch (NoResultException nre) {
      return null;
    }
  }
}
