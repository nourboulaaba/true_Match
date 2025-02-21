package org.example;

import Entities.utilisateur;
import Entities.Role;
import service.utilisateurService;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        utilisateurService service = new utilisateurService();

        // Insertion d'un nouvel utilisateur
        utilisateur user1 = new utilisateur(
                "Doe", "John", "john123", "john.doe@example.com", "password123",
                "Developer", Role.EMPLOYE, "EMP001", "face123", 5000.0, "2023-10-01", "123456789", "cv_john.pdf", "profile.jpg"
        );
        System.out.println("Role: " + user1.getRole());

        service.insert(user1);
        System.out.println("Role: " + user1.getRole());

        // Récupération de tous les utilisateurs
        System.out.println("Liste des utilisateurs :");
        List<utilisateur> utilisateurs = service.getAll();
        utilisateurs.forEach(System.out::println);

        // Récupération d'un utilisateur par son ID
        utilisateur user = service.getById(1);
        if (user != null) {
            System.out.println("Utilisateur trouvé : " + user);
        } else {
            System.out.println("Utilisateur non trouvé.");
        }

        // Mise à jour d'un utilisateur
        if (user != null) {
            user.setSalary(5500.0);
            user.setPhoneNumber("987654321");
            user.setProfilePhoto("new_profile.jpg");
            service.update(user);
            System.out.println("Utilisateur mis à jour avec succès !");
        }

        // Suppression de l'utilisateur
        if (user != null) {
            service.delete(user);
            System.out.println("Utilisateur supprimé avec succès !");
        }

        // Récupération de tous les utilisateurs après suppression
        System.out.println("Liste des utilisateurs après suppression :");
        service.getAll().forEach(System.out::println);
    }
}