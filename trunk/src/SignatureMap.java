import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import Jcg.polyhedron.Vertex;

/**
 * All the valid signatures from the sampling
 * @author Antoine & NTheo (2012)
 *
 */
public class SignatureMap {
	List<Signature> m = new LinkedList<Signature>();
	
	public SignatureMap(Sampling s){
		System.out.print("Computing signatures...");
		for(Vertex v:s.vertices){
			Signature sign = new Signature(v);
			//System.out.println("signature computed !");
			if(sign.isValid())
				m.add(new Signature(v));
		}
		System.out.println("ok");
		System.out.println("After pruning : "+this.m.size()+" signatures.");
	}
	
	// display the vertices that remains in the sample AFTER the pruning
	public void displayPoints(MeshViewer MV){
		MV.noStroke();
		MV.fill(250f, 250f, 0f);
		for(Signature s : this.m){
			MV.mesh.drawVertex(s.getVertex().getPoint());
		}
	}
	
	// display the normal and principal directions for each point 
	public void display(MeshViewer MV){
		Point_3 p;
		Vector_3 n;
		MV.strokeWeight(1);		
		for(Signature s : this.m){
			p=s.getVertex().getPoint();
			n=s.getNormale();
			Vector_3 d1 = s.getPrincipalDirection1().multiplyByScalar(s.getPrincipalCurvature1()/100);
			Vector_3 d2 = s.getPrincipalDirection2().multiplyByScalar(s.getPrincipalCurvature2()/100);
			
			MV.stroke(250.0f,250.0f,250.0f);
			MV.mesh.drawSegment(p, p.plus(n));
			MV.stroke(250.0f,250.0f,0);
			MV.mesh.drawSegment(p, p.plus(d1));
			MV.stroke(0,250.0f,250.0f);
			MV.mesh.drawSegment(p, p.plus(d2));
		}
	}
	
	// display the normalized normal and principal directions for each point
	public void displayNormalized(MeshViewer MV){
		Point_3 p;
		Vector_3 n;
		MV.strokeWeight(1);	
		double scale=.7;
		for(Signature s : this.m){
			p=s.getVertex().getPoint();
			n=s.getNormale();
			Vector_3 d1 = s.getPrincipalDirection1().multiplyByScalar(s.getPrincipalCurvature1());
			Vector_3 d2 = s.getPrincipalDirection2().multiplyByScalar(s.getPrincipalCurvature2());
			
			n=n.divisionByScalar(Math.sqrt((Double) n.squaredLength())).multiplyByScalar(scale);
			if((Double) d1.squaredLength()!=0.0d)
				d1=d1.divisionByScalar(Math.sqrt((Double) d1.squaredLength())).multiplyByScalar(scale);
			else
				d1=new Vector_3(0,0,0);
			if((Double) d2.squaredLength()!=0.0d)
				d2=d2.divisionByScalar(Math.sqrt((Double) d2.squaredLength())).multiplyByScalar(scale);
			else
				d2=new Vector_3(0,0,0);
			
			MV.stroke(250.0f,250.0f,250.0f);
			MV.mesh.drawSegment(p, p.plus(n));
			MV.stroke(250.0f,250.0f,0);
			MV.mesh.drawSegment(p, p.plus(d1));
			MV.stroke(0,250.0f,250.0f);
			MV.mesh.drawSegment(p, p.plus(d2));
		}
	}
}
