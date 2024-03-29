import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import Jcg.polyhedron.Vertex;

/**
 * This class computes all the signatures of the points in the sample, prune them,
 * and split them into a list and a KD-Tree, which will be used for pairing
 * @author Antoine & NTheo (2012)
 *
 */
public class SignatureMap {
	List<Signature> p = new LinkedList<Signature>();
	KDTree<Signature> q = new KDTree<Signature>(2);
	
	public SignatureMap(Sampling sample){
		System.out.print("Computing signatures...");
		Signature s;
		for(Vertex v:sample.vertices){
			s = new Signature(v);
			if(s.isValid())
				p.add(new Signature(v));
		}
		System.out.println("ok");
		System.out.println("After pruning : "+this.p.size()+" signatures.");
		
		Iterator<Signature> it = p.iterator();
		while(it.hasNext()){
			s=it.next();
			if(Math.random()>=Parameters.pairingRatio){
				q.add(new double[]{s.getPrincipalCurvature1(),s.getPrincipalCurvature2()}, s);
				it.remove();
			}
		}
		System.out.println("A subset of "+this.p.size()+" signatures has been extracted.");
	}
	
	// return a list of all the signatures, for display purposes only
	public List<Signature> getSignatures(){
		List<Signature> res = q.getRange(new double[]{Double.MIN_VALUE,Double.MIN_VALUE}, new double[]{Double.MAX_VALUE,Double.MAX_VALUE});
		res.addAll(p);
		return res;
	}
	
	// display the vertices that remains in the sample AFTER the pruning
	public void displayPoints(MeshViewer MV){
		MV.noStroke();
		MV.fill(250f, 250f, 0f);
		for(Signature s : this.getSignatures()){
			MV.mesh.drawVertex(s.getVertex().getPoint());
		}
	}
	
	// display the normal and principal directions for each point 
	public void displayNormals(MeshViewer mv){
		Point_3 p;
		Vector_3 n;
		mv.strokeWeight(1);		
		for(Signature s : this.getSignatures()){
			p=s.getVertex().getPoint();
			n=s.getNormale();
			Vector_3 d1 = s.getPrincipalDirection1().multiplyByScalar(s.getPrincipalCurvature1()/100);
			Vector_3 d2 = s.getPrincipalDirection2().multiplyByScalar(s.getPrincipalCurvature2()/100);
			
			mv.stroke(250.0f,250.0f,250.0f);
			mv.mesh.drawSegment(p, p.plus(n));
			mv.stroke(250.0f,250.0f,0);
			mv.mesh.drawSegment(p, p.plus(d1));
			mv.stroke(0,250.0f,250.0f);
			mv.mesh.drawSegment(p, p.plus(d2));
		}
	}
	
	// display the normalized normal and principal directions for each point
	public void displayNormalized(MeshViewer mv){
		Point_3 p;
		Vector_3 n;
		mv.strokeWeight(1);	
		double scale=.7;
		for(Signature s : this.getSignatures()){
			p=s.getVertex().getPoint();
			n=s.getNormale().normalized();
			Vector_3 d1 = s.getPrincipalDirection1().normalized();
			Vector_3 d2 = s.getPrincipalDirection2().normalized();
			
			n=n.multiplyByScalar(scale);
			d1=d1.multiplyByScalar(scale);
			d2=d2.multiplyByScalar(scale);
			
			mv.stroke(250.0f,250.0f,250.0f);
			mv.mesh.drawSegment(p, p.plus(n));
			mv.stroke(250.0f,250.0f,0);
			mv.mesh.drawSegment(p, p.plus(d1));
			mv.stroke(0,250.0f,250.0f);
			mv.mesh.drawSegment(p, p.plus(d2));
		}
	}
	
	// display the normalized normal and pseudo-normalized principal directions (on a 0.5 to 1 scale)
	public void displayCustom(MeshViewer mv){
		Point_3 p;
		Vector_3 n;
		mv.strokeWeight(1);	
		
		double min1=Double.MAX_VALUE;
		double min2=Double.MAX_VALUE;
		double max1=0;
		double max2=0;
		for(Signature s : this.getSignatures()){
			min1=Math.min(min1, s.getPrincipalCurvature1());
			min2=Math.min(min2, s.getPrincipalCurvature2());
			max1=Math.max(max1, s.getPrincipalCurvature1());
			max2=Math.max(max2, s.getPrincipalCurvature2());
		}
		
		for(Signature s : this.getSignatures()){
			p=s.getVertex().getPoint();
			n=s.getNormale().normalized();
			Vector_3 d1 = s.getPrincipalDirection1().normalized();
			Vector_3 d2 = s.getPrincipalDirection2().normalized();
			
			n=n.multiplyByScalar(Parameters.radius);
			d1=d1.multiplyByScalar((s.getPrincipalCurvature1()-min1)/(max1-min1)+.2).multiplyByScalar(Parameters.radius);
			d2=d2.multiplyByScalar((s.getPrincipalCurvature2()-min2)/(max2-min2)+.2).multiplyByScalar(Parameters.radius);
			
			mv.stroke(250.0f,250.0f,250.0f);
			mv.mesh.drawSegment(p, p.plus(n));
			mv.stroke(250.0f,250.0f,0);
			mv.mesh.drawSegment(p, p.plus(d1));
			mv.stroke(0,250.0f,250.0f);
			mv.mesh.drawSegment(p, p.plus(d2));
			
		}
	}
	
	// display sphere with Parameters.radius as the radius
	public void displaySpheres(MeshViewer mv){
		Point_3 p;
		float scale = (float) mv.mesh.scaleFactor;
		mv.noStroke();
		mv.fill(250.f,250.f,250.f);
		for(Signature s: this.getSignatures()){
			p=s.getVertex().getPoint();
			mv.translate(scale*p.x.floatValue(), scale*p.y.floatValue(), scale*p.z.floatValue());
			mv.sphere(scale * ((float) Parameters.radius));
			mv.translate(-scale*p.x.floatValue(), -scale*p.y.floatValue(), -scale*p.z.floatValue());
		}
	}
		
	// For each signature in p, draw a sphere. The darker the color, the less neighbors this signature has in the signature space
	public void displayNeighborsNumber(MeshViewer mv){
		List<Signature> neighbors;
		for(Signature s: this.p){
			neighbors = this.q.getRange(new double[]{s.getPrincipalCurvature1()-Parameters.pairingRange/2,
					s.getPrincipalCurvature2()-Parameters.pairingRange/2},
					new double[]{s.getPrincipalCurvature1()+Parameters.pairingRange/2,
					s.getPrincipalCurvature2()+Parameters.pairingRange/2});
			mv.noStroke();
			mv.fill(6*neighbors.size()%255, 2*neighbors.size()%255, 6*neighbors.size()%255);		
			mv.mesh.drawVertex(s.getVertex().getPoint());
		}
	}
	
	// Display the points that lie within the pairing range of a point in the Signature Space.
	public void displayCorrespondingPointsInSignatureSpace(MeshViewer mv){
		Signature s = this.p.get(Main.viewIndex%p.size());
		List<Signature> neighbors = this.q.getRange(new double[]{s.getPrincipalCurvature1()-Parameters.pairingRange/2,
				s.getPrincipalCurvature2()-Parameters.pairingRange/2},
				new double[]{s.getPrincipalCurvature1()+Parameters.pairingRange/2,
				s.getPrincipalCurvature2()+Parameters.pairingRange/2});
		
		mv.noStroke();
		mv.fill(255,0,0);
		mv.mesh.drawVertex(s.getVertex().getPoint());
		
		mv.fill(255,255,0);
		for(Signature t : neighbors)
			mv.mesh.drawVertex(t.getVertex().getPoint());
	}
	
}
