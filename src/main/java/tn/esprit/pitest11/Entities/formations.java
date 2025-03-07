package tn.esprit.pitest11.Entities;

public class formations {
    private int id ;
    private String name;
    private String description;
    private String prix;

    public  formations(String name , String description ,String prix) {
        this.id = id;
        this.name =name;
        this.description=description;
        this.prix=prix;
    }



    public formations (int id , String name , String description , String prix) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.prix = prix;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "formations{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", prix='" + prix + '\'' +
                '}';
    }
}

