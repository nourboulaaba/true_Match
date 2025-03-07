package tn.esprit.pitest11.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private static final String URL = "jdbc:mysql://mysql-alphatech.alwaysdata.net:3306/alphatech_pi?useSSL=true&requireSSL=true";
    private static final String USERNAME = "alphatech";
    private static final String PASSWORD = "Azerty1234@";

    private Connection connection;
    private static DataSource instance;

    private DataSource() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connexion réussie !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Vérifier si la connexion est fermée et la rouvrir si nécessaire
            if (connection == null || connection.isClosed()) {
                System.out.println("🔄 Réouverture de la connexion...");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("❌ Impossible de rouvrir la connexion : " + e.getMessage());
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔴 Connexion fermée proprement.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DataSource dataSource = DataSource.getInstance();
        Connection conn = dataSource.getConnection();

        if (conn != null) {
            System.out.println("🟢 La connexion est active.");
        } else {
            System.out.println("🔴 Échec de connexion.");
        }

        dataSource.closeConnection(); // Fermer la connexion après le tn.esprit.pitest11.test
    }
}