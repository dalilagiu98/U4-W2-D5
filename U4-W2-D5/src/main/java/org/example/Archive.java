package org.example;

import com.github.javafaker.Faker;
import org.apache.commons.io.FileUtils;
import org.example.entities.BibliographicalElements;
import org.example.entities.Book;
import org.example.entities.Magazine;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Archive {

    //STATIC VARIABLES:
    public static List<BibliographicalElements> catalogue = new ArrayList<>();
    public static void main(String[] args) {

        //CREATION BOOKS AND MAGAZINE AND ADDING TO CATALOGUE:
        Supplier<Book> bookSupplier = () -> {
            Faker faker = new Faker(Locale.ITALY);
            Random random = new Random();
            return new Book(faker.book().title(), Math.abs(random.nextInt(10, 10000)), faker.lordOfTheRings().character(), faker.book().genre());
        };

        Supplier<Magazine> magazineSupplier = () -> {
            Faker faker = new Faker();
            Random random = new Random();
            return new Magazine(faker.commerce().productName(), Math.abs(random.nextInt(10, 200)));
        };

        for(int i = 0; i < 50; i++) {
            catalogue.add(bookSupplier.get());
        }

        for(int i = 0; i < 50; i++) {
            catalogue.add(magazineSupplier.get());
        }


        //INITIALIZING SCANNER:
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        System.out.println("---------------Hello! Welcome in EpiBooks!------------------");
        while(!exit) {
            System.out.println("Please, enter one of the characters corresponding to the following alternatives:");
            System.out.println("A - Add element on our catalogue");
            System.out.println("B - Remove an element from our catalogue");
            System.out.println("C - Search by ISBN");
            System.out.println("D - Search by year of publication");
            System.out.println("E - Search by author");
            System.out.println("F - Save your catalogue on your pc");
            System.out.println("G - Update your catalogue on our database");
            System.out.println("H - See our catalogue");
            System.out.println("0 - Exit from archive");

            String choice = scanner.nextLine().toUpperCase();

            switch(choice) {

                case "A":
                    System.out.println("What type of element you want to add? Enter B to insert a book or M to insert a magazine");
                    String answer = scanner.nextLine().toUpperCase();

                    if (answer.equals("B")) {
                        System.out.println("For adding your book in our catalogue insert a title, number of pages, author and genre:");
                        String title = scanner.nextLine();
                        long pages = Long.parseLong(scanner.nextLine());
                        String author = scanner.nextLine();
                        String genre = scanner.nextLine();
                        Book bookCreated = new Book(title, pages, author, genre);
                        addElementCatalogue(bookCreated);
                        System.out.println("Book added!");
                        catalogue.forEach(System.out::println);
                    } else if (answer.equals("M")) {
                        System.out.println("For adding your magazine in our catalogue insert a title and number of pages:");
                        String title = scanner.nextLine();
                        long pages = Long.parseLong(scanner.nextLine());
                        Magazine magazineCreated = new Magazine(title, pages);
                        addElementCatalogue(magazineCreated);
                        System.out.println("Magazine added!");
                        catalogue.forEach(System.out::println);
                    } else {
                        System.err.println("Insert a valid character!");
                    }
                    break;

                case "B":
                    System.out.println("Please enter a ISBN to remove a element:");
                    long isbn = Long.parseLong(scanner.nextLine());
                    try {
                        removeElementByIsbn(isbn);
                        System.out.println("Item correctly removed");
                        catalogue.forEach(System.out::println);
                    } catch (Exception e) {
                        System.err.println("ISBN not found! Please enter another ISBN!");
                    }
                    break;

                case "C":
                    System.out.println("Please enter a ISBN to search a book:");
                    long isbn1 = Long.parseLong(scanner.nextLine());
                    try {
                        searchByIsbn(isbn1).forEach((id, title) -> {
                            System.out.println("The ISBN is: " + id + " and the title is: " + title);
                        });
                    } catch (Exception e) {
                        System.err.println("ISBN not found! Please enter another ISBN!");
                    }
                    break;

                case "D":
                    System.out.println("Please enter a year from 1455 to today to search books:");
                    int year = Integer.parseInt(scanner.nextLine());
                    try {
                        searchByYear(year).forEach((years) -> {
                            System.out.println("Item found: " + years.getTitle());
                        });
                    } catch (Exception e) {
                        System.err.println("Year not found! Please enter another year!");
                    }
                    break;

                case "E":
                    System.out.println("Please enter a name of a author to search books:");
                    String author = capitalizeFirstLetter(scanner.nextLine());
                    try {
                        searchByAuthor(author).forEach((authors) -> {
                            System.out.println("title: " + authors.getTitle() + ", (" + authors.getPublicationYear() + ")");
                        });
                    } catch (Exception e) {
                        System.err.println("Author not found! Please enter another author!");
                    }
                    break;

                case "F":
                    System.out.println("Saving catalogue on your computer...");
                    try {
                        saveToDisk();
                        System.out.println("Catalogue saved!");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "G":
                    System.out.println("Printing your catalogue...");
                    try {
                        loadFromDisk().forEach(book -> {
                            System.out.println(book);
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case "H":
                    catalogue.forEach(System.out::println);
                    break;


                case "0":

                    exit = true;
                    break;

                default:
                    System.err.println("Invalid choice! Please enter a valid option.");
            }
        }
        System.out.println("Closing program... Goodbye!");
        scanner.close();
    }

    //METHOD FOR ADDING AN ELEMENT IN CATALOGUE:
    public static void addElementCatalogue (BibliographicalElements element) {
        catalogue.add(element);
    }

    //METHOD FOR REMOVING AN ELEMENT FROM CATALOGUE:
    public static void removeElementByIsbn (long isbn) {
      catalogue =  catalogue.stream().filter(elements -> elements.getId() != isbn).collect(Collectors.toList());
    }

    //METHOD TO SEARCH BY ISBN:
    public static Map<Long, String> searchByIsbn (long isbn) {
        return catalogue.stream().filter(element -> element.getId() == isbn).collect(Collectors.toMap(BibliographicalElements::getId, BibliographicalElements::getTitle));
    }

    //METHOD SEARCH BY YEAR:
    public static List<BibliographicalElements> searchByYear (int year) {
        return catalogue.stream().filter(element -> element.getPublicationYear() == year).collect(Collectors.toList());
    }

    //METHOD SEARCH BY AUTHOR:
    public static List<BibliographicalElements> searchByAuthor (String author) {
        return catalogue.stream().filter(element -> element instanceof Book).filter(element -> ((Book)element).getAuthor().equals(author)).collect(Collectors.toList());
    }

    //METHOD TO WRITE ON DISK:
    public static void saveToDisk () throws IOException {
        String toWrite = "";

        BibliographicalElements item;
        for(Iterator var1= catalogue.iterator(); var1.hasNext(); toWrite = toWrite + item.getTitle() + ", ISBN: " + item.getId() + '\n') {
            item = (BibliographicalElements)var1.next();
        }

        File file = new File("src/catalogue.txt");
        FileUtils.writeStringToFile(file, toWrite + System.lineSeparator(), StandardCharsets.UTF_8);
    }

    //METHOD TO LOAD FROM DISK:
    public static List<String> loadFromDisk() throws IOException {
        File file = new File("src/catalogue.txt");
        String fileString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        List<String> splitElementString = Arrays.asList(fileString);
        return splitElementString;
    }

    //METHOD CAPITALIZE FIRST LETTER OF A STRING:
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
