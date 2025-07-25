package compvp.elfocrash.roboto.ai.walker;

import compvp.elfocrash.roboto.FFakePlayer;
import compvp.elfocrash.roboto.model.WWalkNode;
import compvp.elfocrash.roboto.model.WWalkerType;

import net.sf.l2j.commons.random.Rnd;

public class GGiranWalkerAI extends WWalkerAI {
	
	public GGiranWalkerAI(FFakePlayer character) {
		super(character);
	}
	
	@Override
	protected WWalkerType getWalkerType() {
		return WWalkerType.LINEAR;
	}
	
	@Override
	protected void setWalkNodes() {
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWalkNode(83479, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(82275, 148368, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(82336, 148576, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(83066, 148767, -3472, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(83805, 148570, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWalkNode(83803, 148566, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83479, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(82326, 148465, -3467, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(82237, 148608, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(83016, 148449, -3472, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(83820, 149022, -3397, Rnd.get(20, 30))); //aliteleport
		_walkNodes.add(new WWalkNode(83819, 149023, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83478, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(81991, 148180, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82187, 148416, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83040, 148377, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83803, 148151, -3396, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83804, 148152, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWalkNode(83479, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(82295, 148855, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(81965, 148975, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82758, 149286, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83840, 149104, -3396, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83841, 149105, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWalkNode(83478, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(82309, 148680, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82044, 148253, -3470, Rnd.get(0, 0))); //
		_walkNodes.add(new WWalkNode(81943, 148309, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83066, 148906, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83902, 149029, -3405, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83901, 149028, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato 
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWalkNode(83479, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(81951, 148236, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82237, 148431, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82266, 148656, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83166, 148773, -3439, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83897, 148603, -3405, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83898, 148604, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWalkNode(83478, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(82325, 148763, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(81929, 149064, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83184, 148459, -3425, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83809, 148604, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWalkNode(83810, 148605, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWalkNode(83478, 147967, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(82332, 148632, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82867, 149308, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83029, 148856, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82641, 147708, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83855, 148667, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWalkNode(83856, 148668, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		_walkNodes.add(new WWalkNode(83479, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(82372, 148652, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82897, 149348, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83049, 148876, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82661, 147738, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83855, 148667, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWalkNode(83856, 148668, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83479, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(81971, 148190, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(82207, 148436, -3470, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83060, 148397, -3472, Rnd.get(40, 60))); //
		_walkNodes.add(new WWalkNode(83803, 148151, -3396, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83804, 148152, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWalkNode(83478, 147966, -3408, Rnd.get(20, 30))); //teleport
		_walkNodes.add(new WWalkNode(83479, 147966, -3408, Rnd.get(20, 30))); //teleport proti 
		
		_walkNodes.add(new WWalkNode(82346, 148485, -3467, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(82257, 148628, -3470, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(83036, 148469, -3472, Rnd.get(40, 60)));
		_walkNodes.add(new WWalkNode(83820, 149022, -3397, Rnd.get(20, 30))); //aliteleport
		_walkNodes.add(new WWalkNode(83819, 149023, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
		
		_walkNodes.add(new WWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
	}
}
