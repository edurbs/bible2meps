package br.nom.soares.eduardo.bible2meps.application.format;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import br.nom.soares.eduardo.bible2meps.infra.file.JavaZipFileImpl;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class FormatBibleTest {

    @Mock
    private SiteParser siteParserMock;

    @Mock
    private BibleParams bibleParamsMock;

    @Mock
    private JavaZipFileImpl zipFileMock;

    @InjectMocks
    private FormatBible formatBible;

    @Test
    void testExecute() {
        var urls = List.of(new String("some url"));
        when(siteParserMock.getUrls(Mockito.any(), eq("id"), eq("abbreviation"))).thenReturn(urls);
        when(siteParserMock.formatBook(eq(urls), Mockito.any())).thenReturn("some html");
        when(zipFileMock.create(any(), eq("abbreviation.zip"))).thenReturn(new byte[0]);
        bibleParamsMock = new BibleParams("id", "abbreviation", siteParserMock, zipFileMock);

        formatBible = new FormatBible(bibleParamsMock);
        var zip = formatBible.execute();

        assertNotNull(zip);
        verify(siteParserMock, times(66)).formatBook(eq(urls), Mockito.any());
    }
}
