import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * This class contains the main methods implementing the Mean-Shift clustering
 */
public class MeanShiftClustering {

	KDTree2<Reflection> N;
	KDTree2<Reflection> seeds;
	double sqCvgRad;  // convergence radius
	double sqAvgRad;  // averaging radius (defining the window)
	double sqInflRad;  // influence radius
	double sqMergeRad;  // merging radius
	private double inflRad;
	public ArrayList<Cluster> clusters;

	/**
	 * Main algorithm for detecting all clusters
	 * Clusters are detected iteratively (until all points are processed)
	 * Clusters are merged if required: when the corresponding peaks are close
	 * output: an array of points, of size n
	 *   - first i elements are cluster centers (non null points)
	 *   - remaining n-i elements must be null
	 */
	MeanShiftClustering(KDTree2<Reflection> n){
		System.out.println("Clustering...");
		N = n;
		initMSC(n);
		int clusterIndex = 0;
		clusters = new ArrayList<Cluster>();
		for(Reflection r:N.all()){
			//System.out.println("cluster "+(1+clusterIndex++) +"/"+ N.getRange(low, high).size());
			if(r.cluster<0){
				clusters.add(detectCluster(r, clusterIndex));
			}
		}
		System.out.println(clusters.size()+" clusters detected before merging");
		int iinit = 0;
		do{iinit = mergeCluster(iinit);}while(-1!=iinit);
		System.out.println(clusters.size()+" clusters extracted");
	}

	void initMSC (KDTree2<Reflection> n){
		N = n;
		sqCvgRad = Parameters.bandwidth*0.001;
		sqAvgRad = Parameters.bandwidth;
		sqInflRad = Parameters.bandwidth/4;
		sqMergeRad = Parameters.bandwidth*Parameters.mergingCoeff;
	}

	/**
	 * Single cluster detection -- returns approximate peak and cluster points
	 * The output is a list of point containing:
	 *  - all the points belonging to the detected cluster
	 *  - the peak point (at the top of the list)
	 */
	private final static int d = 6;
	public Cluster detectCluster (Reflection seed, int clusterIndex) { 
		List<Reflection> pointsOfPath = new ArrayList<Reflection>();
		Reflection prev;
		Reflection next;
		next = seed;
		pointsOfPath.add(next);
		double[] low = new double[d];
		double[] high = new double[d];
		do{
			prev = next;
			for(int i = 0; i<d; i++){
				low[i] = prev.r[i]-Math.sqrt(sqAvgRad);
				high[i] = prev.r[i]+Math.sqrt(sqAvgRad);
			}
			List<Reflection> closeRef = N.getRange(low, high);
			List<Reflection> l = new LinkedList<Reflection>();
			for(Reflection re:closeRef){//removing all already-clustered or too far points
				if(re.cluster<0 && KDTree2.pointDistSq(re.r, prev.r) < sqCvgRad)
					l.add(re);
			}
			double[] mean = new double[d];
			double k = (double)l.size();
			for(Reflection r: l) for(int i=0; i<d; i++){
				mean[i]+=r.r[i]/k;
			}
			next = N.getNearestNeighbors(mean, 1).removeMax();
			pointsOfPath.add(next);
			//System.out.println(KDTree2.pointDistSq(next.r, prev.r));
		}while(KDTree2.pointDistSq(next.r, prev.r) > sqCvgRad);
		//computing weight of cluster
		ArrayList<Reflection> cluster = new ArrayList<Reflection>();
		for(Reflection ref: pointsOfPath){
			for(int i = 0; i<d; i++){
				low[i] = ref.r[i]-inflRad;
				high[i] = ref.r[i]+inflRad;
			}
			List<Reflection> l = N.getRange(low, high);
			for(Reflection r:l){
				if(!cluster.contains(r)){
					cluster.add(r);
					r.cluster = clusterIndex;
				}
			}
		}
		return new Cluster(next, cluster.size(), cluster);
	}

	/**
	 * @param list of clusters to merge
	 * @return true iff no merge has been performed
	 */
	public int mergeCluster(int iinit){
		//System.out.println(clusters.size());
		for(int i = iinit; i<clusters.size(); i++){
			for(int j = i+1; j<clusters.size(); j++){
				//System.out.println(i+" "+j);
				if(KDTree2.pointDistSq(clusters.get(i).r.r, clusters.get(j).r.r) < sqMergeRad){
					if (clusters.get(i).weight>clusters.get(j).weight)
						clusters.get(j).r = clusters.get(i).r;
					clusters.get(j).r.weight += clusters.get(i).weight;
					clusters.get(j).l.addAll(clusters.get(i).l);
					clusters.remove(i);
					return i;
				}
			}
		}
		return(-1);
	}


	// Display one cluster and the reflection plane
	public void displayOneClusterAndPairsOfPoints(MeshViewer mv){
		Cluster c = this.clusters.get(Main.viewIndex%this.clusters.size());
		c.r.display(mv);  // plane
		
		for(pair p:c.l){
			mv.noStroke();
			mv.fill(128+c.r.hashCode()%127,128+c.r.hashCode()%127,128+(2*c.r.hashCode())%127);
			mv.mesh.drawVertex(p.first.getPoint());
			mv.mesh.drawVertex(p.second.getPoint());			
			mv.stroke(128+c.r.hashCode()%127,128+c.r.hashCode()%127,128+(2*c.r.hashCode())%127);
			mv.mesh.drawSegment(p.first.getPoint(), p.second.getPoint());
		}
		
		mv.noStroke();
		mv.fill(255,0,0);
		mv.mesh.drawVertex(c.r.getV1().getPoint());
		mv.mesh.drawVertex(c.r.getV2().getPoint());		
		mv.stroke(255,0,0);
		mv.mesh.drawSegment(c.r.getV1().getPoint(), c.r.getV2().getPoint());
	}
}


