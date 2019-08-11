package fr.d2factory.libraryapp.library;

import UseCase.BorrowBook;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Riparian;
import fr.d2factory.libraryapp.member.Student;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;

    Book a = new Book("Harry Potter", "J.K. Rowling", new ISBN(46578964513L));
    Book b = new Book("Around the world in 80 days", "Jules Verne", new ISBN(3326456467846l));
    Book c = new Book("Catch 22", "Joseph Heller", new ISBN(968787565445L));
    Book d = new Book("La peau de chagrin", "Balzac", new ISBN(465789453149L));

    @Before
    public void setup(){
        //TODO instantiate the library and the repository
            bookRepository = new BookRepository();
        library = new BorrowBook(bookRepository);
        //TODO add some test books (use BookRepository#addBooks)
        //TODO to help you a file called books.json is available in src/test/resources
        List<Book> bibli = new ArrayList<Book>();
        bibli.add(a);
        bibli.add(b);
        bibli.add(c);
        bibli.add(d);
        bookRepository.addBooks(bibli);

    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available()throws Exception{
        Member didier = new Riparian();
        LocalDate today = LocalDate.now();
        long test = 46578964513L;
        Book result = library.borrowBook(test, didier, today);
        Assert.assertEquals(result, a);
    }

    @Test(expected = NoAvailableException.class)
    public void borrowed_book_is_no_longer_available()throws Exception {
        Member zach = new Riparian();
        LocalDate today = LocalDate.now();
        long test = 46578964513L;
        library.borrowBook(test, zach, today);
        library.borrowBook(test, zach, today);
    }
    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() throws Exception {
        Member adam = new Riparian();
        adam.setWallet(2);
        LocalDate borrow = LocalDate.now().minusDays(20);
        long test = 968787565445L;
        Book result = library.borrowBook(test, adam, borrow);
        library.returnBook(result, adam);
        Assert.assertEquals(0, adam.getWallet(), 0.01);
    }

    @Test
    public void students_pay_10_cents_the_first_30days() throws Exception {
        Member maxime = new Student(false);
        maxime.setWallet(3);
        LocalDate borrow = LocalDate.now().minusDays(30);
        long test = 968787565445L;
        Book result = library.borrowBook(test, maxime, borrow);
        library.returnBook(result, maxime);
        Assert.assertEquals(0, maxime.getWallet(), 0.01);
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days() throws Exception {
        Member minette = new Student(true);
        minette.setWallet(3);
        LocalDate borrow = LocalDate.now().minusDays(15);
        long test = 968787565445L;
        Book result = library.borrowBook(test, minette, borrow);
        library.returnBook(result, minette);
        Assert.assertEquals(3, minette.getWallet(), 0.01);
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() throws Exception {
        Member axel = new Student(false);
        axel.setWallet(10);
        LocalDate borrow = LocalDate.now().minusDays(40);
        long test = 968787565445L;
        Book result = library.borrowBook(test, axel, borrow);
        library.returnBook(result, axel);
        Assert.assertEquals(4.5, axel.getWallet(), 0.01);
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() throws Exception {
        Member youn = new Riparian();
        youn.setWallet(10);
        LocalDate borrow = LocalDate.now().minusDays(65);
        long test = 968787565445L;
        Book result = library.borrowBook(test, youn, borrow);
        library.returnBook(result, youn);
        Assert.assertEquals(2.5, youn.getWallet(), 0.01);
    }

    @Test(expected = HasLateBooksException.class)
    public void members_cannot_borrow_book_if_they_have_late_books() throws Exception {
        Member rami = new Riparian();
        rami.setWallet(10);
        LocalDate borrow = LocalDate.now().minusDays(65);
        long test = 968787565445L;
        library.borrowBook(test, rami, borrow);
        library.borrowBook(test, rami, borrow);
    }
}
