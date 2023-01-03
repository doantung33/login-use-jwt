package com.example.login.service;

import java.util.List;

public interface IService<I> {
    List <I> findAll();
    I findById (Long id);
    I save (I i);
    void delete (Long id);
}
