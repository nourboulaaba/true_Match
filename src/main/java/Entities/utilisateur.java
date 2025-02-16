package Entities;

public class utilisateur {
    private int id;
    private String lastName; // nom
    private String firstName; // prenom
    private String identifier; // identifiant
    private String email;
    private String password; // motDePasse
    private String jobPosition; // poste
    private Role role; // Utilisation de l'énumération Role
    private String employeeId; // idEmploye
    private String faceId; // faceId
    private double salary; // salaire
    private String hireDate; // dateEmbauche
    private String phoneNumber; // telephone
    private String cv; // cv
    private String profilePhoto; // photoDeProfile


    public utilisateur(String lastName, String firstName, String identifier, String email, String password, String jobPosition, Role role, String employeeId, String faceId, double salary, String hireDate, String phoneNumber, String cv, String profilePhoto) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.identifier = identifier;
        this.email = email;
        this.password = password;
        this.jobPosition = jobPosition;
        this.role = role;
        this.employeeId = employeeId;
        this.faceId = faceId;
        this.salary = salary;
        this.hireDate = hireDate;
        this.phoneNumber = phoneNumber;
        this.cv = cv;
        this.profilePhoto = profilePhoto;
    }

    public utilisateur(int id, String lastName, String firstName, String identifier, String email, String password, String jobPosition) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.identifier = identifier;
        this.email = email;
        this.password = password;
        this.jobPosition = jobPosition;
    }

    public utilisateur(String lastName, String firstName, String identifier, String email, String password, String jobPosition, Role role) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.identifier = identifier;
        this.email = email;
        this.password = password;
        this.jobPosition = jobPosition;
        this.role = role;
    }

    public utilisateur(int id, String lastName, String firstName, String identifier, String email, String password, String jobPosition, Role role, String employeeId, String faceId, double salary, String hireDate, String phoneNumber, String cv, String profilePhoto) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.identifier = identifier;
        this.email = email;
        this.password = password;
        this.jobPosition = jobPosition;
        this.role = role;
        this.employeeId = employeeId;
        this.faceId = faceId;
        this.salary = salary;
        this.hireDate = hireDate;
        this.phoneNumber = phoneNumber;
        this.cv = cv;
        this.profilePhoto = profilePhoto;
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

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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
        return "utilisateur{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", identifier='" + identifier + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", jobPosition='" + jobPosition + '\'' +
                ", role=" + role +
                ", employeeId='" + employeeId + '\'' +
                ", faceId='" + faceId + '\'' +
                ", salary=" + salary +
                ", hireDate='" + hireDate + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", cv='" + cv + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                '}';
    }
}