package nl.scoutcraft.levendstratego.game;

import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import nl.scoutcraft.levendstratego.i18n.Messages;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum Rank {

    SPY("spy", 1, 10, Material.COAL, Messages.RANK_SPY, Messages.RANK_SPY_LORE),
    SCOUT("scout", 2, 0, Material.SUGAR, Messages.RANK_SCOUT, Messages.RANK_SCOUT_LORE),
    MINER("miner", 3, 11, Material.FLINT_AND_STEEL, Messages.RANK_MINER, Messages.RANK_MINER_LORE),
    SERGEANT("sergeant", 4, 0, Material.QUARTZ, Messages.RANK_SERGEANT, Messages.RANK_SERGEANT_LORE),
    LIEUTENANT("lieutenant", 5, 0, Material.LAPIS_LAZULI, Messages.RANK_LIEUTENANT, Messages.RANK_LIEUTENANT_LORE),
    CAPTAIN("captain", 6, 0, Material.IRON_INGOT, Messages.RANK_CAPTAIN, Messages.RANK_CAPTAIN_LORE),
    MAJOR("major", 7, 0, Material.GOLD_INGOT, Messages.RANK_MAJOR, Messages.RANK_MAJOR_LORE),
    COLONEL("colonel", 8, 0, Material.DIAMOND, Messages.RANK_COLONEL, Messages.RANK_COLONEL_LORE),
    GENERAL("general", 9, 0, Material.EMERALD, Messages.RANK_GENERAL, Messages.RANK_GENERAL_LORE),
    MARSHALL("marshall", 10, 0, Material.NETHERITE_INGOT, Messages.RANK_MARSHALL, Messages.RANK_MARSHALL_LORE),
    BOMB("bomb", 11, 0, Material.GUNPOWDER, Messages.RANK_BOMB, Messages.RANK_BOMB_LORE);

    public enum AttackResult {
        VICTORY, DEFEAT, TIE, INVALID
    }

    private final String name;
    private final int basePower;
    private final int specialPower;
    private final Material material;
    private final IMessage langText;
    private final IMessage langLore;

    @Nullable private String rankText;
    @Nullable private ItemBuilder item;

    Rank(String name, int basePower, int specialPower, Material material, IMessage langText, IMessage langLore) {
        this.name = name;
        this.basePower = basePower;
        this.specialPower = specialPower;
        this.material = material;
        this.langText = langText;
        this.langLore = langLore;
        this.rankText = null;
        this.item = null;
    }

    /**
     * Determines who wins the attack.
     *
     * @param other The attacked player's {@link Rank}.
     * @return The {@link AttackResult}
     */
    public AttackResult attack(Rank other) {
        if (this.basePower == 11)
            return AttackResult.INVALID;
        if (this.specialPower == other.basePower)
            return AttackResult.VICTORY;
        if (this.basePower == other.specialPower)
            return AttackResult.DEFEAT;
        if (this.basePower > other.basePower)
            return AttackResult.VICTORY;
        if (this.basePower < other.basePower)
            return AttackResult.DEFEAT;
        return AttackResult.TIE;
    }

    public String getName() {
        return this.name;
    }

    public int getBasePower() {
        return this.basePower;
    }

    public int getSpecialPower() {
        return this.specialPower;
    }

    public ItemBuilder getItem() {
        return this.item != null ? this.item : (this.item = new ItemBuilder(this.material, basePower == 11 ? 1 : basePower).name(this.langText.get((Locale) null, true)).lore(this.getLore()));
    }

    public IMessage getRankText() {
        return this.langText;
    }

    // TODO: Improve (use lang.getList)
    private String[] getLore() {
        return this.langLore.getString((Locale) null).split(";");
    }

    @Nullable
    public static Rank of(@Nullable Integer basePower) {
        return basePower == null ? null : of(basePower.intValue());
    }

    @Nullable
    public static Rank of(int basePower) {
        for (Rank rank : values())
            if (rank.getBasePower() == basePower)
                return rank;
            
        return null;
    }
}
