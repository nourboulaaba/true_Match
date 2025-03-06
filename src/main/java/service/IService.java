package service;
import java.sql.SQLException;
import java.util.List;
public interface IService <T> {
    //boolean insert(T t);
    void update(T t);
    void delete(T t);
    //void deleteByID(int id) throws SQLException;
    List<T> getAll();
    T getById(int id);

}
