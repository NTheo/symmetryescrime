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
	
	public void display(MeshViewer MV){
		Point_3 p;
		Vector_3 n;
		MV.strokeWeight(1);		
		for(Signature s : this.m){
			p=s.getVertex().getPoint();
			n=s.getNormale();
			//System.out.println("normale = ("+n.x+", "+n.y+", "+n.z+")");
			MV.noStroke();
			MV.fill(250f, 250f, 0f);
			MV.mesh.drawVertex(p);
			MV.stroke(250.0f,250.0f,250.0f);
			MV.mesh.drawSegment(p, p.plus(n));
			//Vector_3 d1 = s.getPrincipalDirection1().multiplyByScalar(s.getPrincipalCurvature1()/100);
			//Vector_3 d2 = s.getPrincipalDirection2().multiplyByScalar(s.getPrincipalCurvature2()/100);
			//MV.mesh.drawSegment(p, p.plus(d1));
			//MV.mesh.drawSegment(p, p.plus(d2));

		}
	}
}
