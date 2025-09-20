package dev.hilligans.engine.client.lang;

import dev.hilligans.engine.GameInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Languages {

    public static Language CURRENT_LANGUAGE = new Language("English.txt");
    public static ArrayList<String> languages = new ArrayList<>();
    public static HashMap<String,String> mappedNames = new HashMap<>();
    public static AtomicBoolean switchingLanguage = new AtomicBoolean(false);

    public static String getTranslated(String string) {
        if(CURRENT_LANGUAGE != null) {
            return CURRENT_LANGUAGE.getTranslated(string);
        }
        return string;
    }

    public static void setCurrentLanguage(GameInstance gameInstance, String languageName) {
        gameInstance.RESOURCE_MANAGER.setLanguageFile(languageName);
    }

    private static void addLanguage(String language, String name) {
        languages.add(language);
        mappedNames.put(language,name);
    }

    static {
        addLanguage("English","English (US)");
        addLanguage("British.txt", "English (United Kingdom)");
        addLanguage("Australian", "English (Australia)");
        addLanguage("Lolcat", "LOLCAT");
        addLanguage("French","Francais");
        addLanguage("Vietnamese","Tiếng Việt");
        addLanguage("Japanese","日本語");
        addLanguage("Swedish","Svenska");
        Collections.sort(languages);
    }

}
