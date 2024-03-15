package org.example.entities;

import java.time.LocalDate;
import java.util.Random;

public abstract class BibliographicalElements {

    //ATTRIBUTES LIST
    protected long id;
    protected String title;
    protected int publicationYear;
    protected long pagesNumber;

    //CONSTRUCTOR:

   public BibliographicalElements (String title, long pagesNumber) {
       Random random = new Random();
       this.id = random.nextLong();
       this.title = title;
       this.publicationYear = random.nextInt(1455, LocalDate.now().getYear()); //print a random number from 1455 (year of invention of printing) to actual year
       if(pagesNumber < 10){
           throw new IllegalArgumentException("YOU MUST ENTER A MINIMUM OF 10 PAGES!");
       }
       this.pagesNumber = pagesNumber;
   }


}
