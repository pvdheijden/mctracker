package net.zifzaf.minecraft;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerTracker extends JavaPlugin {
    public PlayerTracker() {
        super();
    }

    Firestore database;

    @Override
    public void onEnable() {
        getLogger().info("PlayerTracker enabled");

        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream("/opt/minecraft/secrets/firebase-admin.json");

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://minecraft-zifzaf-net.firebaseio.com").build();

            FirebaseApp.initializeApp(options);
            
            database = FirestoreClient.getFirestore();

            PluginManager pm = this.getServer().getPluginManager();
            pm.registerEvents(new PlayerListener(this), this);
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "PlayerTracker error: " + e.getMessage());
        }

    }

    @Override
    public void onDisable() {
        getLogger().info("PlayerTracker disabled");
    }
}
