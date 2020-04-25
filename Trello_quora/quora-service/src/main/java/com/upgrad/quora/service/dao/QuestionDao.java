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


    /**
     * method to store created question in the database
     *
     * @param questionEntity  question to be stored
     * @return  question saves in the database
     */
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    /**
     * method to retrieve questions from the database
     *
     * @return  returns list of questions of any user from database
     */
    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * method to edit the question and merge it in the database
     *
     * @param questionEntity  question which is to be edited
     * @return  merges the edited question in the database
     */
    public QuestionEntity updateQuestion(final QuestionEntity questionEntity) {
        entityManager.merge(questionEntity);
        return questionEntity;
    }

    /**
     * method to delete the question from the database
     *
     * @param questionEntity  question which is to deleted
     */
    public void deleteQuestion(QuestionEntity questionEntity) {
        entityManager.remove(questionEntity);
    }

    /**
     * method to retrieve question by user Id from the database
     *
     * @param uuid  question Id
     * @return  returns the question
     */
    public QuestionEntity questionByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * method to retrieve the list of  questions of a user from database
     *
     * @param userId  userId
     * @return  returns list of all questions posted by a user
     */
    public List<QuestionEntity> getQuestionsByUser(final UserEntity userId) {
        try {
            return entityManager.createNamedQuery("getQuestionsByUser", QuestionEntity.class).setParameter("user", userId).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}

