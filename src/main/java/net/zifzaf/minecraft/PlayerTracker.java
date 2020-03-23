package net.zifzaf.minecraft;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerTracker extends JavaPlugin {
    final static String ONLINE_KEY = "online";
    final static String PLAYER_KEY = "player:%s";
    final static String LOCATION_TIMESTAMP = "timestamp";
    final static String LOCATION_X_KEY = "x";
    final static String LOCATION_Y_KEY = "y";
    final static String LOCATION_Z_KEY = "z";

    public PlayerTracker() {
        super();
    }

    @Override
    public void onEnable() {
        getLogger().info("PlayerTracker enabled");

        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream("minecraft-zifzaf-net-firebase-adminsdk-bgo5k-1584b78f44");

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://minecraft-zifzaf-net.firebaseio.com").build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("PlayerTracker disabled");
    }
}
