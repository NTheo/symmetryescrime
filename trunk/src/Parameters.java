import Jcg.geometry.Point_3;
import Jcg.polyhedron.Vertex;


/**
 * All the magic numbers required
 * @author Antoine & NTheo (2012)
 *
 */
public class Parameters {

	static final double samplingRatio=0.2;		// 0.2
	static final double pruningThreshold=0.75;	// 0.75
	static final double pairingRatio=0.2;		// 0.2
	static final double pairingRange=0.5;		// 0.5
	static final double bandwidth = 0.1;			// 0.1
	static final double mergingCoeff = 1;		// 1
	static final double reflectionThreshold=0.05;// 0.05	
	
	static float radius;
	static float minDistance;
	static float maxDistance;

	
	public static void setMinDistance(MeshViewer MV){
		double min=Double.MAX_VALUE;
		double distance;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Vertex v: MV.mesh.polyhedron3D.vertices){
			distance=Math.sqrt(v.getPoint().squareDistance(origin).doubleValue());
			min=Math.min(min,distance);
		}
		minDistance=(float) min;
		System.out.println("minDistance = "+minDistance);
	}
	
	public static void setMaxDistance(MeshViewer MV){
		double max=0;
		double distance;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Vertex v: MV.mesh.polyhedron3D.vertices){
			distance=Math.sqrt(v.getPoint().squareDistance(origin).doubleValue());
			max=Math.max(max,distance);
		}
		maxDistance = (float) max;
		System.out.println("maxDistance = "+maxDistance);
	}
	
	public static void init(MeshViewer MV){
		setMinDistance(MV);
		setMaxDistance(MV);
	}

}


