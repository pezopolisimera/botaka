package alt.elfocrash.roboto.ai.walker;

import alt.elfocrash.roboto.FFFFFakePlayer;
import alt.elfocrash.roboto.model.WWWWWalkNode;
import alt.elfocrash.roboto.model.WWWWWalkerType;

import net.sf.l2j.commons.random.Rnd;

public class GGGGGiranWalkerAI extends WWWWWalkerAI {
	
	public GGGGGiranWalkerAI(FFFFFakePlayer character) {
		super(character);
	}
	
	@Override
	protected WWWWWalkerType getWalkerType() {
		return WWWWWalkerType.LINEAR;
	}
	
	@Override
	protected void setWalkNodes() {
		_walkNodes.add(new WWWWWalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WWWWWalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WWWWWalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WWWWWalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WWWWWalkNode(83478, 147966, -3408, Rnd.get(2, 2)));
		_walkNodes.add(new WWWWWalkNode(83479, 147967, -3408, Rnd.get(6, 6)));
		
		_walkNodes.add(new WWWWWalkNode(82288, 148475, -3470, Rnd.get(8, 8)));
		_walkNodes.add(new WWWWWalkNode(82463, 149099, -3472, Rnd.get(290, 290))); //kani boltes kato ap ta skalia tis giran kontines 
		_walkNodes.add(new WWWWWalkNode(82575, 149160, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82741, 149143, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82832, 148894, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82805, 148486, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82399, 148411, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82700, 148249, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82414, 148121, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83028, 148183, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83054, 148629, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(83049, 148891, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83054, 148480, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(83044, 147971, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(83055, 147789, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82577, 148094, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82351, 148448, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82264, 148366, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82368, 148700, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82813, 148565, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83033, 149122, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82305, 148235, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82434, 148070, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82842, 148302, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83106, 148167, -3463, Rnd.get(290, 290)));  
		_walkNodes.add(new WWWWWalkNode(82961, 149123, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82878, 149129, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82747, 149153, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82644, 149168, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82407, 149103, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82291, 148978, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82217, 148883, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82255, 149182, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82376, 149270, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82478, 149237, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82464, 149167, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82171, 149070, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82316, 149085, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82416, 148969, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82510, 149052, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82647, 149059, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82810, 149026, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82689, 148924, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82531, 148918, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82342, 148871, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82470, 148834, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82654, 148838, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82588, 149027, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82933, 148749, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82760, 148720, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82573, 148728, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82408, 148716, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82465, 148600, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82649, 148651, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82612, 148512, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82956, 148409, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82600, 148415, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82728, 148413, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82426, 148398, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82984, 148665, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82930, 148930, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82520, 148294, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82858, 148124, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82967, 147898, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82731, 148032, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82674, 148300, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82526, 147875, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82350, 147741, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82273, 147991, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82368, 148135, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82330, 147931, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82611, 147764, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82876, 148666, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82533, 148207, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82488, 148500, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82659, 147960, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82910, 149272, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82280, 148047, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82818, 147965, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82476, 147741, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82194, 148623, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82908, 148825, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82463, 149199, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82675, 149260, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82841, 149343, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82932, 148994, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82905, 148586, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82499, 148511, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82800, 148349, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82514, 148221, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83228, 148383, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83154, 148729, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(83149, 148991, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83154, 148580, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(83144, 147971, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(83155, 147889, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82677, 148194, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82451, 148548, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82364, 148466, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82568, 148800, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82913, 148665, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83133, 149222, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82405, 148335, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82534, 148170, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82942, 148402, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83206, 148267, -3463, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83061, 149223, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82978, 149229, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82847, 149253, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82744, 149268, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82507, 149203, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82391, 148978, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82317, 148983, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82355, 149282, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82476, 149370, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82578, 149437, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82564, 149267, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82271, 149170, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82416, 149185, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82516, 148969, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82610, 149152, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82747, 149159, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82910, 149126, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82789, 148924, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82631, 148918, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82442, 148971, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82570, 148734, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82754, 148738, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82688, 149127, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82999, 148649, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82860, 148820, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82673, 148828, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82508, 148816, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82565, 148700, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82749, 148751, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82712, 148612, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82999, 148509, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82700, 148515, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82828, 148513, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82526, 148498, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83084, 148765, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83030, 148830, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82620, 148394, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82958, 148224, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82467, 147998, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82831, 148132, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82774, 148400, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82626, 147975, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82450, 147841, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82373, 147891, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82468, 148235, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82430, 147831, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82711, 147864, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82976, 148766, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82633, 148307, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82588, 148600, -3472, Rnd.get(290, 290)));	
		_walkNodes.add(new WWWWWalkNode(82759, 147860, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82916, 149372, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82380, 148147, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82918, 147865, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82576, 147841, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82294, 148723, -3470, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(82968, 148725, -3472, Rnd.get(290, 290)));
		_walkNodes.add(new WWWWWalkNode(83805, 148570, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WWWWWalkNode(83803, 148566, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WWWWWalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
	}
}
