package net.zifzaf.player_tracker;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import redis.clients.jedis.JedisPool;

public class PlayerTracker extends JavaPlugin {
    /**
     * Redis client
     */
    final static JedisPool jedisPool = new JedisPool("redis");

    final static String ONLINE_KEY = "online";
    final static String PLAYER_KEY = "player:%s";
    final static String LOCATION_TIMESTAMP = "timestamp";
    final static String LOCATION_X_KEY = "x";
    final static String LOCATION_Y_KEY = "y";
    final static String LOCATION_Z_KEY = "z";


    private final ZContext zContext = new ZContext();
    private ZMQ.Socket publisher;

    public PlayerTracker() {
        super();
    }

    @Override
    public void onEnable() {
        getLogger().info("PlayerTracker enabled");

        publisher = zContext.createSocket(SocketType.PUB);
        publisher.bind("tcp://*:5556");

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("PlayerTracker disabled");

        publisher.unbind("tcp://*:5556");
        publisher.close();
    }

    boolean publisherSend(String data) {
        return publisher.send(data, 0);
    }
}
