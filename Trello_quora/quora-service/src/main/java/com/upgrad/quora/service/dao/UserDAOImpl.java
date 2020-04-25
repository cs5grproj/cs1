package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDAOImpl implements UserDAO {

  @PersistenceContext private EntityManager entityManager;

  /**
   * Method to persist all the attributes about the user
   *
   * @param userEntity - Accepts UserEntity object as argument
   * @return - returns userEntity
   */
  public UserEntity signUpUser(final UserEntity userEntity) {
    entityManager.persist(userEntity);
    return userEntity;
  }

  /**
   * Fetch user record by username
   *
   * @param userName - Username as argument
   * @return - returns record as UserEntity or null
   */
  public UserEntity getUserByName(final String userName) {
    try {
      return entityManager
          .createNamedQuery("userByName", UserEntity.class)
          .setParameter("userName", userName)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Fetch user record by email
   *
   * @param email - Email as argument
   * @return - returns record as UserEntity or null
   */
  public UserEntity getUserByEmail(final String email) {
    try {
      return entityManager
          .createNamedQuery("userByEmail", UserEntity.class)
          .setParameter("email", email)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Fetch user record by UUID
   *
   * @param userId - UUID as argument
   * @return - returns record as UserEntity or null
   */
  public UserEntity getUserByUuid(final String userId) {
    try {
      return entityManager
          .createNamedQuery("userByUuid", UserEntity.class)
          .setParameter("uuid", userId)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Method to persist authentication token
   *
   * @param userAuthTokenEntity - Authorization token
   * @return - Authorization token
   */
  public UserAuthTokenEntity createAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
    entityManager.persist(userAuthTokenEntity);
    return userAuthTokenEntity;
  }

  /** @param updatedUserEntity */
  public void updateUser(final UserEntity updatedUserEntity) {
    entityManager.merge(updatedUserEntity);
  }

  /**
   * Fetch user record
   *
   * @param userName - Username as argument
   * @param password - Password as argument
   * @return - returns record as UserEntity or null
   */
  public UserEntity authenticateUser(final String userName, final String password) {
    try {
      return entityManager
          .createNamedQuery("authenticateUserQuery", UserEntity.class)
          .setParameter("userName", userName)
          .setParameter("password", password)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Fetch authorization token as record
   *
   * @param authorization - Authorization token
   * @return - returns record as UserAuthTokenEntity
   */
  public UserAuthTokenEntity getUserAuthToken(final String authorization) {
    try {
      return entityManager
          .createNamedQuery("userAuthTokenByAccessToken", UserAuthTokenEntity.class)
          .setParameter("accessToken", authorization)
          .getSingleResult();
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Sign out user
   *
   * @param userAuthTokenEntity - Authorization token
   * @return - returns authorization token or null
   */
  public UserAuthTokenEntity userSignOut(final UserAuthTokenEntity userAuthTokenEntity) {
    try {
      return entityManager.merge(userAuthTokenEntity);
    } catch (NoResultException nre) {
      return null;
    }
  }

  /**
   * Delete user with the matching UUID from related schemas
   *
   * @param uuid - UUID as argument
   */
  public void deleteUser(final String uuid) {
    UserEntity userEntity = getUserByUuid(uuid);
    entityManager.remove(userEntity);
  }
}
