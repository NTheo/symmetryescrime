import java.util.LinkedList;
import java.util.List;

import Jcg.polyhedron.Vertex;

/**
 * all the signatures from the sampling
 * @author Antoine & NTheo (2012)
 *
 */
public class SignatureMap {
	List<Signature> m = new LinkedList<Signature>();
	public SignatureMap(Sampling s){
		for(Vertex v:s.vertices){
			Signature sign = new Signature(v);
			if(sign.isValid())
				m.add(new Signature(v));
		}
	}
}
