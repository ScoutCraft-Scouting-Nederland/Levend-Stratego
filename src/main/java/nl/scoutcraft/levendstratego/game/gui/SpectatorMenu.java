package nl.scoutcraft.levendstratego.game.gui;

import net.kyori.adventure.text.Component;
import nl.scoutcraft.eagle.server.gui.inventory.base.AbstractInventoryMenu;
import nl.scoutcraft.eagle.server.gui.inventory.base.Button;
import nl.scoutcraft.eagle.server.gui.inventory.base.ButtonClickType;
import nl.scoutcraft.eagle.server.utils.ItemBuilder;
import nl.scoutcraft.levendstratego.game.phase.IMainGamePhase;
import nl.scoutcraft.levendstratego.game.team.Team;
import nl.scoutcraft.levendstratego.i18n.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpectatorMenu extends AbstractInventoryMenu {

    // TODO: Improve
    private static final Component TITLE = Messages.GUI_SPECTATOR_TITLE.get((Locale) null);
    private static final Component TELEPORT_MESSAGE = Messages.GUI_SPECTATOR_TELEPORT.get((Locale) null);

    private final IMainGamePhase game;

    public SpectatorMenu(IMainGamePhase game) {
        super.setType(InventoryType.CHEST);
        super.setSize(45);
        super.setTitle(TITLE);

        this.game = game;
    }

    @Override
    protected List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();

        buttons.add(Button.spacer(4, 13, 22, 31, 40).build());

        List<Player> red = this.game.getTeamData(Team.RED).getOnlinePlayers();
        List<Player> blue = this.game.getTeamData(Team.BLUE).getOnlinePlayers();

        red.removeAll(this.game.getSpectators());
        blue.removeAll(this.game.getSpectators());

        int index = 0;
        for (Player rp : red) {
            buttons.add(this.getButton(rp, Team.RED, index++));

            if (index % 9 == 4)
                index += 5;
        }

        index = 5;
        for (Player bp : blue) {
            buttons.add(this.getButton(bp, Team.BLUE, index++));

            if (index % 9 == 0)
                index += 5;
        }

        return buttons;
    }

    private Button getButton(Player player, Team team, int index) {
        return Button.builder()
                .setItem(new ItemBuilder(Material.PLAYER_HEAD, 1)
                        .skull(player.getUniqueId())
                        .name(team.getTeamColor().get(player).append(Component.text(player.getName())))
                        .lore(TELEPORT_MESSAGE))
                .setSlots(index)
                .addAction(ButtonClickType.ANY, p -> p.teleport(player.getLocation()))
                .build();
    }
}
