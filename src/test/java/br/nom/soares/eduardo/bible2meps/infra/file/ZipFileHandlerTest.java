package br.nom.soares.eduardo.bible2meps.infra.file;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.nom.soares.eduardo.bible2meps.domain.Book;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;

public class ZipFileHandlerTest {
    @Test
    void createZipFile_shouldReturnZipFileByteArrayWithTwoFilesAndContents() {
        // Arrange
        JavaZipFileImpl zipFileHandler = new JavaZipFileImpl();
        List<Book> books = new ArrayList<>();

        // Add sample books to the list
        var book1 = new Book(BookName._01_GEN, "some html");
        var book2 = new Book(BookName._02_EXO, "some html");
        books.add(book1);
        books.add(book2);

        // Act
        byte[] zipFileBytes = zipFileHandler.create(books, "test.zip");

        // Assert
        assertNotNull(zipFileBytes);
        assertTrue(zipFileBytes.length > 0);

        // Extract the zip file
        try (ZipInputStream zipInputStream =
                new ZipInputStream(new ByteArrayInputStream(zipFileBytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.equals(book1.bookName().getMepsFormat() + ".html")) {
                    // Verify if the contents of book1.html match the expected value
                    Path tempFile = Files.createTempFile("temp", ".html");
                    Files.copy(zipInputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                    String contents = Files.readString(tempFile);
                    Assertions.assertEquals("some html", contents);
                } else if (entryName.equals(book2.bookName().getMepsFormat() + ".html")) {
                    // Verify if the contents of book2.html match the expected value
                    Path tempFile = Files.createTempFile("temp", ".html");
                    Files.copy(zipInputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
                    String contents = Files.readString(tempFile);
                    Assertions.assertEquals("some html", contents);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
