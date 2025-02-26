
package Entites;

import Entities.utilisateur;

public class Session {
    private static utilisateur currentUser;





    // Récupérer l'utilisateur actuellement connecté
    public static utilisateur getCurrentUser() {
        return currentUser;
    }




    // Définir l'utilisateur actuellement connecté
    public static void setCurrentUser(utilisateur user) {
        currentUser = user;
    }

    // Vérifier si un utilisateur est connecté
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    // Déconnexion de l'utilisateur
    public static void logout() {
        currentUser = null;
    }
}