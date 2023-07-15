package com.danil.servlets.repository.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.danil.servlets.model.File;
import com.danil.servlets.repository.FileRepository;
import com.danil.servlets.utils.RepositoryUtils;

public class HibernateFileRepositoryImpl implements FileRepository {
    @Override
    public File create(File file) {
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            session.persist(file);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

        return file;
    }

    @Override
    public File update(File file) {
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            session.merge(file);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

        return file;
    }

    @Override
    public File getById(Integer id) {
        File file = null;
        Transaction transaction = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            file = session.get(File.class, id);
            session.flush();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }

        return file;
    }

    @Override
    public File getByIdLazy(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'getByIdLazy'");
    }

    @Override
    public List<File> getAll() {
        Transaction transaction = null;
        List<File> files = null;
        try (Session session = RepositoryUtils.getSession()) {
            transaction = session.beginTransaction();
            files = session.createSelectionQuery("FROM File", File.class).list();
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
            session.createMutationQuery("DELETE File WHERE id = :id").setParameter("id", id).executeUpdate();
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
