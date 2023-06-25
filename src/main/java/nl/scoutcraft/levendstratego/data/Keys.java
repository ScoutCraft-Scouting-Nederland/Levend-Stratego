package nl.scoutcraft.levendstratego.data;

import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.data.locale.LocaleTagType;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoDataTagType;
import org.bukkit.NamespacedKey;

public class Keys {

    private static final LevendStratego plugin = LevendStratego.getInstance();
    public static final StrategoDataTagType STRATEGO_DATA_TAG_TYPE = new StrategoDataTagType();
    public static final LocaleTagType LOCALE_TAG_TYPE = new LocaleTagType();

    public static final NamespacedKey STRATEGO_DATA = new NamespacedKey(plugin, "stratego_data");;
    public static final NamespacedKey TEAM = new NamespacedKey(plugin, "team");
    public static final NamespacedKey RANK = new NamespacedKey(plugin, "rank");
    public static final NamespacedKey RED_FLAG = new NamespacedKey(plugin, "red_flag");
    public static final NamespacedKey BLUE_FLAG = new NamespacedKey(plugin, "blue_flag");

    public static final NamespacedKey CHAT_CHANNEL = new NamespacedKey(plugin, "chat_channel");
    public static final NamespacedKey PARTY_ID = new NamespacedKey(plugin, "party_id");

    public static final NamespacedKey BUTTON_ID = new NamespacedKey(plugin, "button_id");

    public static final NamespacedKey LOCALE = new NamespacedKey(plugin, "locale");
}
