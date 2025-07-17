package com.elfocrash.roboto.ai.walker;

import com.elfocrash.roboto.FakePlayer;
import com.elfocrash.roboto.model.WalkNode;
import com.elfocrash.roboto.model.WalkerType;

import net.sf.l2j.commons.random.Rnd;

public class GiranWalkerAI extends WalkerAI {
	
	public GiranWalkerAI(FakePlayer character) {
		super(character);
	}
	
	@Override
	protected WalkerType getWalkerType() {
		return WalkerType.LINEAR;
	}
	
	@Override
	protected void setWalkNodes() {
		
		_walkNodes.add(new WalkNode(83539, 146609, -16360, Rnd.get(2, 2))); //otan girnai prin stamatisi
		_walkNodes.add(new WalkNode(83489, 146581, -16360, Rnd.get(1200, 1200))); //pano apo trapeza
		_walkNodes.add(new WalkNode(83805, 147809, -16360, Rnd.get(3, 3))); //
		_walkNodes.add(new WalkNode(83479, 147967, -16360, Rnd.get(3, 3))); //
		
		_walkNodes.add(new WalkNode(83478, 147966, -3408, Rnd.get(2, 2)));
		_walkNodes.add(new WalkNode(83479, 147967, -3408, Rnd.get(5, 5)));
		
		_walkNodes.add(new WalkNode(82303, 148767, -3470, Rnd.get(10, 10)));
		_walkNodes.add(new WalkNode(83541, 147944, -3408, Rnd.get(290, 290)));	//boltes pano ap ta skalia ti giran	
		_walkNodes.add(new WalkNode(83371, 147990, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83390, 148112, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83433, 148143, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83592, 148196, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83596, 148339, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83418, 148339, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83298, 148360, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83515, 148460, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83338, 148434, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83278, 148543, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83452, 148567, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83587, 148557, -3408, Rnd.get(290, 290)));  
		_walkNodes.add(new WalkNode(83518, 147986, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83281, 147981, -3408, Rnd.get(290, 290)));		
		_walkNodes.add(new WalkNode(83286, 148102, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83519, 148100, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83640, 148159, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83640, 148159, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83283, 148235, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83421, 148235, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83562, 148247, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83664, 148280, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83489, 148325, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83287, 148327, -3405, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83575, 148384, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83422, 148431, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83688, 148569, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83273, 148679, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83377, 148597, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83296, 149144, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83478, 149152, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83660, 149179, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83656, 149103, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83663, 148969, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83667, 148868, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83672, 148724, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83676, 148454, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83527, 148454, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83271, 148788, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83287, 148902, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83273, 149043, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83292, 149279, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83368, 149225, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83519, 149244, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83631, 149327, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83593, 149174, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83395, 149146, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83488, 148592, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83547, 148741, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83439, 148720, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83357, 148765, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83571, 148641, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83571, 148855, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83446, 148873, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83561, 149008, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83397, 148971, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83369, 149072, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83504, 148977, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83475, 149055, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83507, 148814, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83370, 148861, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83454, 148532, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83509, 148195, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83355, 148200, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83457, 148027, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83606, 148080, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83344, 148328, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83376, 148676, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83580, 149088, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83249, 148937, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83518, 148677, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83442, 148782, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83334, 148987, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83519, 148911, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83625, 148746, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83603, 148928, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83452, 148995, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83337, 148590, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83374, 148494, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83662, 148635, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83611, 148478, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83335, 148680, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83573, 148551, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83456, 148669, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83514, 148395, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83516, 148542, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83415, 148628, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83615, 148684, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83266, 148441, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83346, 148074, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83643, 148006, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83641, 147844, -3408, Rnd.get(290, 290)));		
		_walkNodes.add(new WalkNode(83471, 147890, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83490, 148212, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83533, 148243, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83692, 148296, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83696, 148439, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83518, 148439, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83398, 148460, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83615, 148560, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83438, 148534, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83378, 148643, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83552, 148667, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83687, 148657, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83618, 147886, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83381, 147881, -3408, Rnd.get(290, 290)));		
		_walkNodes.add(new WalkNode(83386, 148202, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83619, 148200, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83740, 148259, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83744, 148259, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83383, 148335, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83521, 148335, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83662, 148347, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83764, 148380, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83589, 148425, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83387, 148427, -3405, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83675, 148484, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83522, 148531, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83788, 148669, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83373, 148779, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83477, 148697, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83396, 149244, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83578, 149252, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83760, 149279, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83756, 149203, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83763, 148869, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83767, 148768, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83772, 148824, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83776, 148554, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83627, 148554, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83371, 148888, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83387, 148802, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83373, 149143, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83392, 149379, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83468, 149325, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83619, 149344, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83731, 149427, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83693, 149274, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83495, 149246, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83588, 148692, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83647, 148841, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83839, 148820, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83457, 148865, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83671, 148741, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83678, 148955, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83546, 148973, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83661, 149108, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83497, 148871, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83469, 149172, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83604, 148877, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83575, 149155, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83607, 148914, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83470, 148961, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83554, 148632, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83609, 148295, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83455, 148300, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83557, 148127, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83706, 148180, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83444, 148428, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83476, 148776, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83680, 149188, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83349, 148837, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83618, 148777, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83542, 148882, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83434, 148887, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83619, 148811, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83725, 148846, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83703, 148828, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83552, 148895, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83437, 148690, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83474, 148594, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83762, 148735, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83711, 148578, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83435, 148780, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83673, 148651, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83556, 148769, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83614, 148495, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83616, 148642, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83515, 148728, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83715, 148784, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83366, 148541, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83446, 148174, -3408, Rnd.get(290, 290)));
		_walkNodes.add(new WalkNode(83743, 148106, -3408, Rnd.get(290, 290)));	
		_walkNodes.add(new WalkNode(83805, 148570, -3396, Rnd.get(20, 30))); //aliteleport 
		_walkNodes.add(new WalkNode(83803, 148566, -2234, Rnd.get(0, 0))); //pano
		_walkNodes.add(new WalkNode(84346, 148445, -2234, Rnd.get(3, 3))); //pano ksekinai to alo apo kato
	}
}
