package org.example;

import com.github.javafaker.Faker;
import org.example.entities.BibliographicalElements;
import org.example.entities.Book;
import org.example.entities.Magazine;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Archive {

    //STATIC VARIABLES:
    public static List<BibliographicalElements> catalogue = new ArrayList<>();
    public static void main(String[] args) {

        //CREATION BOOKS:
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

        for(int i = 0; i < 101; i++) {
            catalogue.add(bookSupplier.get());
            catalogue.add(magazineSupplier.get());
        }

        catalogue.forEach(System.out::println);

        Scanner scanner = new Scanner(System.in);
//        System.out.println("Please for book insert title, number of page, author and genre");
//        String title = scanner.nextLine();
//        long pages = Long.parseLong(scanner.nextLine());
//        String author = scanner.nextLine();
//        String genre = scanner.nextLine();
//        Book bookCreated = new Book(title, pages, author, genre);
//        addElementCatalogue(bookCreated);

//        catalogue.forEach(System.out::println);
//        System.out.println("Please enter a isbn to remove te element:");
//        long isbn = Long.parseLong(scanner.nextLine());
//        try{
//            removeElementByIsbn(isbn);
//        } catch (Exception e) {
//            System.err.println("ISBN not found! Please enter another ISBN");
//        }

//        System.out.println("Please enter a ISBN to search a book:");
//        long isbn = Long.parseLong(scanner.nextLine());
//        searchByIsbn(isbn).forEach((id, title) -> {
//            System.out.println("The ISBN is: " + id + " and the title is: " + title);
//        });

        System.out.println("Please enter a year to search a book:");
        int year = Integer.parseInt(scanner.nextLine());
        searchByYear(year).forEach((years) -> {
            System.out.println("Item found: " + years.getTitle());
        });

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
}
