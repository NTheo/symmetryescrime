import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Jcg.polyhedron.Vertex;


public class Cluster {
	public Reflection r;
	public int weight;
	public List<pair>  l;
	Cluster(Reflection r, int i, ArrayList<Reflection> ref){
		this.r=r;
		weight=i;
		l = new LinkedList<pair>();
		for(Reflection t:ref){
			l.add(new pair(t.getV1(), t.getV2()));
		}
	}
}

