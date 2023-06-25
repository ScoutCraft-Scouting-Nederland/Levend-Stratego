package nl.scoutcraft.levendstratego;

import nl.scoutcraft.eagle.libs.locale.Internationalization;
import nl.scoutcraft.levendstratego.command.CommandGlobalChat;
import nl.scoutcraft.levendstratego.command.ForceStartCommand;
import nl.scoutcraft.levendstratego.listener.ChatListener;
import nl.scoutcraft.levendstratego.listener.CombatListener;
import nl.scoutcraft.levendstratego.listener.ConnectionListener;
import nl.scoutcraft.levendstratego.listener.ProtectionListener;
import nl.scoutcraft.levendstratego.manager.BungeeManager;
import nl.scoutcraft.levendstratego.manager.GameManager;
import nl.scoutcraft.levendstratego.manager.ScoreboardManager;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LevendStratego extends JavaPlugin {

    private static LevendStratego instance;
    public static LevendStratego getInstance() {
        return instance;
    }

    private Internationalization lang;
    private GameManager gameManager;
    private ScoreboardManager scoreboardManager;
    private BungeeManager bungeeManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        super.saveDefaultConfig();

        this.lang = Internationalization.builder("messages")
                .setLangDir(super.getDataFolder().toPath().resolve("lang"))
                .setDefaultLocale("nl")
                .setDefaultClassLoader(super.getClassLoader())
                .addDefaultLangFile("nl")
                .addDefaultLangFile("en")
                .build();
        this.gameManager = new GameManager(this);
        this.scoreboardManager = new ScoreboardManager();
        this.bungeeManager = new BungeeManager(this);

        this.registerListeners();
        this.registerCommands();

        this.gameManager.startLobbyPhase();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new ConnectionListener(this.gameManager), this);
        pm.registerEvents(new CombatListener(this.gameManager), this);
        pm.registerEvents(new ProtectionListener(), this);
    }

    private void registerCommands() {
        new ForceStartCommand().register(this);
        new CommandGlobalChat().register(this);
    }

    public Internationalization getLang() {
        return this.lang;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    public BungeeManager getBungeeManager() {
        return this.bungeeManager;
    }
}
