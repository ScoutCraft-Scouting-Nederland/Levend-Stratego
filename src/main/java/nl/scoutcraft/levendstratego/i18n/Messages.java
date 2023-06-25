package nl.scoutcraft.levendstratego.i18n;

import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.eagle.server.locale.BlankMessage;
import nl.scoutcraft.eagle.server.locale.IMessage;
import nl.scoutcraft.eagle.server.locale.Message;
import nl.scoutcraft.levendstratego.LevendStratego;

public final class Messages {

    private static final Internationalization LANG = LevendStratego.getInstance().getLang();

    public static final IMessage BLANK = new BlankMessage();

    public static final IMessage CHAT_FORMAT = new Message(LANG, "chat.format");
    public static final IMessage CHAT_FORMAT_PLACEHOLDERAPI = new Message(LANG, "chat.format.placeholderapi");

    public static final IMessage RANK_SPY = new Message(LANG, "rank.spy");
    public static final IMessage RANK_SPY_LORE = new Message(LANG, "rank.spy.lore");
    public static final IMessage RANK_SCOUT = new Message(LANG, "rank.scout");
    public static final IMessage RANK_SCOUT_LORE = new Message(LANG, "rank.scout.lore");
    public static final IMessage RANK_MINER = new Message(LANG, "rank.miner");
    public static final IMessage RANK_MINER_LORE = new Message(LANG, "rank.miner.lore");
    public static final IMessage RANK_SERGEANT = new Message(LANG, "rank.sergeant");
    public static final IMessage RANK_SERGEANT_LORE = new Message(LANG, "rank.sergeant.lore");
    public static final IMessage RANK_LIEUTENANT = new Message(LANG, "rank.lieutenant");
    public static final IMessage RANK_LIEUTENANT_LORE = new Message(LANG, "rank.lieutenant.lore");
    public static final IMessage RANK_CAPTAIN = new Message(LANG, "rank.captain");
    public static final IMessage RANK_CAPTAIN_LORE = new Message(LANG, "rank.captain.lore");
    public static final IMessage RANK_MAJOR = new Message(LANG, "rank.major");
    public static final IMessage RANK_MAJOR_LORE = new Message(LANG, "rank.major.lore");
    public static final IMessage RANK_COLONEL = new Message(LANG, "rank.colonel");
    public static final IMessage RANK_COLONEL_LORE = new Message(LANG, "rank.colonel.lore");
    public static final IMessage RANK_GENERAL = new Message(LANG, "rank.general");
    public static final IMessage RANK_GENERAL_LORE = new Message(LANG, "rank.general.lore");
    public static final IMessage RANK_MARSHALL = new Message(LANG, "rank.marshall");
    public static final IMessage RANK_MARSHALL_LORE = new Message(LANG, "rank.marshall.lore");
    public static final IMessage RANK_BOMB = new Message(LANG, "rank.bomb");
    public static final IMessage RANK_BOMB_LORE = new Message(LANG, "rank.bomb.lore");

    public static final IMessage TEAM_RED_COLOR = new Message(LANG, "team.red.color");
    public static final IMessage TEAM_BLUE_COLOR = new Message(LANG, "team.blue.color");
    public static final IMessage TEAM_RED_NAME = new Message(LANG, "team.red.name");
    public static final IMessage TEAM_BLUE_NAME = new Message(LANG, "team.blue.name");

    public static final IMessage LOBBY_JOIN = new Message(LANG, "lobby.join");
    public static final IMessage LOBBY_QUIT = new Message(LANG, "lobby.quit");
    public static final IMessage LOBBY_COUNTDOWN = new Message(LANG, "lobby.countdown");
    public static final IMessage[] LOBBY_SHOWMAP = new IMessage[]{ new Message(LANG, "lobby.showmap.title"), new Message(LANG, "lobby.showmap.subtitle") };
    public static final IMessage[] LOBBY_SPECTATE = new IMessage[]{ new Message(LANG, "lobby.spectate.title"), new Message(LANG, "lobby.spectate.subtitle") };
    public static final IMessage FLAGHIDING_TEAM = new Message(LANG, "flaghiding.team");
    public static final IMessage FLAGHIDING_COUNTDOWN = new Message(LANG, "flaghiding.countdown");
    public static final IMessage[] FLAGHIDING_CARRIER = new IMessage[]{ new Message(LANG, "flaghiding.carrier.title"), new Message(LANG, "flaghiding.carrier.subtitle") };
    public static final IMessage[] SPAWN = new IMessage[]{ new Message(LANG, "spawn.title"), new Message(LANG, "spawn.subtitle") };
    public static final IMessage SPAWN_INVULNERABILITY = new Message(LANG, "spawn.invulnerability");
    public static final IMessage SPAWN_INVULNERABILITY_END = new Message(LANG, "spawn.invulnerability.end");
    public static final IMessage[] GAMEPLAY_FLAG_PLACE_INVALID = new IMessage[]{ new Message(LANG, "gameplay.flag_place_invalid.title"), new Message(LANG, "gameplay.flag_place_invalid.subtitle") };
    public static final IMessage GAMEPLAY_BOMB_FLAG_PICKUP = new Message(LANG, "gameplay.bomb_flag_pickup");
    public static final IMessage[] GAMEPLAY_FRIENDLY_FLAG_PICKUP = new IMessage[]{ new Message(LANG, "gameplay.friendly_flag_pickup.title"), new Message(LANG, "gameplay.friendly_flag_pickup.subtitle") };
    public static final IMessage[] GAMEPLAY_ENEMY_FLAG_PICKUP = new IMessage[]{ new Message(LANG, "gameplay.enemy_flag_pickup.title"), new Message(LANG, "gameplay.enemy_flag_pickup.subtitle") };
    public static final IMessage GAMEPLAY_FLAG_REHIDING = new Message(LANG, "gameplay.flag_rehiding");
    public static final IMessage[] SPECTATOR_FLAG_PICKUP = new IMessage[]{ new Message(LANG, "spectator.flag_pickup.title"), new Message(LANG, "spectator.flag_pickup.subtitle") };
    public static final IMessage[] COMBAT_ATTACK_VICTORY = new IMessage[]{ new Message(LANG, "combat.attack.victory.title"), new Message(LANG, "combat.attack.victory.subtitle") };
    public static final IMessage[] COMBAT_DEFENCE_VICTORY = new IMessage[]{ new Message(LANG, "combat.defence.victory.title"), new Message(LANG, "combat.defence.victory.subtitle") };
    public static final IMessage[] COMBAT_ATTACK_TIE = new IMessage[]{ new Message(LANG, "combat.attack.tie.title"), new Message(LANG, "combat.attack.tie.subtitle") };
    public static final IMessage[] COMBAT_DEFENCE_TIE = new IMessage[]{ new Message(LANG, "combat.defence.tie.title"), new Message(LANG, "combat.defence.tie.subtitle") };
    public static final IMessage[] COMBAT_INVALID = new IMessage[]{ new Message(LANG, "combat.invalid.title"), new Message(LANG, "combat.invalid.subtitle") };
    public static final IMessage[] COMBAT_DEATH = new IMessage[]{ new Message(LANG, "combat.death.title"), new Message(LANG, "combat.death.subtitle") };
    public static final IMessage DEATH_RESPAWN = new Message(LANG, "death.respawn");
    public static final IMessage[] ROUND_VICTORY = new IMessage[]{ new Message(LANG, "round.victory.title"), new Message(LANG, "round.victory.subtitle") };
    public static final IMessage[] ROUND_DEFEAT = new IMessage[]{ new Message(LANG, "round.defeat.title"), new Message(LANG, "round.defeat.subtitle") };
    public static final IMessage[] ROUND_SPECTATOR = new IMessage[]{ new Message(LANG, "round.spectator.title"), new Message(LANG, "round.spectator.subtitle") };
    public static final IMessage[] GAMEOVER_VICTORY = new IMessage[]{ new Message(LANG, "gameover.victory.title"), new Message(LANG, "gameover.subtitle") };
    public static final IMessage[] GAMEOVER_DEFEAT = new IMessage[]{ new Message(LANG, "gameover.defeat.title"), new Message(LANG, "gameover.subtitle") };
    public static final IMessage[] GAMEOVER_SPECTATOR = new IMessage[]{ new Message(LANG, "gameover.spectator.title"), new Message(LANG, "gameover.subtitle") };

    public static final IMessage SIDEBAR_TITLE = new Message(LANG, "sidebar.title");
    public static final IMessage SIDEBAR_MAP = new Message(LANG, "sidebar.map");
    public static final IMessage SIDEBAR_ROUND = new Message(LANG, "sidebar.round");
    public static final IMessage SIDEBAR_ROUNDS_WON = new Message(LANG, "sidebar.rounds_won");
    public static final IMessage SIDEBAR_TEAM = new Message(LANG, "sidebar.team");
    public static final IMessage SIDEBAR_RANK = new Message(LANG, "sidebar.rank");
    public static final IMessage SIDEBAR_CARDS_LEFT = new Message(LANG, "sidebar.cards_left");
    public static final IMessage SIDEBAR_SERVER = new Message(LANG, "sidebar.server");
    public static final IMessage SIDEBAR_URL = new Message(LANG, "sidebar.url");

    public static final IMessage BUTTON_RULEBOOK_NAME = new Message(LANG, "button.rulebook.name");
    public static final IMessage BUTTON_RULEBOOK_TITLE = new Message(LANG, "button.rulebook.title");
    public static final IMessage BUTTON_RULEBOOK_AUTHOR = new Message(LANG, "button.rulebook.author");
    public static final IMessage BUTTON_SPECTATE_NAME = new Message(LANG, "button.spectate.name");
    public static final IMessage BUTTON_JOIN_NAME = new Message(LANG, "button.join.name");
    public static final IMessage BUTTON_EXIT_NAME = new Message(LANG, "button.exit.name");

    public static final IMessage GUI_SPECTATOR_TITLE = new Message(LANG, "gui.spectator.title");
    public static final IMessage GUI_SPECTATOR_TELEPORT = new Message(LANG, "gui.spectator.teleport");
    public static final IMessage GUI_DECK_TITLE = new Message(LANG, "gui.deck.title");
    public static final IMessage GUI_DECK_LOADING = new Message(LANG, "gui.deck.loading");

    private Messages(){}
}
