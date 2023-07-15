package com.danil.servlets.repository.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.danil.servlets.model.User;
import com.danil.servlets.repository.UserRepository;
import com.danil.servlets.utils.RepositoryUtils;

public class HibernateUserRepositoryImpl implements UserRepository {
    @Override
    public User create(User user) {
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

        return user;
    }

    @Override
    public User getByIdLazy(Integer id) {
        User user = null;
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

        return user;
    }

    @Override
    public User update(User user) {
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

        return user;
    }

    @Override
    public User getById(Integer id) {
        User user = null;
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            user = session.createSelectionQuery("FROM User u LEFT JOIN FETCH u.events e JOIN FETCH e.file WHERE u.id = :id", User.class).setParameter("id", id).getSingleResultOrNull();
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

        return user;
    }

    @Override
    public List<User> getAllLazy() {
        Transaction transaction = null;
        List<User> files = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            files = session.createSelectionQuery("FROM User", User.class).list();
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return files;
    }

    @Override
    public void deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            session.createMutationQuery("DELETE User WHERE id = :id").setParameter("id", id).executeUpdate();
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
