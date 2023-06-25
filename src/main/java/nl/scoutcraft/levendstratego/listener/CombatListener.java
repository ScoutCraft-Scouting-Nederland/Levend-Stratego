package nl.scoutcraft.levendstratego.listener;

import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.eagle.server.locale.MessagePlaceholder;
import nl.scoutcraft.eagle.server.locale.Placeholder;
import nl.scoutcraft.levendstratego.data.Keys;
import nl.scoutcraft.levendstratego.data.strategodata.StrategoData;
import nl.scoutcraft.levendstratego.effect.Effects;
import nl.scoutcraft.levendstratego.game.Rank;
import nl.scoutcraft.levendstratego.game.phase.GamePlayPhase;
import nl.scoutcraft.levendstratego.manager.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class CombatListener implements Listener {

    private final GameManager manager;

    public CombatListener(GameManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Player)
            this.handleAttack(e.getPlayer(), (Player) e.getRightClicked());
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player)
            this.handleAttack((Player) e.getDamager(), (Player) e.getEntity());
    }

    private void handleAttack(Player attacker, Player defender) {
        if (attacker.isInvulnerable() || defender.isInvulnerable())
            return;

        GamePlayPhase game = (GamePlayPhase) this.manager.getGame();
        if (game == null)
            return;

        StrategoData attackerData = attacker.getPersistentDataContainer().get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        StrategoData defenderData = defender.getPersistentDataContainer().get(Keys.STRATEGO_DATA, Keys.STRATEGO_DATA_TAG_TYPE);
        if (attackerData.getTeam() == defenderData.getTeam())
            return;

        IMessage attackerRank = attackerData.getRank().getRankText();
        IMessage defenderRank = defenderData.getRank().getRankText();

        Rank.AttackResult result = attackerData.getRank().attack(defenderData.getRank());
        if (result == Rank.AttackResult.INVALID) {
            Effects.COMBAT_ATTACK_INVALID.play(attacker);
        }
        else if (result == Rank.AttackResult.TIE) {
            Effects.COMBAT_ATTACK_TIE.play(attacker, new MessagePlaceholder("%defender_rank%", defenderRank), new Placeholder("%defender_name%", defender.getName()));
            Effects.COMBAT_DEFENCE_TIE.play(defender, new MessagePlaceholder("%attacker_rank%", attackerRank), new Placeholder("%attacker_name%", attacker.getName()));
        }
        else if (result == Rank.AttackResult.VICTORY) {
            Effects.COMBAT_ATTACK_VICTORY.play(attacker, new MessagePlaceholder("%defender_rank%", defenderRank), new Placeholder("%defender_name%", defender.getName()));
            Effects.COMBAT_DEFEAT.play(defender, new MessagePlaceholder("%killer_rank%", attackerRank), new Placeholder("%killer_name%", attacker.getName()));

            game.kill(defender, defenderData, attacker, false);
        }
        else if (result == Rank.AttackResult.DEFEAT) {
            Effects.COMBAT_DEFEAT.play(attacker, new MessagePlaceholder("%killer_rank%", defenderRank), new Placeholder("%killer_name%", defender.getName()));
            Effects.COMBAT_DEFENCE_VICTORY.play(defender, new MessagePlaceholder("%attacker_rank%", attackerRank), new Placeholder("%attacker_name%", attacker.getName()));

            game.kill(attacker, attackerData, defender, false);
        }
    }
}
