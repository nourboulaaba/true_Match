package tn.esprit.pitest11.Main;

import tn.esprit.pitest11.Model.*;
import tn.esprit.pitest11.Services.ConvoqueService;
import tn.esprit.pitest11.Services.EntretienService;
import tn.esprit.pitest11.Utils.DataSource;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        //DataSource ds = DataSource.getInstance();
        User u = new User(1, "user1", "admin");
        Recrutement r = new Recrutement(1, "recrutement1");

        Entretien e = new Entretien(1,"a", LocalDate.now(),1,"a",0.35,0.75);
        e.setResponsable(u);
        Offre o = new Offre(1,"aaa",r);
        Convoque c = new Convoque(1,"a","a","a",o,e);
        ConvoqueService cs = new ConvoqueService();
        EntretienService es = new EntretienService();
        cs.add(c);
        es.add(e);



    }
}
