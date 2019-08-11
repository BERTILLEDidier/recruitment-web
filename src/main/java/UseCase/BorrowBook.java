package UseCase;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.library.HasLateBooksException;
import fr.d2factory.libraryapp.library.Library;
import fr.d2factory.libraryapp.library.NoAvailableException;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Riparian;
import fr.d2factory.libraryapp.member.Student;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BorrowBook implements Library {

    private BookRepository bibliotheque;

    public BorrowBook(BookRepository bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
        if (member == null) try {
            throw new Exception("vous n'etes pas adherent");
        } catch (Exception ex) {
            Logger.getLogger(BorrowBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (member.getListBook().isEmpty()) {
            if (this.bibliotheque.findBook(isbnCode) != null) {
                member.addBook(this.bibliotheque.findBook(isbnCode));
                Book aux = this.bibliotheque.findBook(isbnCode);
                this.bibliotheque.saveBookBorrow(this.bibliotheque.findBook(isbnCode), borrowedAt);
                return aux;
            }
        }
        member.getListBook().stream().map(a -> this.bibliotheque.findBorrowedBookDate(a)).mapToLong(aux -> aux.until(LocalDate.now(), ChronoUnit.DAYS)).filter(borrow -> member.isTooLate((int) borrow)).forEachOrdered(borrow -> {
            throw new HasLateBooksException();
        });
        throw new NoAvailableException();
    }

    @Override
    public void returnBook(Book book, Member member) {
        if (this.bibliotheque.findBorrowedBookDate(book) != null) {
            if (member instanceof Student) {
                LocalDate borrow = this.bibliotheque.findBorrowedBookDate(book);
                long aux = borrow.until(LocalDate.now(), ChronoUnit.DAYS);
                member.payBook((int) aux);
            }
            if (member instanceof Riparian) {
                LocalDate borrow = this.bibliotheque.findBorrowedBookDate(book);
                long aux = borrow.until(LocalDate.now(), ChronoUnit.DAYS);
                member.payBook((int) aux);
            }
            member.deleteBook(book);
            this.bibliotheque.saveBookAvailable(book);
        } else {
            try {
                throw new Exception("le livre est libre");
            } catch (Exception ex) {
                Logger.getLogger(BorrowBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
