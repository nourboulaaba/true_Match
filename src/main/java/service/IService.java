package service;
import java.util.List;
public interface IService <T> {
    void insert(T t);
    void update(T t);
    void delete(T t);
    void deleteByID(int id);
    List<T> getAll();
    T getById(int id);

}
