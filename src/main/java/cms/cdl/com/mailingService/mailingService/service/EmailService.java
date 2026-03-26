package cms.cdl.com.mailingService.mailingService.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.util.ObjectUtils;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String smtpFromEmail;

    @Value("${default.sender.name:CMS Portal}")
    private String defaultSenderName;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an HTML email with optional CC recipients.
     *
     * @param senderName Display name for the sender.
     * @param toEmail The primary recipient email address.
     * @param subject The email subject.
     * @param htmlBody The HTML content of the email.
     * @param ccEmails Optional array of CC email addresses.
     * @return true if the email was successfully sent, false otherwise.
     */
    public boolean sendHtmlEmail(String senderName, String toEmail, String subject, String htmlBody, String... ccEmails) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        try {
            helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String finalSenderDisplayName = senderName != null && !senderName.isEmpty() ? senderName : defaultSenderName;
            helper.setFrom(smtpFromEmail, finalSenderDisplayName);

            helper.setTo(toEmail);

            // ✨ NEW: Set multiple CC recipients ✨
            if (!ObjectUtils.isEmpty(ccEmails)) { // Check if the array is not null and not empty
                helper.setCc(ccEmails);
                logger.debug("Setting CC recipients: {}", String.join(", ", ccEmails));
            } else {
                logger.debug("No CC recipients provided for email to {}", toEmail);
            }

            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
            logger.info("HTML Email sent successfully: FROM='{}' <{}>, TO={}, CC={}, SUBJECT='{}'",
                    finalSenderDisplayName, smtpFromEmail, toEmail, ObjectUtils.isEmpty(ccEmails) ? "None" : String.join(", ", ccEmails), subject);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to construct MIME message for HTML email to {}. Error: {}", toEmail, e.getMessage(), e);
            return false;
        } catch (MailException e) {
            logger.error("Failed to send HTML email to {} (CC: {}). SMTP Error: {}", toEmail, (ObjectUtils.isEmpty(ccEmails) ? "None" : String.join(", ", ccEmails)), e.getMessage(), e);
            return false;
        } catch (UnsupportedEncodingException e) {
            logger.error("Unsupported encoding for sender name '{}' in HTML email to {}. Error: {}", senderName, toEmail, e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while sending HTML email to {}. Error: {}", toEmail, e.getMessage(), e);
            return false;
        }
    }
}