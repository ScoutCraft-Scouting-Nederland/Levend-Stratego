package nl.scoutcraft.levendstratego.listener;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.libs.utils.TextUtils;
import nl.scoutcraft.eagle.server.data.EagleKeys;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoData;
import nl.scoutcraft.levendstratego.game.ChatChannel;
import nl.scoutcraft.levendstratego.i18n.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChatListener implements Listener {

    private final Map<ChatChannel, ChatRenderer> renderers;

    public ChatListener() {
        this.renderers = new HashMap<>(); // this.createRenderer(Messages.CHAT_FORMAT.get((Locale) null));
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        ChatChannel channel;

        Component message = event.message();

        if (TextUtils.toString(message).startsWith("$")) {
            message = message.replaceText(b -> b.matchLiteral("$").replacement(""));
            event.message(message);

            if (TextUtils.toPlainString(message).length() == 0) {
                event.setCancelled(true);
                return;
            }

            channel = ChatChannel.GLOBAL;
        } else {
            channel = ChatChannel.of(player.getPersistentDataContainer().get(Keys.CHAT_CHANNEL, PersistentDataType.INTEGER));
        }

        if (channel == null)
            return;

        event.renderer(this.renderers.computeIfAbsent(channel, this::createRenderer));

        if (channel != ChatChannel.GLOBAL)
            event.viewers().removeIf(au -> au instanceof Player p && !channel.canReceive(p));
    }

    private ChatRenderer createRenderer(ChatChannel channel) {
        Component channelFormat = Messages.CHAT_FORMAT.get((Locale) null).replaceText(b -> b.match("%channel%").replacement(channel.getTagText()));

        return (player, displayName, message, audience) -> {
            PersistentDataContainer data = player.getPersistentDataContainer();
            StrategoData strategoData = data.get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
            String nameString = TextUtils.toPlainString(TextUtils.colorize(data.getOrDefault(EagleKeys.DISPLAY_NAME, PersistentDataType.STRING, player.getName())));
            String prefixString = data.getOrDefault(EagleKeys.PREFIX, PersistentDataType.STRING, "");

            Component name = strategoData == null ? Component.text(nameString) : strategoData.getTeam().getTeamColor().get((Locale) null).append(Component.text(nameString));
            Component prefix = TextUtils.colorize(prefixString);

            return channelFormat.replaceText(b -> b.matchLiteral("%prefix%").replacement(prefix))
                    .replaceText(b -> b.matchLiteral("%name%").replacement(name))
                    .replaceText(b -> b.matchLiteral("%message%").replacement(message));
        };
    }
}
