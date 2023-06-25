package nl.scoutcraft.levendstratego.game;

import nl.scoutcraft.levendstratego.LevendStratego;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

public class Deck {

    private static final Map<Rank, Integer> SET_CARDS = getSetCards(LevendStratego.getInstance().getConfig());

    private final Deque<Rank> deck;

    /**
     * Deck constructor. Automatically fills the deck.
     */
    public Deck(int sets) {
        this.deck = new ArrayDeque<>();
        this.fill(sets);
    }

    public void fill(int sets) {
        this.deck.clear();

        List<Rank> set = new ArrayList<>(20);
        for (int i = 0; i < sets; i++) {

            // Adds all cards to the set in order.
            SET_CARDS.forEach((k, v) -> {
                for (; v > 0; v--)
                    set.add(k);
            });

            // Shuffles the set and adds it to the deck.
            Collections.shuffle(set);
            deck.addAll(set);
            set.clear();
        }
    }

    public Rank pop() {
        return this.deck.pop();
    }

    public void addBack(Rank rank) {
        this.deck.addFirst(rank);
    }

    public int size() {
        return this.deck.size();
    }

    public boolean isEmpty() {
        return this.deck.isEmpty();
    }

    private static Map<Rank, Integer> getSetCards(FileConfiguration config) {
        return Arrays.stream(Rank.values()).collect(Collectors.toMap(r -> r, r -> config.getInt("game.deck.cards." + r.getName())));
    }
}
