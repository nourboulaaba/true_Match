package service;

public class AuthService {
    UserService userService = new UserService();
    public void logout() {
      UserSession.setCurrentUser(null);
        System.out.println("go to login");
    }

    public void ForgotPassword(String email) {
        if (userService.emailExists(email)) {
            String temPassword = OTPService.generateOTP(email, 8);
            OTPService.sendOTP(email, temPassword);
        }
    }
}
