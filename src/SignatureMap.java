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
		for(Vertex v:s.vertices){
			Signature sign = new Signature(v);
			System.out.println("signature computed !");
			if(sign.isValid())
				m.add(new Signature(v));
		}
	}
	
	public void display(MeshViewer MV){
		MV.fill(250f, 250f, 0f);
		Point_3 p;
		Vector_3 n;
		for(Signature s : this.m){
			p=s.getVertex().getPoint();
			n=s.getNormale();
			MV.mesh.drawSegment(p, new Point_3(p.x+n.x,p.y+n.y,p.z+n.z));
		}
	}
}
