package org.test;

import Entite.Personne;
import Service.PersonneService;
import utile.DataSource;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Création d'une instance du service
        PersonneService ps = new PersonneService();

        // Insertion d'une nouvelle personne
        Personne p1 = new Personne("Doe", "John");
        ps.insert(p1);
        System.out.println("✅ Personne insérée avec succès.");

        // Affichage de toutes les personnes
        System.out.println("\n📌 Liste des personnes :");
        ps.readAll().forEach(System.out::println);

        // Mise à jour de la personne avec ID 1 (⚠ Modifier selon votre base)
        Personne p2 = new Personne(1, "Doe", "Jane");
        ps.update(p2);
        System.out.println("\n✅ Mise à jour réussie pour ID 1.");

        // Lecture d'une personne par ID
        int idRecherche = 1; // Modifier selon un ID existant dans votre BD
        Personne p3 = ps.readById(idRecherche);
        if (p3 != null) {
            System.out.println("\n🔍 Personne trouvée : " + p3);
        } else {
            System.out.println("\n⚠ Aucune personne trouvée avec l'ID " + idRecherche);
        }

        // Suppression d'une personne (exemple avec ID 6)
        int idASupprimer = 6;
        Personne p4 = new Personne();
        p4.setId(idASupprimer);
        ps.delete(p4);
        System.out.println("\n🗑️ Personne avec ID " + idASupprimer + " supprimée.");

        // Affichage final après suppression
        System.out.println("\n📌 Liste après suppression :");
        ps.readAll().forEach(System.out::println);
    }
}