import java.io.*;
import java.util.*;

// Superklasse Person, som Friend og FamilyMember vil arve fra
class Person implements Serializable { // Serializable gør det muligt at gemme objekter i en fil
    private final String name;
    private final String phone;
    private final String email;

    // Konstruktør til initialisering af Person-objekter
    public Person(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // Getter-metoder
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // Overrider toString-metoden for at lette udskrivning
    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phone + ", Email: " + email;
    }
}

// Friend-klassen arver fra Person
class Friend extends Person {
    public Friend(String name, String phone, String email) {
        super(name, phone, email);
    }
}

// FamilyMember-klassen arver også fra Person og tilføjer en relation-attribut
class FamilyMember extends Person {
    private final String relation;

    public FamilyMember(String name, String phone, String email, String relation) {
        super(name, phone, email);
        this.relation = relation;
    }

    public String getRelation() { return relation; }

    @Override
    public String toString() {
        return super.toString() + ", Relation: " + relation;
    }
}

// Menu-klassen håndterer menuens visning
class Menu {
    private final String menuHeader;
    private final String leadText;
    private final List<String> menuItems;

    public Menu(String menuHeader, String leadText, List<String> menuItems) {
        this.menuHeader = menuHeader;
        this.leadText = leadText;
        this.menuItems = menuItems;
    }

    // Metode til at vise menuen i konsollen
    public void showMenu() {
        System.out.println(menuHeader);
        for (String item : menuItems) {
            System.out.println(item);
        }
        System.out.print(leadText);
    }
}

// Main-klassen håndterer programmets flow
public class Main {
    private List<Person> persons = new ArrayList<>(); // Liste til at gemme personer
    private final Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "persons.dat";

    public static void main(String[] args) {
        new Main().run(); // Starter programmet ved at kalde run-metoden
    }

    public void run() {
        // Oprettelse af menu
        Menu menu = new Menu("MENU", "Choose option:", Arrays.asList(
                "1. Show list of persons",
                "2. Enter new friend",
                "3. Enter new family member",
                "4. Delete person",
                "5. Save list",
                "6. Load list",
                "9. Quit"
        ));

        boolean running = true;
        while (running) {
            menu.showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Fjerner newline fra input

            switch (choice) {
                case 1 -> showList();
                case 2 -> enterNewFriend();
                case 3 -> enterNewFamilyMember();
                case 4 -> deletePerson();
                case 5 -> saveList();
                case 6 -> loadList();
                case 9 -> running = false;
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void showList() {
        if (persons.isEmpty()) {
            System.out.println("No persons in the list.");
        } else {
            for (Person p : persons) {
                System.out.println(p);
            }
        }
    }

    private void enterNewFriend() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        persons.add(new Friend(name, phone, email));
    }

    private void enterNewFamilyMember() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter relation: ");
        String relation = scanner.nextLine();
        persons.add(new FamilyMember(name, phone, email, relation));
    }

    private void deletePerson() {
        System.out.print("Enter name to delete: ");
        String name = scanner.nextLine();
        persons.removeIf(p -> p.getName().equalsIgnoreCase(name));
    }

    private void saveList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(persons);
            System.out.println("List saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving list: " + e.getMessage());
        }
    }

    private void loadList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            persons = (List<Person>) ois.readObject();
            System.out.println("List loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading list: " + e.getMessage());
        }
    }
}
