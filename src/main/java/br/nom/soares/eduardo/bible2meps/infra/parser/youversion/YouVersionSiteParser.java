package br.nom.soares.eduardo.bible2meps.infra.parser.youversion;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import br.nom.soares.eduardo.bible2meps.application.format.SiteParser;
import br.nom.soares.eduardo.bible2meps.domain.Language;
import br.nom.soares.eduardo.bible2meps.domain.Translation;
import br.nom.soares.eduardo.bible2meps.domain.enums.BookName;
import br.nom.soares.eduardo.bible2meps.domain.enums.YouVersionBookName;

public class YouVersionSiteParser implements SiteParser {

    @Override
    public String formatBook(List<String> urls, BookName bookName) {
        return new YouVersionFormatBook().execute(urls, bookName);
    }

    @Override
    public List<String> getUrls(BookName bookName, String id, String abbreviation) {
        List<String> urlList = new ArrayList<>();
        YouVersionBookName youVersionBookName = YouVersionBookName.fromString(bookName.name());
        // https://www.bible.com/bible/277/LEV.1.TB
        String youVersionUrl = "https://www.bible.com/bible/";
        int totalChapter = bookName.getNumberOfChapters();
        for (int chapterNumber = 1; chapterNumber <= totalChapter; chapterNumber++) {
            String finalUrl = new StringBuilder().append(youVersionUrl).append(id).append("/")
                    .append(youVersionBookName.getName()).append(".").append(chapterNumber)
                    .append(".").append(abbreviation).toString();
            urlList.add(finalUrl);
        }
        return urlList;
    }

    @Override
    public List<Language> getLanguages() {
        String jsonResponse = getJson("https://www.bible.com/api/bible/configuration");
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray defaultVersions = jsonObject.getAsJsonObject("response").getAsJsonObject("data")
                .getAsJsonArray("default_versions");
        List<Language> languages = new ArrayList<>();
        for (JsonElement versionElement : defaultVersions) {
            JsonObject versionObject = versionElement.getAsJsonObject();
            String name = versionObject.get("name").getAsString();
            String languageTag = versionObject.get("language_tag").getAsString();
            languages.add(new Language(name, languageTag));
        }
        return languages;
    }

    @Override
    public List<Translation> getBibles(String languageTag) {
        String jsonResponse = getJson("https://www.bible.com/api/bible/versions?language_tag="
                + languageTag + "&type=all");
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray versions = jsonObject.getAsJsonObject("response").getAsJsonObject("data")
                .getAsJsonArray("versions");
        List<Translation> translations = new ArrayList<>();
        for (JsonElement versionElement : versions) {
            JsonObject versionObject = versionElement.getAsJsonObject();
            String id = versionObject.get("id").getAsString();
            String abbreviation = versionObject.get("abbreviation").getAsString();
            String name = versionObject.get("local_title").getAsString();
            translations.add(new Translation(id, abbreviation, name));
        }
        return translations;
    }

    private String getJson(String apiUrl) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        return responseEntity.getBody();
    }


}
