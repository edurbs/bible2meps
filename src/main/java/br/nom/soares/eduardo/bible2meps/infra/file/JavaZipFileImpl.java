package br.nom.soares.eduardo.bible2meps.infra.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.stereotype.Component;
import br.nom.soares.eduardo.bible2meps.application.format.ZipFile;
import br.nom.soares.eduardo.bible2meps.domain.Book;

@Component
public class JavaZipFileImpl implements ZipFile {
    public byte[] create(List<Book> books, String zipFilename) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            for (Book book : books) {
                String html = book.html();
                String htmlFilename = book.bookName().getMepsFormat() + ".html";
                addEntry(zipOutputStream, html, htmlFilename);
            }
            zipOutputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private void addEntry(ZipOutputStream zipOutputStream, String html, String htmlFilename)
            throws IOException {
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ZipEntry zipEntry = new ZipEntry(htmlFilename);
        zipOutputStream.putNextEntry(zipEntry);
        byte[] bytes = new byte[1014];
        int length;
        while ((length = inputStream.read(bytes)) >= 0) {
            zipOutputStream.write(bytes, 0, length);
        }
        inputStream.close();
    }
}
