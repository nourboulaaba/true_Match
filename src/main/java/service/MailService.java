package service;

import utils.Constants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailService {
    private static final String FROM_EMAIL = "boulaabanour2020@gmail.com"; // Change this to your email
    private static final String PASSWORD = "thua uyfj fekw bvnv"; // Change this to your email password

    public static void send(String recipientEmail, String generatedCode) throws MessagingException {
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");



        // Set up authentication
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        };

        // Create a session
        Session session = Session.getInstance(properties, authenticator);
        session.setDebug(true);

        // Create MimeMessage
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Password Reset Code");

        // Create MimeBodyPart
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(Constants.getOtpEmail(generatedCode), "text/html");

        // Create Multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        // Set content
        message.setContent(multipart);

        // Send the message
        Transport.send(message);
    }
    public static void sendLeaveRequestNotification(String employeeName, String leaveType, String startDate, String endDate) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", MailConfig.SMTP_HOST);
            properties.put("mail.smtp.port", MailConfig.SMTP_PORT);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(MailConfig.FROM_EMAIL, MailConfig.PASSWORD);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MailConfig.FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MailConfig.HR_EMAIL));
            message.setSubject("Nouvelle demande de congé");

            String emailContent = "<html><body>"
                    + "<h3>Une nouvelle demande de congé a été soumise</h3>"
                    + "<p><strong>Employé :</strong> " + employeeName + "</p>"
                    + "<p><strong>Type de congé :</strong> " + leaveType + "</p>"
                    + "<p><strong>Début :</strong> " + startDate + "</p>"
                    + "<p><strong>Fin :</strong> " + endDate + "</p>"
                    + "<p>Veuillez consulter le système pour plus de détails.</p>"
                    + "</body></html>";

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(emailContent, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email envoyé avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}