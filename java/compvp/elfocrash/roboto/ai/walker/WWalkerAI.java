package compvp.elfocrash.roboto.ai.walker;

import java.util.LinkedList;
import java.util.Queue;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.ai.FFakePlayerAI;
import compvp.elfocrash.roboto.model.WWalkNode;
import compvp.elfocrash.roboto.model.WWalkerType;

import net.sf.l2j.commons.random.Rnd;

public abstract class WWalkerAI extends FFakePlayerAI {

	protected Queue<WWalkNode> _walkNodes;
	private WWalkNode _currentWalkNode;
	private int currentStayIterations = 0;
	protected boolean isWalking = false;
	
	public WWalkerAI(FFakePlayer character) {
		super(character);
	}
	
	public Queue<WWalkNode> getWalkNodes(){
		return _walkNodes;
	}
	
	protected void addWalkNode(WWalkNode walkNode) {
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
					_currentWalkNode = (WWalkNode) getWalkNodes().toArray()[Rnd.get(0, getWalkNodes().size() - 1)];
					break;
				case LINEAR:
					_currentWalkNode = getWalkNodes().poll();
					_walkNodes.add(_currentWalkNode);
					break;
			}
			_ffakePlayer.getFFakeAi().moveTo(_currentWalkNode.getX(), _currentWalkNode.getY(), _currentWalkNode.getZ());	
			isWalking = true;				
		}
		
		setBusyThinking(false);
	}

	@Override
	protected int[][] getBuffs() {
		return new int[0][0]; 
	}

	protected boolean userReachedDestination(WWalkNode targetWalkNode) {
		//TODO: Improve this with approximate equality and not strict
		if(_ffakePlayer.getX() == targetWalkNode.getX()
			&& _ffakePlayer.getY() == targetWalkNode.getY() 
			&& _ffakePlayer.getZ() == targetWalkNode.getZ())
			return true;
		
		return false;
	}
	
	protected abstract WWalkerType getWalkerType();
	protected abstract void setWalkNodes();
}
