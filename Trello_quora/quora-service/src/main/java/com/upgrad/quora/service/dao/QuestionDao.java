package com.upgrad.quora.service.dao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
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

    public List<QuestionEntity> getAllQuestions(final String authorizationToken) {
        try {
            return entityManager.createNamedQuery("userByName", UserEntity.class).setParameter("userName", userAuthTokenEntity)
                    .getResultList(); //work on this query
        } catch (NoResultException nre) {
            return null;
        }
    }
}