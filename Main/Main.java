package tn.esprit.pitest22.Main;


import tn.esprit.pitest22.Model.Departement;
import tn.esprit.pitest22.Model.Offre;
import tn.esprit.pitest22.Model.User;
import tn.esprit.pitest22.Services.DepartementService;
import tn.esprit.pitest22.Services.OffreService;
import tn.esprit.pitest22.Utils.DataSource;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        DataSource ds = new DataSource();

        User responsable = new User(1,"test", "test");

        Departement departement = new Departement(1, "departement1", 1000, 10, responsable);
        Offre offre = new Offre(1, "titre1", "description1", 1000, 2000, departement);

        OffreService os = new OffreService();
        os.add(offre);

        Offre offre2 = new Offre(2, "titre2", "description2", 2000, 3000, departement);
        //os.update(offre2, 1);

        //os.delete(1);

        //System.out.println(os.readById(1));

        //CRUD Departement
        DepartementService depS = new DepartementService();
        depS.add(departement);




    }
}

