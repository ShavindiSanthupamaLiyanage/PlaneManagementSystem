import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Ticket {
    public String row;
    public int seat;
    public double price;
    public Person person;

    public Ticket(String row, int seat, double price, Person person){
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    public String getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    public double getPrice() {
        return price;
    }

    public Person getPerson() {
        return person;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void printInformation(){
        System.out.println("Ticket Information: " );
        System.out.println("Row Letter: " + row);
        System.out.println("Seat Number: " + seat);
        System.out.println("Booked Seat: " + row + seat );
        System.out.println("Price: " + price);
        System.out.println("Personal Information: " );
        person.printInformation();
    }

    public void saveFile() {
        String filename = row + seat + ".txt";

        try (FileWriter file = new FileWriter(filename)) {
            file.write("Ticket Information:\n");
            file.write("Row  : " + row + "\n");
            file.write("Seat : " + seat + "\n");
            file.write("Price: " + price + "\n");
            file.write("Passenger Information:\n");
            file.write("Name   : " + person.getName() + "\n");
            file.write("Surname: " + person.getSurname() + "\n");
            file.write("Email  : " + person.getEmail() + "\n");

            System.out.println("Ticket information saved to " + filename);
        } catch (IOException e) {
            System.out.println("An error occurred while saving ticket information to file.");
            e.printStackTrace();
        }
    }

    public void deleteFile() {
        String filename = row + seat + ".txt";
        File file = new File(filename);

        try {
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("File " + filename + " deleted successfully.");
                } else {
                    System.out.println("Failed to delete file " + filename);
                }
            } else {
                System.out.println("File " + filename + " does not exist.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting file " + filename);
            e.printStackTrace();
        }
    }
}
