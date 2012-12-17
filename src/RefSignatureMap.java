import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import Jcg.polyhedron.Vertex;

/**
 * All the valid signatures from the sampling
 * @author Antoine & NTheo (2012)
 *
 */
public class RefSignatureMap {
	List<RefSignature> p = new LinkedList<RefSignature>();
	KDTree2<RefSignature> q = new KDTree2<RefSignature>(2);
	
	public RefSignatureMap(Sampling sample){
		System.out.print("Computing refsignatures...");
		RefSignature s;
		for(Vertex v:sample.vertices){
			s = new RefSignature(v);
			if(s.isValid())
				p.add(new RefSignature(v));
		}
		System.out.println("ok");
		System.out.println("After pruning : "+this.p.size()+" signatures.");
		
		Iterator<RefSignature> it = p.iterator();
		while(it.hasNext()){
			s=it.next();
			if(Math.random()>=Parameters.pairingRatio){
				q.add(new double[]{s.getAngleSum()}, s);
				it.remove();
			}
		}
		System.out.println("A subset of "+this.p.size()+" signatures has been extracted.");
	}
	
	// return a list of all the signatures, for display purposes only
	public List<RefSignature> getSignatures(){
		List<RefSignature> res = q.getRange(new double[]{Double.MIN_VALUE,Double.MIN_VALUE}, new double[]{Double.MAX_VALUE,Double.MAX_VALUE});
		res.addAll(p);
		return res;
	}
	
	// display the vertices that remains in the sample AFTER the pruning
	public void displayPoints(MeshViewer MV){
		MV.noStroke();
		MV.fill(250f, 250f, 0f);
		for(RefSignature s : this.getSignatures()){
			MV.mesh.drawVertex(s.getVertex().getPoint());
		}
	}
	
	// display the angleSum for each point 
	public void displayNormals(MeshViewer MV){
		Point_3 p;
		Vector_3 n;
		MV.strokeWeight(1);		
		for(RefSignature s : this.getSignatures()){
			p=s.getVertex().getPoint();
			n=s.getNormale();
			
			MV.stroke(0.f,(float)(s.getAngleSum()-5.)*200,(float)(s.getAngleSum()-5.)*200);
			MV.mesh.drawSegment(p, p.plus(n));
		}
	}
	
	// display the normalized normal and principal directions for each point
	public void displayNormalized(MeshViewer MV){
		displayNormals(MV);
	}
	
	// display the normalized normal and pseudo-normalized principal directions (on a 0.5 to 1 scale)
	public void displayCustom(MeshViewer MV){
		displayNormals(MV);
	}
	
	
	// For each signature in p, draw a sphere. The darker the color, the less neighbors this signature has in the signature space
	public void displayNeighborsNumber(MeshViewer mv){
		return;
	}
	
	
	
}