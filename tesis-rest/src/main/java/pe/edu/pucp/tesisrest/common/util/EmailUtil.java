package pe.edu.pucp.tesisrest.common.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class EmailUtil {

    public static void sendEmail(String to, String subject, String content, boolean isHtml) {
        // Configuraciones del servidor SMTP
        String host = "smtp.example.com";
        final String user = "tu-email@example.com";  // Cambia esto
        final String password = "tu-contraseña";    // Cambia esto

        // Configuración de las propiedades
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        // Obtener la sesión
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            // Crear el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            if (isHtml) {
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(content, "text/html");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                message.setContent(multipart);
            } else {
                message.setText(content);
            }

            // Enviar el mensaje
            Transport.send(message);

            System.out.println("Correo enviado correctamente.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
