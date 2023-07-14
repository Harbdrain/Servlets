package com.danil.servlets.repository;

public interface GenericRepository<T> {
    T create(T t);
    T update(T t);
    T getById(Integer id);
    T getByIdLazy(Integer id);
}
