package service;

public class MailConfig {
    public static final String FROM_EMAIL = "Fdhila.Donia@esprit.tn"; // Mettre directement l'email ici
    public static final String PASSWORD = "14062024Yassine"; // ⚠️ Attention : ne pas exposer le mot de passe dans le code
    public static final String HR_EMAIL = "rhtruematch@gmail.com";       // Email du service RH
    public static final String SMTP_HOST = "smtp.office365.com";
    public static final String SMTP_PORT = "587";
    static {
        if (FROM_EMAIL == null || FROM_EMAIL.isEmpty()) {
            throw new IllegalStateException("L'adresse email d'expédition est manquante !");
        }
        if (PASSWORD == null || PASSWORD.isEmpty()) {
            throw new IllegalStateException("Le mot de passe email est manquant !");
        }
        if (HR_EMAIL == null || HR_EMAIL.isEmpty()) {
            throw new IllegalStateException("L'adresse email du service RH est manquante !");
        }
    }

}

