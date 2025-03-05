package utils;

public class Constants {

    // Modèle d'email pour l'OTP de réinitialisation de mot de passe
    public static final String OTP_EMAIL_TEMPLATE =
            "<div style=\"max-width: 680px; margin: 0 auto; padding: 45px 30px 60px; " +
                    "background-color: #2b2d33; border-radius: 15px; text-align: center; " +
                    "color: #c9c9c9; font-family: Arial, sans-serif;\">" +
                    "<h1 style=\"margin: 0; font-size: 24px; font-weight: 500; color: #e8e8e8;\">" +
                    "Votre OTP pour la réinitialisation du mot de passe</h1>" +
                    "<p style=\"margin: 20px 0; font-size: 16px; font-weight: 400; color: #c9c9c9;\">" +
                    "Veuillez utiliser le code OTP suivant pour réinitialiser votre mot de passe :</p>" +
                    "<h2 style=\"margin: 20px 0; font-size: 32px; font-weight: 600; color: #4caf50;\">%s</h2>" +
                    "<p style=\"margin: 20px 0; font-size: 14px; font-weight: 400; color: #b1b1b1;\">" +
                    "Ce code OTP est valable pendant 1 heure. Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet email.</p>" +
                    "<p style=\"color: #c9c9c9; font-size: 12px;\">© 2025 TRUE MATCH. Tous droits réservés.</p>" +
                    "</div>";

    // Modèle HTML pour l'email de bienvenue
    public static final String WELCOME_EMAIL_HTML_TEMPLATE =
            "<div style=\"max-width: 680px; margin: 0 auto; padding: 45px 30px 60px; " +
                    "background-color: #2b2d33; border-radius: 15px; text-align: center; " +
                    "color: #c9c9c9; font-family: Arial, sans-serif;\">" +
                    "<h1 style=\"margin: 0; font-size: 24px; font-weight: 500; color: #e8e8e8;\">" +
                    "Bienvenue sur TRUE MATCH, %s !</h1>" +
                    "<p style=\"margin: 20px 0; font-size: 16px; font-weight: 400; color: #c9c9c9;\">" +
                    "Nous sommes ravis de vous compter parmi nous.</p>" +
                    "<p style=\"margin: 20px 0; font-size: 14px; font-weight: 400; color: #b1b1b1;\">" +
                    "Si vous avez des questions, n'hésitez pas à contacter notre équipe de support.</p>" +
                    "<p style=\"color: #c9c9c9; font-size: 12px;\">© 2025 TRUE MATCH. Tous droits réservés.</p>" +
                    "</div>";

    /**
     * Génère le contenu de l'email OTP en remplaçant le placeholder par le code OTP.
     *
     * @param otpCode Le code OTP à insérer dans le modèle.
     * @return Le contenu de l'email formaté.
     */
    public static String getOtpEmail(String otpCode) {
        return String.format(OTP_EMAIL_TEMPLATE, otpCode);
    }

    /**
     * Génère le contenu de l'email de bienvenue en remplaçant le placeholder par le nom de l'utilisateur.
     *
     * @param userName Le nom de l'utilisateur à insérer dans le modèle.
     * @return Le contenu de l'email formaté.
     */
    public static String getWelcomeEmail(String userName) {
        return String.format(WELCOME_EMAIL_HTML_TEMPLATE, userName);
    }
}