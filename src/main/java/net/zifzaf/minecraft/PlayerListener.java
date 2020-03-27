package net.zifzaf.minecraft;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final PlayerTracker plugin;
    PlayerListener(PlayerTracker plugin) {
        this.plugin = plugin;
    }

    private final HashMap<Integer, Long> playerUpdateTimes = new HashMap<>(64);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getLogger().info(event.getPlayer().getName() + " joined the server! :D");

        String key = event.getPlayer().getUniqueId().toString();

        Map<String, Object> statusData = new HashMap<>();
        statusData.put("timestamp", System.currentTimeMillis());
        statusData.put("online", true);
    
        plugin.database.collection("playerStatus").document(key)
            .collection("data").document("status").set(statusData);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getLogger().info(event.getPlayer().getName() + " left the server! :'(");

        String key = event.getPlayer().getUniqueId().toString();

        Map<String, Object> statusData = new HashMap<>();
        statusData.put("timestamp", System.currentTimeMillis());
        statusData.put("online", false);
    
        plugin.database.collection("playerStatus").document(key)
            .collection("data").document("status").set(statusData);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        int id = event.getPlayer().getUniqueId().hashCode();
        long updateTimestamp = playerUpdateTimes.get(id) == null ? 0 : playerUpdateTimes.get(id);
        long currentTimestamp = System.currentTimeMillis();

        if (updateTimestamp <= currentTimestamp - 10000) {
            String key = event.getPlayer().getUniqueId().toString();

            Location from = event.getFrom();
            Location to = event.getTo();

            Map<String, Object> locationData = new HashMap<>();
            locationData.put("timestamp", currentTimestamp);
            locationData.put("from", Arrays.asList(from.getX(), from.getY(), from.getZ()));
            locationData.put("to", Arrays.asList(to.getX(), to.getY(), to.getZ()));

            plugin.database.collection("playerStatus").document(key)
                .collection("data").document("location").set(locationData);
            
            playerUpdateTimes.put(id, currentTimestamp);
        }
    }
}
