package dre.elfocrash.roboto.task;

import java.util.ArrayList;
import java.util.List;
import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.FFFFakePlayerManager;

public class AAAAITask implements Runnable
{
    private int _from;
    private int _to;
    private final List<FFFFakePlayer> _localBots = new ArrayList<>();

    public AAAAITask(int from, int to) {
        _from = from;
        _to = to;
    }

    @Override
    public void run()
    {
        try {
            _localBots.stream()
                .filter(bot -> !bot.getFFFFakeAi().isBusyThinking())
                .forEach(bot -> bot.getFFFFakeAi().thinkAndAct());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addBot(FFFFakePlayer bot) {
        _localBots.add(bot);
    }

    public void removeBot(FFFFakePlayer bot) {
        _localBots.remove(bot);
    }
}
