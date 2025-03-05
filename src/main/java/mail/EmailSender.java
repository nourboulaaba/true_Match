package mail;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.Session;
import jakarta.mail.PasswordAuthentication;


import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String recipient, String subject, String messageText) {
        final String senderEmail = "yasminalabella1@gmail.com";  // Remplace par ton email
        final String senderPassword = "kkbp gzha cnhz sike";   // Utilise un App Password si nécessaire

        // Configuration des propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Création de la session avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(messageText);

            // Envoi du message
            Transport.send(message);
            //System.out.println("✅ Email envoyé avec succès à " + recipient);
        } catch (MessagingException e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }
}