package com.upgrad.quora.service.dao;


import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {
    @PersistenceContext
    private EntityManager entityManager;


    //createQuestion
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    //update question
    public QuestionEntity updateQuestion(final QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
        return questionEntity;
    }


    //deleteQuestion
    public void deleteQuestion(QuestionEntity questionEntity) {

        entityManager.remove(questionEntity);
    }

    //getQuestionByUserId
    public QuestionEntity getQuestionByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //getQuestionsByUser
    public List<QuestionEntity> getQuestionsByUser(final UserEntity userId) {
        try {

            return entityManager.createNamedQuery("getQuestionsByUser", QuestionEntity.class).setParameter("user", userId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }

    }

}

