import java.util.LinkedList;

import Jcg.geometry.Point_3;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.polyhedron.Vertex;


/**
 * This class performs a random sampling of the model
 * @author Antoine & NTheo (2012)
 *
 */
public class RandomSampling extends Sampling {
	
	public RandomSampling(Polyhedron_3 polyhedron) {
		// NB: we should use Iterator.remove for the pruning, in O(1) instead of O(n) for remove()
		this.vertices=new LinkedList<Vertex>();
		int psv=polyhedron.sizeOfVertices();
		for(int i=0;i<psv*Parameters.samplingRatio;i++){
			vertices.add(polyhedron.vertices.get((int) (Math.random()*psv)));
		}
		System.out.println("Sampling : "+vertices.size()+" points selected.");
	}

	@Override
	void displaySample() {
		
	}


}
