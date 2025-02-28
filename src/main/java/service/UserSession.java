package service;

import Entities.Role;
import Entities.User;
import utils.PreferenceManager;

public class UserSession {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        checkUser();

        currentUser = user;
    }

    private static void checkUser() {
        if(currentUser == null){
            currentUser = new User();
            currentUser.setRole( Role.valueOf(PreferenceManager.getString("role", "RH")));
            currentUser.setId(PreferenceManager.getInt("id", 0));
            currentUser.setLastName(PreferenceManager.getString("lastName", "unkonw"));
            currentUser.setEmail(PreferenceManager.getString("email", "unkonw"));
            currentUser.setFirstName(PreferenceManager.getString("firstName", "unkown"));
            currentUser.setIdentifier( PreferenceManager.getString("identifier", "unkown"));
            currentUser.setPassword(PreferenceManager.getString("password", "unkown"));
            currentUser.setcin(PreferenceManager.getString("CIN","unkown"));
            currentUser.setFaceId(PreferenceManager.getString("faceId", "unkown"));
            currentUser.setSalary(PreferenceManager.getDouble("salary",0.0));
            currentUser.setHireDate(PreferenceManager.getString("hireDate", "unkown"));
            currentUser.setPhoneNumber(PreferenceManager.getString("phoneNumber", "unkown"));
            currentUser.setCv(PreferenceManager.getString("cv", "unkown"));
            currentUser.setProfilePhoto(PreferenceManager.getString("profilePhoto", "unkown"));

        }
    }

    public static User getConnectedUser() {
        checkUser();
        return currentUser;
    }


}
