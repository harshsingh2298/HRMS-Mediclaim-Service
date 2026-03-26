package cms.cdl.com.mailingService.mailingService.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for generating HTML email templates for celebration wishes.
 */
@Service
public class TamplateService {

    private static final Logger logger = LoggerFactory.getLogger(TamplateService.class);

    // Common image URL placeholder (replace with your actual hosted image URL)
    // For a real application, consider hosting these images on a CDN or within your app's public folder
    // and referencing them. For demo, using a placeholder.
    private static final String DEFAULT_LOGO_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRk7gHCcBMSDOOUPzzViQn6oPl6NrvScwbCjQ&s"; // Replace with your company logo
    private static final String DEFAULT_BIRTHDAY_IMAGE_URL = "https://b3075642.smushcdn.com/3075642/wp-content/uploads/Birthday-Cake-1.png?lossy=1&strip=1&webp=1"; // Example B-day image
    private static final String DEFAULT_ANNIVERSARY_IMAGE_URL = "https://www.shutterstock.com/image-photo/work-anniversary-card-offices-companies-260nw-2357221435.jpg"; // Example Anniversary image

    private static final String DEFAULT_SUGGESTION_IMAGE_URL = "https://t4.ftcdn.net/jpg/04/45/10/59/360_F_445105940_WdQVlnbBREQQrm7XcTYO4hSGOh0SSPU3.jpg"; // Example: Lightbulb for suggestion
    private static final String DEFAULT_IDEA_IMAGE_URL = "https://www.shutterstock.com/image-photo/creative-light-bulb-explodes-colorful-600nw-2536939495.jpg";     // Example: Brainstorming/idea diagram
    @Value("${default.sender.name:CMS Portal}")
    private String defaultSenderName; // Injected from application.properties

    /**
     * Generates an HTML template for a birthday wish.
     * @param recipientFullName The full name of the birthday person.
     * @param senderName The name of the sender.
     * @param messageBody The custom message body from the frontend.
     * @return HTML string of the birthday wish email.
     */
    public String buildBirthdayTemplate(String recipientFullName, String senderName, String messageBody) {
        logger.debug("Building birthday template for {}", recipientFullName);
        String finalSenderName = senderName != null && !senderName.isEmpty() ? senderName : defaultSenderName;

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "    <div style='background-color: #DC3545; padding: 20px; text-align: center;'>" +
                "      <img src='" + DEFAULT_LOGO_URL + "' alt='Company Logo' style='max-width: 150px; height: auto; border-radius: 5px;'>" +
                "      <h1 style='color: #ffffff; margin: 0; font-size: 28px;'>Happy Birthday!</h1>" +
                "    </div>" +
                "    <div style='padding: 30px;'>" + // Removed text-align: center from this div
                "      <img src='" + DEFAULT_BIRTHDAY_IMAGE_URL + "' alt='Birthday Celebration' style='max-width: 100%; height: auto; border-radius: 8px; margin-bottom: 20px;'>" +
                "      <h2 style='color: #333333; font-size: 24px; margin-bottom: 15px; text-align: left;'>Dear " + recipientFullName + ",</h2>" + // Added text-align: left
                "      <p style='color: #555555; font-size: 16px; line-height: 1.6; margin-bottom: 20px; text-align: left;'>" +
                messageBody +
                "      </p>" +
                "      <p style='color: #555555; font-size: 16px; line-height: 1.6; text-align: left; margin-top: 30px;'>" +
                "        Best wishes,<br/>" +
                finalSenderName +
                "      </p>" +
                "    </div>" +
                "    <div style='background-color: #eeeeee; padding: 20px; text-align: center; font-size: 12px; color: #777777;'>" +
                "      <p>&copy; " + java.time.Year.now().getValue() + " " + (finalSenderName.equals(defaultSenderName) ? "Your Company Name" : finalSenderName) + ". All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Generates an HTML template for a work anniversary wish.
     * @param recipientFullName The full name of the employee.
     * @param senderName The name of the sender.
     * @param yearsOfService The number of years for the anniversary.
     * @param messageBody The custom message body from the frontend.
     * @return HTML string of the anniversary wish email.
     */
    public String buildAnniversaryTemplate(String recipientFullName, String senderName, Integer yearsOfService, String messageBody) {
        logger.debug("Building anniversary template for {}", recipientFullName);
        String finalSenderName = senderName != null && !senderName.isEmpty() ? senderName : defaultSenderName;
        String yearsText = (yearsOfService != null && yearsOfService > 0) ? "your " + yearsOfService + (yearsOfService == 1 ? "st" : "th") + " Work Anniversary" : "your Work Anniversary";

        // --- NEW LOGIC TO REMOVE THE EMOJI LINE FROM messageBody ---
        String cleanedMessageBody = messageBody;
        String unwantedPrefix = "\uD83C\uDF89 Congratulations " + recipientFullName + "! " + yearsOfService + " year" + (yearsOfService == 1 ? "" : "s") + " completed at work!";

        // Handle variations in the "year" vs "years" part
        String unwantedPrefixSingular = "\uD83C\uDF89 Congratulations " + recipientFullName + "! 1 year completed at work!";
        String unwantedPrefixPlural = "\uD83C\uDF89 Congratulations " + recipientFullName + "! " + yearsOfService + " years completed at work!";


        if (cleanedMessageBody.startsWith(unwantedPrefixSingular)) {
            cleanedMessageBody = cleanedMessageBody.substring(unwantedPrefixSingular.length()).trim();
        } else if (cleanedMessageBody.startsWith(unwantedPrefixPlural)) {
            cleanedMessageBody = cleanedMessageBody.substring(unwantedPrefixPlural.length()).trim();
        }
        // --- END NEW LOGIC ---

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "    <div style='background-color: #28a745; padding: 20px; text-align: center;'>" +
                "      <img src='" + DEFAULT_LOGO_URL + "' alt='Company Logo' style='max-width: 150px; height: auto; border-radius: 5px;'>" +
                "      <h1 style='color: #ffffff; margin: 0; font-size: 28px;'>Happy Work Anniversary!</h1>" +
                "    </div>" +
                "    <div style='padding: 30px;'>" +
                "      <img src='" + DEFAULT_ANNIVERSARY_IMAGE_URL + "' alt='Anniversary Celebration' style='max-width: 100%; height: auto; border-radius: 8px; margin-bottom: 20px;'>" +
                "      <h2 style='color: #333333; font-size: 24px; margin-bottom: 15px; text-align: left;'>Dear " + recipientFullName + ",</h2>" +
                "      <p style='color: #555555; font-size: 16px; line-height: 1.6; margin-bottom: 20px; text-align: left;'>" +
                cleanedMessageBody + // Use the cleaned message body here
                "      </p>" +
                "      <p style='color: #555555; font-size: 16px; line-height: 1.6; text-align: left; margin-top: 30px;'>" +
                "        Best regards,<br/>" +
                finalSenderName +
                "      </p>" +
                "    </div>" +
                "    <div style='background-color: #eeeeee; padding: 20px; text-align: center; font-size: 12px; color: #777777;'>" +
                "      <p>&copy; " + java.time.Year.now().getValue() + " " + (finalSenderName.equals(defaultSenderName) ? "Your Company Name" : finalSenderName) + ". All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Generates an HTML template for a general suggestion.
     * @param senderName The name of the sender (employee).
     * @param messageBody The suggestion text.
     * @return HTML string of the suggestion email.
     */
    public String buildSuggestionTemplate(String senderName, String messageBody) {
        logger.debug("Building suggestion template from {}", senderName);
        String finalSenderName = senderName != null && !senderName.isEmpty() ? senderName : defaultSenderName;

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "    <div style='background-color: #007bff; padding: 20px; text-align: center;'>" + // Blue header for suggestions
                "      <img src='" + DEFAULT_LOGO_URL + "' alt='Company Logo' style='max-width: 150px; height: auto; display: block; margin: 0 auto 10px;'>" +
                "      <h1 style='color: #ffffff; margin: 0; font-size: 28px;'>New Employee Suggestion</h1>" +
                "    </div>" +
                "    <div style='padding: 30px; text-align: center;'>" +
                "      <img src='" + DEFAULT_SUGGESTION_IMAGE_URL + "' alt='Suggestion Idea' style='max-width: 100%; height: auto; border-radius: 8px; margin-bottom: 20px; display: block; margin-left: auto; margin-right: auto;'>" +
                "      <h2 style='color: #333333; font-size: 24px; margin-bottom: 15px;'>Dear CEO,</h2>" +
                "      <p style='color: #555555; font-size: 16px; line-height: 1.6; margin-bottom: 20px; text-align: left; white-space: pre-wrap;'>" +
                "An employee has submitted the following suggestion:<br/><br/>" +
                messageBody +
                "      </p>" +
                "      <p style='color: #555555; font-size: 16px; line-height: 1.6; text-align: left; margin-top: 30px;'>" +
                "        Regards,<br/>" +
                finalSenderName +
                "      </p>" +
                "    </div>" +
                "    <div style='background-color: #eeeeee; padding: 20px; text-align: center; font-size: 12px; color: #777777;'>" +
                "      <p>&copy; " + java.time.Year.now().getValue() + " " + (finalSenderName.equals(defaultSenderName) ? "Your Company Name" : finalSenderName) + ". All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Generates an HTML template for an "Ideas That Xcite" submission.
     * @param senderName The name of the sender (employee).
     * @param messageBody The idea text.
     * @return HTML string of the idea email.
     */
    public String buildIdeaTemplate(String senderName, String messageBody) {
        logger.debug("Building idea template from {}", senderName);
        String finalSenderName = senderName != null && !senderName.isEmpty() ? senderName : defaultSenderName;

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                "  <div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>" +
                "    <div style='background-color: #6f42c1; padding: 20px; text-align: center;'>" + // Purple header for ideas
                "      <img src='" + DEFAULT_LOGO_URL + "' alt='Company Logo' style='max-width: 150px; height: auto; display: block; margin: 0 auto 10px;'>" +
                "      <h1 style='color: #ffffff; margin: 0; font-size: 28px;'>New Idea Submitted!</h1>" +
                "    </div>" +
                "    <div style='padding: 30px; text-align: center;'>" +
                "      <img src='" + DEFAULT_IDEA_IMAGE_URL + "' alt='New Idea' style='max-width: 100%; height: auto; border-radius: 8px; margin-bottom: 20px; display: block; margin-left: auto; margin-right: auto;'>" +
                "      <h2 style='color: #333333; font-size: 24px; margin-bottom: 15px;'>Dear Happitude Team,</h2>" +
                "      <p style='color: #555555; font-size: 16px; line-height: 1.6; margin-bottom: 20px; text-align: left; white-space: pre-wrap;'>" +
                "An employee has submitted the following idea:<br/><br/>" +
                messageBody +
                "      </p>" +
                "      <p style='color: #555555; font-size: 16px; line-height: 1.6; text-align: left; margin-top: 30px;'>" +
                "        Best regards,<br/>" +
                finalSenderName +
                "      </p>" +
                "    </div>" +
                "    <div style='background-color: #eeeeee; padding: 20px; text-align: center; font-size: 12px; color: #777777;'>" +
                "      <p>&copy; " + java.time.Year.now().getValue() + " " + (finalSenderName.equals(defaultSenderName) ? "Your Company Name" : finalSenderName) + ". All rights reserved.</p>" +
                "    </div>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }
}

