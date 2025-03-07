package tn.esprit.pitest11.service;

import tn.esprit.pitest11.Entities.Certificat;
import java.util.List;

public interface ICertificatService {
    void insert(Certificat c);
    void update(Certificat c);
    void delete(Certificat c);
    List<Certificat> getAll();
    Certificat getById(int id);
}