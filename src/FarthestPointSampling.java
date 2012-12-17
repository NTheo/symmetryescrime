import Jcg.polyhedron.Polyhedron_3;
import Jcg.polyhedron.Vertex;


/**
 * This class performs a uniform sampling of the model, based on Farthest Point
 * @author Antoine & NTheo (2012)
 *
 */
public class FarthestPointSampling extends Sampling {
	
	public FarthestPointSampling(Polyhedron_3 polyhedron) {
		super(polyhedron);
		
		int psv=polyhedron.sizeOfVertices();
		Vertex prev;
		Vertex vmax=new Vertex();
		Double max=0.d;
		prev=polyhedron.vertices.get((int)Math.random()*psv);
		
		// Initialization
		for(Vertex v:polyhedron.vertices){
			v.d=Double.MAX_VALUE;
		}
		
		while(this.vertices.size()<this.size){
			max=0.d;
			for(Vertex v:polyhedron.vertices){
				v.d=Math.min(v.d,(Double) v.getPoint().distanceFrom(prev.getPoint()));
				if(v.d>max){
					max=v.d;
					vmax=v;
				}					
			}
			this.vertices.add(vmax);
			vmax.d=0.d;
			prev=vmax;
		}
		
		// max is the distance between the 2 closest points
		System.out.println("max = "+max);
		Parameters.radius=max.floatValue();
		
		System.out.println("ok");
		System.out.println("Sampling : "+vertices.size()+" points selected ("+Parameters.samplingRatio*100+" %)");
	}

}
