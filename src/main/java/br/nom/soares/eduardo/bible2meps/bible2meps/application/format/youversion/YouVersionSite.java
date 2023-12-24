package br.nom.soares.eduardo.bible2meps.bible2meps.application.format.youversion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class YouVersionSite {

    /**
     * @return Map<String languageTag, String languageName>
     */
    public Map<String, String> getLanguagesMap() {
        String jsonResponse = getJsonResponse();
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray defaultVersions = jsonObject
                .getAsJsonObject("response")
                .getAsJsonObject("data")
                .getAsJsonArray("default_versions");
        Map<String, String> map = new HashMap<>();
        for (JsonElement versionElement : defaultVersions) {
            JsonObject versionObject = versionElement.getAsJsonObject();
            String name = versionObject.get("name").getAsString();
            String languageTag = versionObject.get("language_tag").getAsString();
            map.put(languageTag, name);
        }

        return map;
    }

    private String getJsonResponse() {
        /*
         * https://www.bible.com/api/bible/configuration
         * response{data{default_versions[{name, language_tag}]}}
         * response.data.default_versions.0.language_tag -> languageCode
         */
        String apiUrl = "https://www.bible.com/api/bible/configuration";
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        return responseEntity.getBody();
    }

    public List<String> listBibles(String languageTag) {
        /*
         * https://www.bible.com/api/bible/versions?language_tag=por&type=all
         * response{data{versions[{id, abbreviation, local_title}]}}
         */
        return null;
    }

}
