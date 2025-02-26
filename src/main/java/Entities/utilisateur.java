package Entities;

public class utilisateur {
    private int id;
    private String lastName;
    private String firstName;
    private String identifier;
    private String email;
    private String password;
    private String CIN;
    private Role role;

    private String faceId;
    private double salary;
    private String hireDate;
    private String phoneNumber;
    private String cv;
    private String profilePhoto;
    private static utilisateur currentUtilisateur;

    // Constructeur par défaut
    public utilisateur() {}

    // Constructeur complet
    public utilisateur(String lastName, String firstName, String identifier, String email, String password,
                       String phoneNumber, String CIN, String faceId, double salary,
                       Role role, String hireDate, String cv, String profilePhoto) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.identifier = identifier;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.CIN = CIN;
        this.faceId = faceId;
        this.salary = salary;
        this.role = role;
        this.hireDate = hireDate;
        this.cv = cv;
        this.profilePhoto = profilePhoto;
    }

    // Getters et Setters avec validations
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
        if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email invalide !");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
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
        if (salary >= 0) {
            this.salary = salary;
        } else {
            throw new IllegalArgumentException("Le salaire ne peut pas être négatif !");
        }
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
        if (phoneNumber.matches("^\\d{8,15}$")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Numéro de téléphone invalide !");
        }
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

    // Méthode toString améliorée
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", identifier='" + identifier + '\'' +
                ", email='" + email + '\'' +
                ", CIN='" + CIN + '\'' +
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
