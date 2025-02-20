package org.example;

import entite.formations;
import entite.Certificat;
import service.FormationService;
import service.CertificatService;

public class Main {
    public static void main(String[] args) {
        FormationService formationService = new FormationService();
        CertificatService certificatService = new CertificatService();

        // --------- Gestion des Formations ---------
        System.out.println("===== Gestion des Formations =====");

        formations f1 = new formations(1, "Java Basics", "Introduction à Java", "100€");
        formations f2 = new formations(2, "Spring Boot", "Développement Web avec Spring Boot", "200€");

        formationService.insert(f1);
        formationService.insert(f2);

        System.out.println("Toutes les formations en base : " + formationService.getAll());

        formations f1Updated = new formations(1, "Java Avancé", "Concepts avancés de Java", "150€");
        formationService.update(f1Updated);

        System.out.println("Formation avec ID 1 : " + formationService.getById(1));

        formationService.delete(f2);

        System.out.println("Toutes les formations après suppression : " + formationService.getAll());


        // --------- Gestion des Certificats ---------
        System.out.println("\n===== Gestion des Certificats =====");

        Certificat c1 = new Certificat(1, 1, "2025-05-10", "10:00", 120, "100€", "Débutant");
        Certificat c2 = new Certificat(2, 2, "2025-06-15", "14:00", 90, "150€", "Intermédiaire");

        certificatService.insert(c1);
        certificatService.insert(c2);

        System.out.println("Tous les certificats en base : " + certificatService.getAll());

        Certificat c1Updated = new Certificat(1, 1, "2025-05-12", "11:00", 130, "120€", "Avancé");
        certificatService.update(c1Updated);

        System.out.println("Certificat avec ID 1 : " + certificatService.getById(1));

        certificatService.delete(c2);

        System.out.println("Tous les certificats après suppression : " + certificatService.getAll());
    }
}
