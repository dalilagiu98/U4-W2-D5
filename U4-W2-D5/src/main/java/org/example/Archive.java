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

        for(int i = 0; i < 10; i++) {
            catalogue.add(bookSupplier.get());
            catalogue.add(magazineSupplier.get());
        }

        catalogue.forEach(System.out::println);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please for book insert title, number of page, author and genre");
        String title = scanner.nextLine();
        long pages = Long.parseLong(scanner.nextLine());
        String author = scanner.nextLine();
        String genre = scanner.nextLine();
        Book bookCreated = new Book(title, pages, author, genre);
        addElementCatalogue(bookCreated);

        catalogue.forEach(System.out::println);
        System.out.println("Please enter a isbn to remove te element:");
        long isbn = Long.parseLong(scanner.nextLine());
        try{
            removeElementByIsbn(isbn);
        } catch (Exception e) {
            System.err.println("ISBN not found! Please enter another ISBN");
        }
        catalogue.forEach(System.out::println);


    }

    //METHODS FOR ADDING AN ELEMENT IN CATALOGUE:
    public static void addElementCatalogue (BibliographicalElements element) {
        catalogue.add(element);
    }

    public static void removeElementByIsbn (long isbn) {
      catalogue =  catalogue.stream().filter(elements -> elements.getId() != isbn).collect(Collectors.toList());
    }
}
