package Service;

import Entite.Personne;

import java.util.List;

public interface IService<T>{
    void insert(T t) ;
    void update(T t);
    void delete(T t);
    List<T> readAll();
    T readById(int id);
}
