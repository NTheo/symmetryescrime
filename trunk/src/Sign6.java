import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;

/**
 * signatures for transformations: uniform scaling, rotation, translation
 * @author Antoine & NTheo (2012)
 *
 */
public class Sign6 extends Sign {

	public double k1;
	public double k2; //according to the paper, k2>=k1
	
	public void computeSign(SurfaceMesh m, Vertex<Point_3> v) {
		double[] k = super.signatures(m, v);
		if(k[0]>k[1]){
			k1 = k[1];
			k2 = k[0];
		} else {
			k1 = k[0];
			k2 = k[1];
		}
	}

	public boolean isValid() {
		return (k1>0. && k2/k1 > threshold);
	}

}
