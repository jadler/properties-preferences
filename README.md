# **Properties Preferences API**

This project implements the java `java.util.prefs.Preferences` API using as BackingStorage `java.util.Properties`.
The implementation allows preferences to be stored in local `.properties` files, providing a persistent solution 
for system and user preferences.


## **Features**

- Stores and retrieves preferences in `.properties` files.
- Uses local property files for persistence (e.g., `prefs.properties`).
- Allows configuration of the file paths for system and user preferences.


## **Technologies Used**

- **Java** (JDK 17 or higher)
- **Properties Files** (for preference storage)
- **Java Preferences API**


## **How to Set Up and Run**

Add the following dependency into yout project:

```xml
<dependency>
    <groupId>br.dev.jadl.preferences</groupId>
    <artifactId>properties-preferences</artifactId>
    <version>1.0.0</version>
</dependency>
```


### **Configuration via System Properties**

The configuration should be specified using system properties. These properties can be defined for a specific scope
(`user` or `system`), if no scope is defined, the settings will apply to both scopes.

- `br.dev.jadl.prefs.PropertiesPreferences.user.filename` - The file path for the user preferences file (default is `prefs.properties`).
- `br.dev.jadl.prefs.PropertiesPreferences.system.filename` - The file path for the system preferences file (default is `prefs.properties`).


### **Running your application**

You can define the system properties from the command line, through a configuration file, or dynamically at runtime.

#### Running from the command line


```bash
# Running a fat jar directly from the command line
java -Djava.util.prefs.PreferencesFactory=br.dev.jadl.prefs.PropertiesPreferencesFactory \
     -Dbr.dev.jadl.prefs.PropertiesPreferences.user.filename=userprefs.properties \
     -Dbr.dev.jadl.prefs.PropertiesPreferences.system.filename=application.properties \
     -jar your-application.jar
```

#### From a configuration file


```text
# Defines the Preferences API implementation to be used
-Djava.util.prefs.PreferencesFactory=br.dev.jadl.prefs.PropertiesPreferencesFactory

# No scope defined so it will be used for both
# user and system
-Dbr.dev.jadl.prefs.PropertiesPreferences.filename=prefs.properties
```

```bash
java @<filename> -jar application.jar
```

#### Dynamically at runtime

```java
import java.util.prefs.Preferences;

import static java.lang.System.Logger.Level.INFO;

public class Main {

    private static final System.Logger logger = System.getLogger(Main.class.getCanonicalName());

    public static void main(String[] args) {

        System.setProperty(PreferencesFactory.class.getCanonicalName(), "br.dev.jadl.prefs.PropertiesPreferencesFactory");
        System.setProperty("br.dev.jadl.prefs.PropertiesPreferences.filename", "prefs.properties");
        Preferences prefs = Preferences.userRoot().node("theme");

        // Retrieve the preference or the default value
        String theme = prefs.get("dark", "my-awesome-theme");

        logger.log(INFO, "Loading {0} theme", theme);
    }
}
```


## **Contributing**

If you encounter any issues or would like to contribute to improving this project, feel free to open an issue or submit
a pull request. We welcome your contributions!

## **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

