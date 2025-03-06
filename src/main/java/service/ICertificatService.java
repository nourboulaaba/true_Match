package service;

import entite.Certificat;
import java.util.List;

public interface ICertificatService {
    void insert(Certificat c);
    void update(Certificat c);
    void delete(Certificat c);
    List<Certificat> getAll();
    Certificat getById(int id);
}