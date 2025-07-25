package dyn.elfocrash.roboto.ai.walker;

import java.util.LinkedList;
import java.util.Queue;

import dyn.elfocrash.roboto.FFFakePlayer;
import dyn.elfocrash.roboto.ai.FFFakePlayerAI;
import dyn.elfocrash.roboto.model.WWWalkNode;
import dyn.elfocrash.roboto.model.WWWalkerType;

import net.sf.l2j.commons.random.Rnd;

public abstract class WWWalkerAI extends FFFakePlayerAI {

	protected Queue<WWWalkNode> _walkNodes;
	private WWWalkNode _currentWalkNode;
	private int currentStayIterations = 0;
	protected boolean isWalking = false;
	
	public WWWalkerAI(FFFakePlayer character) {
		super(character);
	}
	
	public Queue<WWWalkNode> getWalkNodes(){
		return _walkNodes;
	}
	
	protected void addWalkNode(WWWalkNode walkNode) {
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
					_currentWalkNode = (WWWalkNode) getWalkNodes().toArray()[Rnd.get(0, getWalkNodes().size() - 1)];
					break;
				case LINEAR:
					_currentWalkNode = getWalkNodes().poll();
					_walkNodes.add(_currentWalkNode);
					break;
			}
			_fffakePlayer.getFFFakeAi().moveTo(_currentWalkNode.getX(), _currentWalkNode.getY(), _currentWalkNode.getZ());	
			isWalking = true;
		}
		
		setBusyThinking(false);
	}

	@Override
	protected int[][] getBuffs() {
		return new int[0][0]; 
	}

	protected boolean userReachedDestination(WWWalkNode targetWalkNode) {
		//TODO: Improve this with approximate equality and not strict
		if(_fffakePlayer.getX() == targetWalkNode.getX()
			&& _fffakePlayer.getY() == targetWalkNode.getY() 
			&& _fffakePlayer.getZ() == targetWalkNode.getZ())
			return true;
		
		return false;
	}
	
	protected abstract WWWalkerType getWalkerType();
	protected abstract void setWalkNodes();
}
