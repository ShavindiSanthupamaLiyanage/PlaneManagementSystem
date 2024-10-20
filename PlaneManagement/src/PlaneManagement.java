import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaneManagement {
    private static final int ROWS = 4;
    private static final int[] SEATS_PER_ROW = {14, 12, 12, 14};
    private static final int[][] seats = new int[ROWS][];
    private static final Ticket[] ticketsSold = new Ticket[ROWS * SEATS_PER_ROW[0]]; // Maximum number of tickets
    private static int ticketsSoldCount = 0;

    public static void main(String[] args) {
        for (int i = 0; i < ROWS; i++) {
            seats[i] = new int[SEATS_PER_ROW[i]];
        }

        System.out.println("Welcome to the Plane Management application");
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Buy a seat");
            System.out.println("2. Cancel a seat");
            System.out.println("3. Find the first available seat");
            System.out.println("4. Show seating plan");
            System.out.println("5. Print tickets information and total sales");
            System.out.println("6. Search Ticket");
            System.out.println("0. Quit");
            System.out.print("Select an option: ");
           try {
               option = scanner.nextInt();
               switch (option) {
                   case 1:
                       buySeat();
                       break;
                   case 2:
                       cancelSeat();
                       break;
                   case 3:
                       findFirstAvailable();
                       break;
                   case 4:
                       showSeatingPlan();
                       break;
                   case 5:
                       printTicketInfo();
                       break;
                   case 6:
                       searchTicket();
                       break;
                   case 0:
                       System.out.println("Exiting program...");
                       break;
                   default:
                       System.out.println("Invalid option. Please try again.");
               }
           } catch (java.util.InputMismatchException e){
               System.out.println("Invalid option. Please try again.");
               scanner.nextLine(); // Clear the input buffer
               option = -1; // Reset option to prevent infinite loop
           }
        } while (option != 0);
    }

    public static void buySeat() {
        Scanner scanner = new Scanner(System.in);
        char rowLetter = ' ';
        int seatNumber;

        while (true) {
            try {
                System.out.print("Enter row letter (A-D): ");
                String rowInput = scanner.next().toUpperCase();
                if (rowInput.length() != 1 || rowInput.charAt(0) < 'A' || rowInput.charAt(0) > 'D') {
                    throw new InputMismatchException("Invalid row letter.");
                } else {
                    rowLetter = rowInput.charAt(0);
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid row letter. Please enter a valid letter.");
                scanner.nextLine(); // Consume invalid input
            }
        }

        while (true) {
            System.out.print("Enter seat number: ");
            try {
                seatNumber = scanner.nextInt();
                int row = rowLetter - 'A';
                int seatIndex = seatNumber - 1;

                if (seatIndex < 0 || seatIndex >= SEATS_PER_ROW[row]) {
                    System.out.println("Invalid seat number.");
                } else {
                    if (seats[row][seatIndex] == 0) {
                        System.out.print("Enter passenger name: ");
                        String name = scanner.next();
                        System.out.print("Enter passenger surname: ");
                        String surname = scanner.next();
                        System.out.print("Enter passenger email: ");
                        String email = scanner.next();

                        int price = 0;
                        if (seatNumber >= 1 && seatNumber <= 5){
                            price = 200;
                        } else if (seatNumber >= 6 && seatNumber <= 9 ){
                            price = 150;
                        }
                        else{
                            price = 180;
                        }
                        System.out.print("The price of ticket: " + price);

                        Person person = new Person(name, surname, email);
                        Ticket ticket = new Ticket(String.valueOf(rowLetter), seatNumber, price, person);
                        ticketsSold[ticketsSoldCount++] = ticket;
                        seats[row][seatIndex] = 1;
                        System.out.println(" ");
                        System.out.println("Seat " + rowLetter + seatNumber + " has been successfully booked.");

                        //save in a file
                        ticket.saveFile();
                    } else {
                        System.out.println(" ");
                        System.out.println("Seat " + rowLetter + seatNumber + " is already booked.");
                    }
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid seat number. Please enter a valid number.");
                scanner.next(); // Consume invalid input
            }
        }
    }
    private static void cancelSeat() {
        Scanner scanner = new Scanner(System.in);
        char rowLetter = ' ';
        int seatNumber;

        while (true) {
            try {
                System.out.print("Enter row letter (A-D): ");
                String rowInput = scanner.next().toUpperCase();
                if (rowInput.length() != 1 || rowInput.charAt(0) < 'A' || rowInput.charAt(0) > 'D') {
                    throw new InputMismatchException("Invalid row letter.");
                } else {
                    rowLetter = rowInput.charAt(0);
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid row letter. Please enter a valid letter.");
                scanner.nextLine(); // Consume invalid input
            }
        }

        while (true) {
            try {
                System.out.print("Enter seat number: ");
                seatNumber = scanner.nextInt();
                int row = rowLetter - 'A';
                int seatIndex = seatNumber - 1;

                if (seatIndex < 0 || seatIndex >= SEATS_PER_ROW[row]) {
                    System.out.println("Invalid seat number.");
                } else {
                    if (seats[row][seatIndex] == 0) {
                        System.out.println("Seat " + rowLetter + seatNumber + " is not booked.");
                    } else {
                        seats[row][seatIndex] = 0;
                        for (int i = 0; i < ticketsSoldCount; i++) {
                            if (ticketsSold[i] != null && ticketsSold[i].getRow().equals(String.valueOf(rowLetter))
                                    && ticketsSold[i].getSeat() == seatNumber) {
                                ticketsSold[i].deleteFile(); // Delete the corresponding ticket file
                                ticketsSold[i] = null;
                                ticketsSoldCount--;
                                System.out.println("Seat " + rowLetter + seatNumber + " has been successfully cancelled.");
                                return;
                            }
                        }
                    }
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid seat number. Please enter a valid number.");
                scanner.next(); // Consume invalid input
            }
        }
    }



    private static void findFirstAvailable() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                if (seats[i][j] == 0) {
                    char rowLetter = (char) ('A' + i);
                    int seatNumber = j + 1;
                    System.out.println("First available seat: " + rowLetter + seatNumber);
                    return;
                }
            }
        }
        System.out.println("No available seats.");
    }

    public static void showSeatingPlan() {
        System.out.println("Seating Plan:");
        for (int i = 0; i < ROWS; i++) {
            char rowLetter = (char) ('A' + i);
            System.out.print(" " + rowLetter + " ");
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                System.out.print(seats[i][j] == 0 ? "O " : "X ");
            }
            System.out.println();
        }
    }

    private static void printTicketInfo(){
        int totalAmount = 0;
        System.out.println("**Tickets Information**");
        System.out.println(" ");
        for (int i = 0; i < ticketsSoldCount; i++) {
            Ticket ticket = ticketsSold[i];
            if (ticket != null) {
                System.out.println("Ticket for seat " + ticket.getRow() + ticket.getSeat() + ":");
                System.out.println("  Passenger: " + ticket.getPerson().getName() + " " + ticket.getPerson().getSurname());
                System.out.println("  Email: " + ticket.getPerson().getEmail());
                System.out.println("  Price: " + ticket.getPrice());
                System.out.println(" ");
                totalAmount += ticket.getPrice();
            }
        }
        System.out.println("Total Sales of tickets: " + totalAmount);
    }

    private static void searchTicket() {
        Scanner scanner = new Scanner(System.in);
        char rowLetter = ' ';
        int seatNumber;

        while (true) {
            try {
                System.out.print("Enter row letter (A-D): ");
                String rowInput = scanner.next().toUpperCase();
                if (rowInput.length() != 1 || rowInput.charAt(0) < 'A' || rowInput.charAt(0) > 'D') {
                    throw new InputMismatchException("Invalid row letter.");
                } else {
                    rowLetter = rowInput.charAt(0);
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid row letter. Please enter a valid letter.");
                scanner.nextLine(); // Consume invalid input
            }
        }

        while (true) {
            try {
                System.out.print("Enter seat number: ");
                seatNumber = scanner.nextInt();
                int row = rowLetter - 'A';
                int seatIndex = seatNumber - 1;

                if (seatIndex < 0 || seatIndex >= SEATS_PER_ROW[row]) {
                    System.out.println("Invalid seat number.");
                } else {
                    boolean ticketFound = false;
                    for (Ticket ticket : ticketsSold) {
                        if (ticket != null && ticket.getRow().equals(String.valueOf(rowLetter)) && ticket.getSeat() == seatNumber) {
                            ticket.printInformation();
                            ticketFound = true;
                            break;
                        }
                    }
                    if (!ticketFound) {
                        System.out.println("This seat is available");
                    }
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid seat number. Please enter a valid number.");
                scanner.next(); // Consume invalid input
            }
        }
    }
}

