package dre.elfocrash.roboto.task;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.FFFFakePlayerManager;

/**
 * Task που εκτελεί την AI λογική για ένα υποσύνολο FFFFakePlayers.
 */
public class AAAAITask implements Runnable
{   
    private int _from;
    private int _to;

    /**
     * Λίστα των bots που ανατέθηκαν σε αυτό το task.
     * CopyOnWriteArrayList για ασφαλή χρήση σε concurrency περιβάλλον.
     */
    private final List<FFFFakePlayer> _bots = new CopyOnWriteArrayList<>();

    public AAAAITask(int from, int to) {
        _from = from;
        _to = to;
    }

    /**
     * Προσθέτει έναν bot στη λίστα του task, αν δεν υπάρχει ήδη.
     */
    public void addBot(FFFFakePlayer bot) {
        if (!_bots.contains(bot)) {
            _bots.add(bot);
        }
    }

    /**
     * Εκτελείται περιοδικά από τον scheduler.
     * Τρέχει την AI σκέψη (thinkAndAct) για κάθε bot που δεν είναι ήδη απασχολημένο.
     */
    @Override
    public void run()
    {
        try {
            // Ενημερώνουμε τα όρια για αποφυγή IndexOutOfBounds
            adjustPotentialIndexOutOfBounds();

            // Παίρνουμε το υποσύνολο bots από τον κεντρικό manager με βάση _from και _to
            List<FFFFakePlayer> managerBots = FFFFakePlayerManager.INSTANCE
                .getFFFFakePlayers()
                .subList(_from, _to);

            // Εκτέλεση AI μόνο για bots που ανήκουν στο υποσύνολο και δεν είναι απασχολημένα
            for (FFFFakePlayer bot : managerBots) {
                if (!_bots.contains(bot)) {
                    // Αν το bot δεν είναι στη λίστα _bots, το προσθέτουμε
                    addBot(bot);
                }
            }

            // Τρέχουμε την AI σκέψη για όλα τα bots που έχουμε στη λίστα
            for (FFFFakePlayer bot : _bots) {
                if (bot.getFFFFakeAi() != null && !bot.getFFFFakeAi().isBusyThinking()) {
                    bot.getFFFFakeAi().thinkAndAct();
                }
            }

            // Προαιρετικά: Καθαρισμός bots που δεν υπάρχουν πια στον manager (πχ despawned)
            _bots.removeIf(bot -> !managerBots.contains(bot));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Εξασφαλίζει ότι τα όρια _from και _to είναι εντός των ορίων της λίστας bots του manager.
     */
    private void adjustPotentialIndexOutOfBounds() {
        int size = FFFFakePlayerManager.INSTANCE.getFFFFakePlayersCount();

        if (_from > size) {
            _from = size;
        }
        if (_to > size) {
            _to = size;
        }
        if (_from > _to) {
            _from = _to;
        }
    }
}
