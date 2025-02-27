package Entities;

public class User {
    private int id = 0;
    private String lastName;
    private String firstName;
    private String identifier;
    private String email;
    private String password;
    private String cin;
    private Role role;
    private String faceId;
    private double salary;
    private String hireDate;
    private String phoneNumber;
    private String cv;
    private String profilePhoto;

    // Constructeur par défaut
    public User() {}

    public User(int id, String lastName, String firstName, String identifier, String email, String cin, Role role, String faceId, double salary, String hireDate, String phoneNumber, String cv, String profilePhoto, String password) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.identifier = identifier;
        this.email = email;
        this.cin = cin;
        this.role = role;
        this.faceId = faceId;
        this.salary = salary;
        this.hireDate = hireDate;
        this.phoneNumber = phoneNumber;
        this.cv = cv;
        this.profilePhoto = profilePhoto;
        this.password = password;
    }

    // Constructeur complet
    public User(String lastName, String firstName, String email, String phoneNumber, String password) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        // Vous pouvez définir des valeurs par défaut ou laisser les autres attributs comme null
        this.role = null; // Par exemple
        this.salary = 0.0;
        this.cin = "";
        this.faceId = "";
        this.hireDate = "";
        this.cv = "";
        this.profilePhoto = "";
    }


    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCin() {
        return cin;
    }

    public void setcin(String cin) {
        this.cin = cin;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", identifier='" + identifier + '\'' +
                ", email='" + email + '\'' +
                ", cin='" + cin + '\'' +
                ", role=" + role +
                ", faceId='" + faceId + '\'' +
                ", salary=" + salary +
                ", hireDate='" + hireDate + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", cv='" + cv + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }
}
