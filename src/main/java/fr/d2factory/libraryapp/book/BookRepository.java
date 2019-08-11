package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    public void addBooks(List<Book> books) {
        books.forEach(a -> availableBooks.put(a.isbn, a));
    }

    public Book findBook(long isbnCode) {
        return availableBooks.entrySet().stream().filter(entry -> entry.getKey().isbnCode == isbnCode).findFirst().map(Map.Entry::getValue).orElse(null);

    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt) {
        borrowedBooks.put(book, borrowedAt);
        availableBooks.remove(book.isbn);
    }

    public LocalDate findBorrowedBookDate(Book book) {
        return borrowedBooks.entrySet().stream().filter(entry -> entry.getKey() == book).findFirst().map(Map.Entry::getValue).orElse(null);

    }
    public void saveBookAvailable(Book book) {
        availableBooks.put(book.isbn, book);
        borrowedBooks.remove(book);
    }
}
