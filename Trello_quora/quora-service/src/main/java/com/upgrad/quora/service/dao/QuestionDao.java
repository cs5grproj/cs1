package com.upgrad.quora.service.dao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity QuestionEntity) {
        entityManager.persist(QuestionEntity);
        return QuestionEntity;
    }
    /*
    method to get all the questions from the question DB
     */
    public List<QuestionEntity> getAllQuestions(final String authorizationToken) {
        try {
            return entityManager.createNamedQuery("getAllQuestionsQuery", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}