package alt.elfocrash.roboto.ai.walker;

import java.util.LinkedList;
import java.util.Queue;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.ai.FFFFFakePlayerAI;
import alt.elfocrash.roboto.model.WWWWWalkNode;
import alt.elfocrash.roboto.model.WWWWWalkerType;

import net.sf.l2j.commons.random.Rnd;

public abstract class WWWWWalkerAI extends FFFFFakePlayerAI {

	protected Queue<WWWWWalkNode> _walkNodes;
	private WWWWWalkNode _currentWalkNode;
	private int currentStayIterations = 0;
	protected boolean isWalking = false;
	
	public WWWWWalkerAI(FFFFFakePlayer character) {
		super(character);
	}
	
	public Queue<WWWWWalkNode> getWalkNodes(){
		return _walkNodes;
	}
	
	protected void addWalkNode(WWWWWalkNode walkNode) {
		_walkNodes.add(walkNode);
	}
	
	@Override
	public void setup() {
		super.setup();		
		_walkNodes = new LinkedList<>();
		setWalkNodes();
	}
	
	@Override
	public void thinkAndAct() {
		setBusyThinking(true);		
		handleDeath();
		
		if(_walkNodes.isEmpty())
			return;
		
		if(isWalking) {
			if(userReachedDestination(_currentWalkNode)) {
				if(currentStayIterations < _currentWalkNode.getStayIterations() ) {
					currentStayIterations++;
					setBusyThinking(false);
					return;
				}				
				_currentWalkNode = null;
				currentStayIterations = 0;
				isWalking = false;
			}			
		}
		
		if(!isWalking && _currentWalkNode == null) {
			switch(getWalkerType()) {
				case RANDOM:
					_currentWalkNode = (WWWWWalkNode) getWalkNodes().toArray()[Rnd.get(0, getWalkNodes().size() - 1)];
					break;
				case LINEAR:
					_currentWalkNode = getWalkNodes().poll();
					_walkNodes.add(_currentWalkNode);
					break;
			}
			_fffffakePlayer.getFFFFFakeAi().moveTo(_currentWalkNode.getX(), _currentWalkNode.getY(), _currentWalkNode.getZ());	
			isWalking = true;
		}
		
		setBusyThinking(false);
	}

	@Override
	protected int[][] getBuffs() {
		return new int[0][0]; 
	}

	protected boolean userReachedDestination(WWWWWalkNode targetWalkNode) {
		//TODO: Improve this with approximate equality and not strict
		if(_fffffakePlayer.getX() == targetWalkNode.getX()
			&& _fffffakePlayer.getY() == targetWalkNode.getY() 
			&& _fffffakePlayer.getZ() == targetWalkNode.getZ())
			return true;
		
		return false;
	}
	
	protected abstract WWWWWalkerType getWalkerType();
	protected abstract void setWalkNodes();
}
