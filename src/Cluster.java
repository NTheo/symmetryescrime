import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represent a cluster, corresponding to a given reflection
 * @author Antoine & NTheo (2012)
 *
 */
public class Cluster implements Comparable<Cluster> {
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
	
	@Override
	public int compareTo(Cluster c) {
		if(this.l.size()<c.l.size()) return 1;
		if(this.l.size()==c.l.size()) return 0;
		return -1;
	}
}

