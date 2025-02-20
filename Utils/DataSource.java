package tn.esprit.pitest22.Utils;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DataSource {
    private String url="jdbc:mysql://localhost:3306/pitest2";
    //jdbc driver : assure la connexion entre la base de données et l'application

    //singleton : assure une seule instance de la connexion
    //design pattern : des bouts de code qui resoudent un probleme

    private static String login = "root";
    private static String pwd = "";
    private Connection cnx;

    private static DataSource instance;

    public DataSource(){
        try {
            cnx= DriverManager.getConnection(url,login,pwd);
            System.out.println("sucess");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource getInstance(){
        if(instance==null)
            instance=new DataSource();
        return instance;
    }

    public Connection getCnx() { return cnx;}
}

