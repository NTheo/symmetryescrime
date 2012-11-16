import Jcg.polyhedron.Vertex;

/**
 * signatures for transformations: uniform scaling, rotation, translation
 * @author Antoine & NTheo (2012)
 *
 */
public class Sign6 implements Sign {

	public double k1;
	public double k2; //according to the paper, k2>=k1
	
	public void computeSign(SurfaceMesh m, Vertex v) {
		double[] k = v.curvatures(radius);
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

	public double compare(Sign sign2) {
		// TODO Auto-generated method stub
		return 0;
	}

}
