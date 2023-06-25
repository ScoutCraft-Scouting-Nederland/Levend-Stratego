package nl.scoutcraft.levendstratego.game.gui;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.server.gui.inventory.base.AbstractInventoryMenu;
import nl.scoutcraft.eagle.server.gui.inventory.base.Button;
import nl.scoutcraft.eagle.server.gui.inventory.base.ButtonClickType;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import nl.scoutcraft.levendstratego.LevendStratego;
import nl.scoutcraft.levendstratego.game.Deck;
import nl.scoutcraft.levendstratego.game.Rank;
import nl.scoutcraft.levendstratego.game.phase.GamePlayPhase;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.i18n.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DeckInventoryMenu extends AbstractInventoryMenu {

    // TODO: Improve
    private static final Component TITLE = Messages.GUI_DECK_TITLE.get((Locale) null);
    private static final Button.Builder LOADING_BUTTON = Button.builder().setItem(new ItemBuilder(Material.BARRIER).name(Messages.GUI_DECK_LOADING.get((Locale) null, true)));

    private final GamePlayPhase game;
    private final Team team;
    private final Deck deck;
    private final Rank[] ranks;
    private final Rank[] rankCache;

    private final List<UUID> keepOpen;

    public DeckInventoryMenu(GamePlayPhase game, Team team, int deckSets) {
        super.setType(InventoryType.HOPPER);
        super.setTitle(TITLE);

        this.game = game;
        this.team = team;
        this.deck = new Deck(deckSets);
        this.ranks = new Rank[]{deck.pop(), deck.pop(), deck.pop()};
        this.rankCache = new Rank[3];
        this.keepOpen = new ArrayList<>();
    }

    @Override
    public void open(Player player) {
        if (!this.keepOpen.contains(player.getUniqueId()))
            this.keepOpen.add(player.getUniqueId());

        super.open(player);
    }

    @Override
    public void onInventoryClose(Player player) {
        if (this.keepOpen.contains(player.getUniqueId()))
            super.open(player);
    }

    public void close(boolean spectate) {
        this.keepOpen.clear();
        super.close(spectate ? this.game::spectate : null);
    }

    @Override
    protected List<Button> getButtons() {
        List<Button> buttons = Lists.newArrayList();

        buttons.add(Button.spacer(0, 4).build());
        buttons.add(getRankButton(0));
        buttons.add(getRankButton(1));
        buttons.add(getRankButton(2));

        return buttons;
    }

    public int size() {
        return this.deck.size() +
                (this.ranks[0] != null || this.rankCache[0] != null ? 1 : 0) +
                (this.ranks[1] != null || this.rankCache[1] != null ? 1 : 0) +
                (this.ranks[2] != null || this.rankCache[2] != null ? 1 : 0);
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    private Button getRankButton(int index) {
        Rank rank = this.ranks[index];
        if (rank == null) {
            if (this.deck.isEmpty()) {
                return Button.spacer(index + 1).build();
            } else {
                return LOADING_BUTTON.setIndices(index + 1).build();
            }
        }

        return Button.builder()
                .setSlots(index + 1)
                .addAction(ButtonClickType.ANY, p -> this.selectRank(p, index))
                .setItem(rank.getItem())
                .build();
    }

    private void selectRank(Player player, int index) {
        this.keepOpen.remove(player.getUniqueId());
        player.closeInventory();

        Rank rank = this.ranks[index];
        this.setRank(index, null);

        if (!this.deck.isEmpty()) {
            this.rankCache[index] = this.deck.pop();
            Bukkit.getScheduler().runTaskLater(LevendStratego.getInstance(), () -> { this.setRank(index, this.rankCache[index]); this.rankCache[index] = null; }, 40L);
        }

        this.game.respawn(player, this.team, rank, true);

        if (this.isEmpty())
            this.close(true);
    }

    private void setRank(int index, @Nullable Rank rank) {
        this.ranks[index] = rank;
        super.update();
    }

    public Deck getDeck() {
        return deck;
    }
}
