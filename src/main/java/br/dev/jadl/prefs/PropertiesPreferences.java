package br.dev.jadl.prefs;

import static java.lang.System.Logger.Level.ERROR;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

public class PropertiesPreferences extends AbstractPreferences {

    private static final System.Logger logger = System.getLogger(PropertiesPreferences.class.getCanonicalName());

    private final Properties properties;
    private final Path path;

    private String prefix = "";

    public PropertiesPreferences(final Path path, final Properties properties) {
        this(null, "", path, properties);
    }

    private PropertiesPreferences(final AbstractPreferences parent, final String name, final Path path, final Properties props) {
        super(parent, name);
        this.path = path;
        this.properties = props;

        if (this.parent() != null) {
            this.prefix = this.absolutePath()
                    .replaceFirst("/", "")
                    .replaceAll("/", ".")
                    .concat(".");
        }

        try (FileInputStream fis = new FileInputStream(this.path.toFile())) {
            this.properties.load(fis);
        } catch (final IOException e) {
            logger.log(ERROR, e.getLocalizedMessage(), e);
        }
    }

    @Override
    protected void putSpi(final String key, final String value) {
        properties.setProperty(normalize(key), value);
    }

    @Override
    protected String getSpi(final String key) {
        return properties.getProperty(normalize(key), null);
    }

    @Override
    protected void removeSpi(final String key) {
        properties.remove(normalize(key));
    }

    @Override
    protected void removeNodeSpi() {
        properties.keySet().removeIf(key -> this.denormalize(key).matches("^[^.]+$"));
    }

    @Override
    protected String[] keysSpi() {
        return properties.stringPropertyNames().stream()
                .map(this::denormalize)
                .filter(key -> key.matches("^[^.]*$"))
                .toArray(String[]::new);
    }

    @Override
    protected String[] childrenNamesSpi() {
        return this.properties.stringPropertyNames().stream()
                .map(this::denormalize)
                .filter(key -> !key.matches("^[^.]*$"))
                .map(key -> key.substring(0, key.indexOf(".")))
                .toArray(String[]::new);
    }

    @Override
    protected AbstractPreferences childSpi(final String node) {
        return new PropertiesPreferences(this, node, this.path, this.properties);
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
        try (FileInputStream fis = new FileInputStream(this.path.toFile())) {
            this.properties.load(fis);
        } catch (final IOException e) {
            logger.log(ERROR, e.getLocalizedMessage(), e);
            throw new BackingStoreException(e);
        }
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
        try (FileOutputStream fos = new FileOutputStream(this.path.toFile())) {
            properties.store(fos, "Properties preferences");
        } catch (final IOException e) {
            logger.log(ERROR, e.getLocalizedMessage(), e);
            throw new BackingStoreException(e);
        }
    }

    private String normalize(final String key) {
        return this.prefix + key;
    }

    private String denormalize(final Object key) {
        return String.valueOf(key).replaceAll(this.prefix, "");
    }
}
