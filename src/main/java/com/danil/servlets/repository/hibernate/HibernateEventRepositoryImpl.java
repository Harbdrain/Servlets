package com.danil.servlets.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.danil.servlets.model.Event;
import com.danil.servlets.repository.EventRepository;
import com.danil.servlets.utils.RepositoryUtils;

public class HibernateEventRepositoryImpl implements EventRepository {
    @Override
    public Event create(Event event) {
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            session.persist(event);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

        return event;
    }

    @Override
    public Event update(Event t) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Event getById(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public Event getByIdLazy(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'getByIdLazy'");
    }

    @Override
    public void deleteByFileId(Integer fileId) {
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            session.createNativeMutationQuery("DELETE FROM events WHERE file_id = :id").setParameter("id", fileId).executeUpdate();
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public void deleteByUserId(Integer userId) {
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            session.createNativeMutationQuery("DELETE FROM events WHERE user_id = :id").setParameter("id", userId).executeUpdate();
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
