package nl.scoutcraft.levendstratego.effect;

import nl.scoutcraft.levendstratego.i18n.Messages;
import org.bukkit.Particle;
import org.bukkit.Sound;

public final class Effects {

    public static final Effect LOBBY_JOIN = new ChatEffect(Messages.LOBBY_JOIN);
    public static final Effect LOBBY_QUIT = new ChatEffect(Messages.LOBBY_QUIT);
    public static final Effect LOBBY_SPECTATE = new TitleEffect(Messages.LOBBY_SPECTATE, 10, 80, 10);
    public static final Effect LOBBY_COUNTDOWN = new ActionBarEffect(Messages.LOBBY_COUNTDOWN);
    public static final Effect LOBBY_MAP = new TitleEffect(Messages.LOBBY_SHOWMAP, 10, 100, 10);
    public static final Effect COUNTDOWN_CLICK = new SoundEffect(Sound.UI_BUTTON_CLICK, 1.5f, 1.45f);

    public static final Effect FLAGHIDING_COUNTDOWN = new ActionBarEffect(Messages.FLAGHIDING_COUNTDOWN);
    public static final Effect FLAGHIDING_TEAM = new ChatEffect(Messages.FLAGHIDING_TEAM);
    public static final Effect FLAG_CARRIER = new TitleEffect(Messages.FLAGHIDING_CARRIER, 10, 120, 10);

    public static final Effect COMBAT_ATTACK_INVALID = new EffectList(new SoundEffect(Sound.BLOCK_CHAIN_BREAK, 2f, 1f), new TitleEffect(Messages.COMBAT_INVALID, 5, 30, 5));
    public static final Effect COMBAT_ATTACK_TIE = new EffectList(new SoundEffect(Sound.ENTITY_IRON_GOLEM_REPAIR, 1f, 1.25f), new TitleEffect(Messages.COMBAT_ATTACK_TIE, 5, 40, 5));
    public static final Effect COMBAT_DEFENCE_TIE = new EffectList(new SoundEffect(Sound.ENTITY_IRON_GOLEM_REPAIR, 1f, 1.25f), new TitleEffect(Messages.COMBAT_DEFENCE_TIE, 5, 40, 5));
    public static final Effect COMBAT_ATTACK_VICTORY = new EffectList(new SoundEffect(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 2f, 2f), new TitleEffect(Messages.COMBAT_ATTACK_VICTORY, 5, 40, 5));
    public static final Effect COMBAT_DEFENCE_VICTORY = new EffectList(new SoundEffect(Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 2f, 2f), new TitleEffect(Messages.COMBAT_DEFENCE_VICTORY, 5, 40, 5));
    public static final Effect COMBAT_DEFEAT = new EffectList(new SoundEffect(Sound.ENTITY_WITHER_HURT, 2f, 0.5f), new TitleEffect(Messages.COMBAT_DEATH, 10, 60, 10));

    public static final Effect DEATH = new ParticleEffect(Particle.LAVA.builder().count(30).offset(0.5, 0.1, 0.5));
    public static final Effect RESPAWN = new TitleEffect(Messages.SPAWN, 10, 100, 10);
    public static final Effect CARD_USE = new SoundEffect(Sound.BLOCK_BELL_USE, 2f, 1.5f);

    public static final Effect FLAG_PICKUP = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 2f, 2f);
    public static final Effect FLAG_PLACE = new SoundEffect(Sound.ENTITY_PLAYER_LEVELUP, 2f, 1.5f);
    public static final Effect FLAG_PLACE_INVALID = new TitleEffect(Messages.GAMEPLAY_FLAG_PLACE_INVALID, 5, 60, 5);
    public static final Effect FLAG_PICKUP_INVALID = new EffectList(new SoundEffect(Sound.BLOCK_CHAIN_BREAK, 2f, 1f), new ActionBarEffect(Messages.GAMEPLAY_BOMB_FLAG_PICKUP));
    public static final Effect FLAG_FOUND = new EffectList(new SoundEffect(Sound.BLOCK_BEACON_DEACTIVATE, 2f, 1.6f), new TitleEffect(Messages.GAMEPLAY_FRIENDLY_FLAG_PICKUP, 5, 80, 5));
    public static final Effect FLAG_STOLEN = new EffectList(new SoundEffect(Sound.BLOCK_BEACON_DEACTIVATE, 2f, 0.95f), new TitleEffect(Messages.GAMEPLAY_ENEMY_FLAG_PICKUP, 5, 80, 5));
    public static final Effect SPECTATOR_FLAG_STOLEN = new EffectList(new SoundEffect(Sound.BLOCK_BEACON_DEACTIVATE, 2f, 0.95f), new TitleEffect(Messages.SPECTATOR_FLAG_PICKUP, 5, 80, 5));

    public static final Effect ROUND_VICTORY = new TitleEffect(Messages.ROUND_VICTORY, 10, 100, 10);
    public static final Effect ROUND_DEFEAT = new TitleEffect(Messages.ROUND_DEFEAT, 10, 100, 10);
    public static final Effect SPECTATOR_END_OF_ROUND = new TitleEffect(Messages.ROUND_SPECTATOR, 10, 100, 10);

    public static final Effect GAME_VICTORY = new EffectList(new SoundEffect(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 0.9f), new TitleEffect(Messages.GAMEOVER_VICTORY, 10, 100, 10));
    public static final Effect GAME_DEFEAT = new EffectList(new SoundEffect(Sound.BLOCK_END_PORTAL_SPAWN, 0.8f, 0.5f), new TitleEffect(Messages.GAMEOVER_DEFEAT, 10, 100, 10));
    public static final Effect SPECTATOR_END_OF_GAME = new TitleEffect(Messages.GAMEOVER_SPECTATOR, 10, 100, 10);

    private Effects(){}
}
