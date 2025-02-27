package utils;

public class Constants {
    public static final String OTP_EMAIL_TEMPLATE =
            "<div style=\"max-width: 680px; margin: 0px auto; padding: 45px 30px 60px; " +
                    "background-color: rgb(43, 45, 51) !important; border-radius: 15px; text-align: center; " +
                    "color: rgb(201, 201, 201) !important;\">" +
                    "<h1 style=\"margin: 0px; font-size: 24px; font-weight: 500; color: rgb(232, 232, 232) !important;\">" +
                    "Your OTP for Password Reset</h1>" +
                    "<p style=\"margin: 20px 0px; font-size: 16px; font-weight: 400; color: rgb(201, 201, 201) !important;\">" +
                    "Please use the following OTP to reset your password:</p>" +
                    "<h2 style=\"margin:20px 0; font-size:32px; font-weight:600; color:#4caf50\">%s</h2>" +
                    "<p style=\"margin: 20px 0px; font-size: 14px; font-weight: 400; color: rgb(177, 177, 177) !important;\">" +
                    "This OTP is valid for 1 hour. If you did not request this, please ignore this email.</p>" +
                    "<p style=\"color: rgb(201, 201, 201) !important;\">© 2025 TRUE MATCH. All rights reserved.</p>" +
                    "</div>";

    public static String getOtpEmail(String otpCode) {
        return String.format(OTP_EMAIL_TEMPLATE, otpCode);
    }
}
