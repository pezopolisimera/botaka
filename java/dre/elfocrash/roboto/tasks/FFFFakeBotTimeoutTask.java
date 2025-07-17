package dre.elfocrash.roboto.tasks;

import dre.elfocrash.roboto.FFFFakePlayerManager;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import java.util.List;

public class FFFFakeBotTimeoutTask implements Runnable {
    @Override
    public void run() {
        List<Player> gmBots = FFFFakePlayerManager.INSTANCE.getGMFakePlayers();
        for (Player bot : gmBots) {
            Long spawnTime = FFFFakeHelpers.getGMBotSpawnTime(bot);
            if (spawnTime == null) continue;

            long ageMinutes = (System.currentTimeMillis() - spawnTime) / 60000;
            if (ageMinutes >= 60 && ageMinutes <= 240) {
                int delay = 60000 * (10 + (int) (Math.random() * 6)); // 10–15 λεπτά
                FFFFakeHelpers.unmarkGMBot(bot);
                ThreadPool.schedule(() -> {
                    if (FFFFakePlayerManager.INSTANCE.getGMFakePlayers().contains(bot)) {
                        bot.deleteMe();
                        FFFFakePlayerManager.INSTANCE.getGMFakePlayers().remove(bot);
                        FFFFakeHelpers.unregisterBotStats(bot);
                    }
                }, delay);
            }
        }
    }
}
