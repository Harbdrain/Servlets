package com.danil.servlets.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class RepositoryUtils {
    private static SessionFactory sessionFactory;

    static {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .loadProperties("hibernate.properties")
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(com.danil.servlets.model.File.class)
                    .addAnnotatedClass(com.danil.servlets.model.User.class)
                    .addAnnotatedClass(com.danil.servlets.model.Event.class)
                    .buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            System.exit(1);
        }
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return getSessionFactory().openSession();
    }
}
