package br.dev.jadl.prefs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

public class PropertiesPreferencesFactory implements PreferencesFactory {

    @Override
    public Preferences systemRoot() {
        return SystemPreference.instance;
    }

    @Override
    public Preferences userRoot() {
        return UserPreference.instance;
    }

    private static class UserPreference {
        private static final Preferences instance = preferences("user");
    }

    private static class SystemPreference {
        private static final Preferences instance = preferences("system");
    }

    private static Preferences preferences(final String scope) {
        final String prefix = PropertiesPreferences.class.getCanonicalName();
        final String filename = property(prefix, scope, "filename", "prefs.properties");
        if (!Files.exists(Path.of(filename))) {
            try {
                Files.createFile(Path.of(filename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new PropertiesPreferences(Path.of(filename), new Properties());
    }

    private static String property(final String prefix, final String scope, final String key, final String def) {
        final String scoped = String.format("%s.%s.%s", prefix, scope, key);
        final String unscoped = String.format("%s.%s", prefix, key);
        return System.getProperty(scoped, System.getProperty(unscoped, def));
    }
}
