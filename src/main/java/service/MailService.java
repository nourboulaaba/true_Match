package service;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class MailService {
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
