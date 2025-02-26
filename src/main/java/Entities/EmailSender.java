package Entities;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendWelcomeEmail(String toEmail) {
        // Configuration SMTP
        String host = "smtp.gmail.com"; // Serveur SMTP (ex: Gmail)
        String port = "587"; // Port SMTP (TLS)
        String fromEmail = "boulaabanour2020@gmail.com"; // Votre adresse email
        String password = "thua uyfj fekw bvnv"; // Votre mot de passe ou mot de passe d'application

        // Propriétés SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Activation de TLS

        // Création de la session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Bienvenue sur notre plateforme !");

            // Corps du message
            String emailContent = "Bonjour "  + ",\n\n"
                    + "Merci de vous être inscrit sur notre plateforme.\n"
                    + "Nous sommes ravis de vous compter parmi nous !\n\n"
                    + "Cordialement,\n"
                    + "L'équipe de support";
            message.setText(emailContent);


            // Envoi du message
            Transport.send(message);
            System.out.println("Email de bienvenue envoyé à " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'envoi de l'email de bienvenue : " + e.getMessage());
        }
    }
}