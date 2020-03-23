package net.zifzaf.minecraft;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerListener implements Listener {
    private final PlayerTracker plugin;

    PlayerListener(PlayerTracker plugin) {
        this.plugin = plugin;
    }

    private final HashMap<Integer, Long> playerUpdateTimes = new HashMap<Integer, Long>(64);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getLogger().info(event.getPlayer().getName() + " joined the server! :D");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getLogger().info(event.getPlayer().getName() + " left the server! :'(");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        /*
        plugin.getLogger().info(String.format("%s from %.2f,%.2f,%.2f to %.2f,%.2f,%.2f",
                event.getPlayer().getName(),
                from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ()));
        */

        int playerIdHash = event.getPlayer().getUniqueId().hashCode();

        String key = String.format(PlayerTracker.PLAYER_KEY, event.getPlayer().getUniqueId().toString());

        long updateTime = playerUpdateTimes.get(playerIdHash) == null ? 0 : playerUpdateTimes.get(playerIdHash);
        long currentTime = System.currentTimeMillis();

        if (updateTime <= currentTime - 1000) {
            HashMap<String, String> location = new HashMap<>(6, 1);
            location.put(PlayerTracker.LOCATION_TIMESTAMP, String.valueOf(currentTime));
            location.put(PlayerTracker.LOCATION_X_KEY, String.valueOf(to.getX()));
            location.put(PlayerTracker.LOCATION_Y_KEY, String.valueOf(to.getY()));
            location.put(PlayerTracker.LOCATION_Z_KEY, String.valueOf(to.getZ()));


            playerUpdateTimes.put(playerIdHash, currentTime);
        }
    }
}