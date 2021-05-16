package fr.flushfr.crates.license;

import fr.flushfr.crates.Main;
import fr.flushfr.crates.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;

public class VersionChecker implements Listener {

    public final String URL_API = "https://groupez.dev/api/v1/resource/version/%s";
    public final String URL_RESOURCE = "https://groupez.dev/resources/%s";
    public static int pluginID;
    public boolean useLastVersion = false;

    private static VersionChecker instance;
    public VersionChecker (int pluginID) {
        instance = this;
        VersionChecker.pluginID = pluginID;
        useLastVersion();
    }
    public static VersionChecker getInstance() {
        return instance;
    }

    public void useLastVersion() {
        String pluginVersion = Main.getMainInstance().getDescription().getVersion();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        this.getVersion(version -> {

            long ver = Long.valueOf(version.replace(".", ""));
            long plVersion = Long.valueOf(pluginVersion.replace(".", ""));
            atomicBoolean.set(plVersion >= ver);
            this.useLastVersion = atomicBoolean.get();
            if (atomicBoolean.get())
                Logger.getInstance().log(Level.INFO,"No update available.");
            else {
                Logger.getInstance().log(Level.INFO,"New update available. Your version: " + pluginVersion + ", latest version: " + version);
                Logger.getInstance().log(Level.INFO,"Download plugin here: " + String.format(URL_RESOURCE, this.pluginID));
            }
        });

    }

    /**
     * Get version by plugin id
     *
     * @param consumer
     *            - Do something after
     */
    public void getVersion(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getMainInstance(), () -> {
            final String apiURL = String.format(URL_API, this.pluginID);
            try {
                URL url = new URL(apiURL);
                URLConnection hc = url.openConnection();
                hc.setRequestProperty("User-Agent", "Mozilla/5.0");
                Scanner scanner = new Scanner(hc.getInputStream());
                if (scanner.hasNext())
                    consumer.accept(scanner.next());
                scanner.close();

            } catch (IOException exception) {
                Main.getMainInstance().getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

}
