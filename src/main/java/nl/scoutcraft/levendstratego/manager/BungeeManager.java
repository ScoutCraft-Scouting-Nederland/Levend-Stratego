package nl.scoutcraft.levendstratego.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import nl.scoutcraft.eagle.libs.network.NetworkChannel;
import nl.scoutcraft.eagle.libs.network.channels.IPartyChannel;
import nl.scoutcraft.eagle.libs.network.channels.IPlayerChannel;
import nl.scoutcraft.eagle.libs.network.channels.IServerChannel;
import nl.scoutcraft.eagle.libs.server.GameState;
import nl.scoutcraft.eagle.server.network.ServerNetworkChannel;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.data.Keys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BungeeManager implements IServerChannel, IPartyChannel {

    private static final Logger LOGGER = Logger.getLogger("LS Bungee");
    private static final String HUB_SERVER = LevendStratego.getInstance().getConfig().getString("hub_server");;

    private final LevendStratego plugin;
    private final NetworkChannel<IServerChannel> server;
    private final NetworkChannel<IPlayerChannel> player;
    private final NetworkChannel<IPartyChannel> party;

    public BungeeManager(LevendStratego plugin) {
        this.plugin = plugin;
        this.server = new ServerNetworkChannel<>("eagle:server", IServerChannel.class, null, plugin);
        this.player = new ServerNetworkChannel<>("eagle:player", IPlayerChannel.class, null, plugin);
        this.party = new ServerNetworkChannel<>("eagle:party", IPartyChannel.class, null, plugin);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    public void sendToHub(Player player) {
        ByteArrayDataOutput data = ByteStreams.newDataOutput();
        data.writeUTF("Connect");
        data.writeUTF(HUB_SERVER);
        player.sendPluginMessage(this.plugin, "BungeeCord", data.toByteArray());
    }

    public void sendStatus(GameState status) {
        this.server.request().setTarget(Bukkit.getServer()).setGameState(status.getId());
    }

    public void setInGame(Collection<? extends Player> players, boolean value) {
        players.forEach(p -> this.setInGame(p, value));
    }

    public void setInGame(Player player, boolean value) {
        this.player.request().setTarget(player).setInGame(player.getUniqueId(), value);
    }

    public void requestPartyUUID(Player player) {
        this.party.request()
                .onResponse(data -> {
                    if (data != null)
                        player.getPersistentDataContainer().set(Keys.PARTY_ID, PersistentDataType.STRING, data.toString());
                })
                .setTarget(player)
                .getPartyId(player.getUniqueId());
    }
}
