package alt.elfocrash.roboto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;

public enum FFFFFakePlayerNameManager {
	INSTANCE;
	
	public static final Logger _log = Logger.getLogger(FFFFFakePlayerNameManager.class.getName());
	private List<String> _fffffakePlayerNames;
	
	public void initialise() {
		loadWordlist();
	}
	
	public String getRandomAvailableName() {
		String name = getRandomNameFromWordlist();
		
		while(nameAlreadyExists(name)) {
			name = getRandomNameFromWordlist();
		}
		
		return name;
	}
	
	private String getRandomNameFromWordlist() {
		return _fffffakePlayerNames.get(Rnd.get(0, _fffffakePlayerNames.size() - 1));
	}
	
	public List<String> getFFFFFakePlayerNames() {
		return _fffffakePlayerNames;
	}
	
	private void loadWordlist()
    {
        try(LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(new File("./data/fffffakenamewordlist.txt"))));)
        {
            String line;
            ArrayList<String> playersList = new ArrayList<String>();
            while((line = lnr.readLine()) != null)
            {
                if(line.trim().length() == 0 || line.startsWith("#"))
                    continue;
                playersList.add(line);
            }
            _fffffakePlayerNames = playersList;
            _log.log(Level.INFO, String.format("Loaded %s fffffake player names.", _fffffakePlayerNames.size()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	
	private boolean nameAlreadyExists(String name) {
		return PlayerInfoTable.getInstance().getPlayerObjectId(name) > 0;
	}
}
