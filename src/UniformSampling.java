import Jcg.polyhedron.Polyhedron_3;
import Jcg.polyhedron.Vertex;


/**
 * This class performs a uniform sampling of the model
 * @author Antoine & NTheo (2012)
 *
 */
public class UniformSampling extends Sampling {
	
	private boolean isTooClose(Vertex v){		
		for(Vertex u : this.vertices){
			if(u.getPoint().distanceFrom(v.getPoint()).doubleValue() < this.radius)
				return true;
		}
		return false;
	}
	
	public UniformSampling(Polyhedron_3 polyhedron) {
		super(polyhedron);		
		
		int psv=polyhedron.sizeOfVertices();	
		Vertex v;
		while(this.vertices.size()<this.size){
			v=polyhedron.vertices.get((int) (Math.random()*psv));
			int retry=0;
			while(vertices.contains(v)||isTooClose(v)){
				v=polyhedron.vertices.get((int) (Math.random()*psv));
				retry++;
				if(retry>50){
					// Can't find points that far from each others => diminish the radius to the value
					// for which we are sure to find enough points
					this.radius=Math.sqrt(4*minDistance()*minDistance()/this.size);
				}
			}
			vertices.add(v);
		}
		System.out.println("ok");
		System.out.println("Sampling : "+vertices.size()+" points selected ("+Parameters.samplingRatio*100+" %)");
	}

}
