package br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class YouVersionSite {

    public record TranslationRecord(String id, String abbreviation, String localTitle) {
    }

    public record LanguageRecord(String name, String languageTag) {
    }

    /**
     * @return Map<String languageTag, String languageName>
     */
    public List<LanguageRecord> getLanguages() {
        String jsonResponse = getJson("https://www.bible.com/api/bible/configuration");
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray defaultVersions = jsonObject
                .getAsJsonObject("response")
                .getAsJsonObject("data")
                .getAsJsonArray("default_versions");
        List<LanguageRecord> languageRecords = new ArrayList<>();
        for (JsonElement versionElement : defaultVersions) {
            JsonObject versionObject = versionElement.getAsJsonObject();
            String name = versionObject.get("name").getAsString();
            String languageTag = versionObject.get("language_tag").getAsString();
            languageRecords.add(new LanguageRecord(name, languageTag));
        }
        return languageRecords;
    }

    public List<TranslationRecord> getBibles(String languageTag) {
        /*
         * https://www.bible.com/api/bible/versions?language_tag=por&type=all
         * response{data{versions[{id, abbreviation, local_title}]}}
         */
        String jsonResponse = getJson(
                "https://www.bible.com/api/bible/versions?language_tag=" + languageTag + "&type=all");
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray versions = jsonObject
                .getAsJsonObject("response")
                .getAsJsonObject("data")
                .getAsJsonArray("versions");
        List<TranslationRecord> translationRecords = new ArrayList<>();
        for (JsonElement versionElement : versions) {
            JsonObject versionObject = versionElement.getAsJsonObject();
            String id = versionObject.get("id").getAsString();
            String abbreviation = versionObject.get("abbreviation").getAsString();
            String name = versionObject.get("local_title").getAsString();
            translationRecords.add(new TranslationRecord(id, abbreviation, name));
        }
        return translationRecords;
    }

    private String getJson(String apiUrl) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        return responseEntity.getBody();
    }

    Optional<LanguageRecord> getLanguageByTag(List<LanguageRecord> languageRecords, String languageTag) {
        return languageRecords.stream()
                .filter(language -> language.languageTag().equals(languageTag))
                .findFirst();

    }

    Optional<TranslationRecord> getTranslationById(List<TranslationRecord> translationRecords,
            String id) {
        return translationRecords.stream()
                .filter(translation -> translation.id().equals(id))
                .findFirst();
    }

}
