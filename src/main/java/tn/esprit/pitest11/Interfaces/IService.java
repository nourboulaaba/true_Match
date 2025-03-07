package tn.esprit.pitest11.Interfaces;

import java.util.List;

public interface IService <T>{
    //CRUD
    //la relation ou communication avec la base de données
    //requetes SQL : Ajout , Suppression , Modification , Selection
    //les methodes : add , delete , update , readAll , readById
    void add(T t);
    void delete(int id);
    void update(T t, int id);
    T readById(int id);
    List<T> readAll();


}
