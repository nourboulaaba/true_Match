// Classe Animal
class Animal {
    String family;
    String name;
    int age;
    boolean isMammal;

    public Animal(String family, String name, int age, boolean isMammal) {
        this.family = family;
        this.name = name;
        this.age = age;
        this.isMammal = isMammal;
    }

    public void afficherInfo() {
        System.out.println("Nom: " + name);
        System.out.println("Famille: " + family);
        System.out.println("Âge: " + age);
        System.out.println("Est-ce un mammifère? " + (isMammal ? "Oui" : "Non"));
    }
}

// Classe Zoo
class Zoo {
    Animal[] animals;
    String name;
    Animal[] aquaticAnimals;
    String city;
    int nbrCages;

    public Zoo(String name, String city, int nbrCages, int maxAnimals) {
        this.name = name;
        this.city = city;
        this.nbrCages = nbrCages;
        this.animals = new Animal[maxAnimals];
        this.aquaticAnimals = new Animal[10];
    }

    public void ajouterAnimal(Animal animal, int index) {
        if (index < animals.length) {
            animals[index] = animal;
            System.out.println("Animal ajouté : " + animal.name);
        } else {
            System.out.println("Impossible d'ajouter l'animal, le zoo est plein.");
        }
    }

    public void afficherInfoZoo() {
        System.out.println("Nom du Zoo: " + name);
        System.out.println("Ville: " + city);
        System.out.println("Nombre de cages: " + nbrCages);
    }
}

// Classe principale
public class Main {
    public static void main(String[] args) {
        ZooManagement zm = new ZooManagement();

        Scanner sc = new Scanner(System.in);
        System.out.println("Insert the number of cages");

        int numberOfCages = sc.nextInt();

        System.out.println("Insert the name of the zoo");

        String nameOfZoo = sc.next();

        zm.nbrCages = numberOfCages;
        zm.zooName = nameOfZoo;

        zm.displayInformation();
        Animal lion = new Animal("Felidae", "Lion", 5, true);
        Zoo myZoo = new Zoo("Parc Animalier", "Paris", 15, 25);
        myZoo.ajouterAnimal(lion, 0);
        System.out.println("\nInformations sur le Zoo:");
        myZoo.afficherInfoZoo();
        System.out.println("\nInformations sur l'Animal:");
        lion.afficherInfo();
    }
}
