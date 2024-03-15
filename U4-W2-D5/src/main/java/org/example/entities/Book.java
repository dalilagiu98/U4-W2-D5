package org.example.entities;

public class Book extends BibliographicalElements{

    //ATTRIBUTES LIST:
    private String author;
    private String genre;

    //CONSTRUCTOR:
    public Book(String title, long pagesNumber, String author, String genre) {
        super(title, pagesNumber);
        this.author = author;
        this.genre = genre;
    }
}
