package dev.hilligans.engine.resource;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.lang.Language;
import dev.hilligans.engine.client.graphics.api.IModel;
import dev.hilligans.engine.client.lang.Languages;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

public class ResourceManager {

    public HashMap<String, ArrayList<LanguageFile>> languageFiles = new HashMap<>();
    public HashMap<String, ArrayList<IModel>> models = new HashMap<>();

    public ArrayList<URLClassLoader> classLoaders = new ArrayList<>();
    public GameInstance gameInstance;


    public ResourceManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void setLanguageFile(String languageFile) {
        Languages.switchingLanguage.set(true);
        Future<Language> languageFuture = gameInstance.THREAD_PROVIDER.submit(() -> {
            Language language = new Language(languageFile + ".txt");
            ArrayList<LanguageFile> languageData = languageFiles.get(languageFile);
            if(languageData != null) {
                for(LanguageFile languageFile1 : languageData) {
                    language.parseLines(languageFile1.data.split("\n"));
                }
            }
            Languages.CURRENT_LANGUAGE = language;
            Languages.switchingLanguage.set(false);
            return language;
        });
    }

    public IModel getModel(String name) {
        ArrayList<IModel> models = this.models.get(name);
        if(models != null) {
            return models.get(0);
        }
        return null;
    }

    public InputStream getResource(String path) {
        InputStream stream = ResourceManager.class.getResourceAsStream("/" + path);
        if(stream == null) {
            for(URLClassLoader classLoader : classLoaders) {
                stream = classLoader.getResourceAsStream(path);
                if(stream != null) {
                    break;
                }
            }
        } else {
            return stream;
        }
        return ResourceManager.class.getResourceAsStream(path);
    }

    static class LanguageFile {

        public String data;
        public String modID;

    }
}
