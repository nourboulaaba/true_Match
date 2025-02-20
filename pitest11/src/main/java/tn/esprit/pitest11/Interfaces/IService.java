package tn.esprit.pitest11.Interfaces;

import java.util.List;

public interface IService <T>{

    void add(T t);
    void delete(int id);
    void update(T t, int id);
    T readById(int id);
    List<T> readAll();
}