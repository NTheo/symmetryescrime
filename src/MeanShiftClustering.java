import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;


/**
 * This class contains the main methods implementing the Mean-Shift clustering
 */
public class MeanShiftClustering {

	KDTree<Reflection> N;
	KDTree<Reflection> seeds;
	double sqCvgRad;  // convergence radius
	double sqAvgRad;  // averaging radius (defining the window)
	double sqInflRad;  // influence radius
	double sqMergeRad;  // merging radius
	private double inflRad;
	private ArrayList<Cluster> tempClusters;
	public ArrayList<Cluster> clusters;
	
	/**
	 * Main algorithm for detecting all clusters
	 * Clusters are detected iteratively (until all points are processed)
	 * Clusters are merged if required: when the corresponding peaks are close
	 * output: an array of points, of size n
	 *   - first i elements are cluster centers (non null points)
	 *   - remaining n-i elements must be null
	 */
	MeanShiftClustering(KDTree<Reflection> n){
		System.out.println("Clustering...");
		N = n;
		initMSC(n);
		int clusterIndex = 0;
		tempClusters = new ArrayList<Cluster>();
		for(Reflection r:N.getAll()){
			//System.out.println("cluster "+(1+clusterIndex++) +"/"+ N.getRange(low, high).size());
			if(r.cluster<0){
				tempClusters.add(detectCluster(r, clusterIndex));
			}
		}
		System.out.println(tempClusters.size()+" clusters detected before merging.");
		int iinit = 0;
		do{iinit = mergeCluster(iinit);}while(-1!=iinit);
		System.out.println(tempClusters.size()+" clusters remain after merging.");
		pruneNonsignificantClusters();
		System.out.println(tempClusters.size()+" clusters contain more than one pair of points.");
		sortClusters();
	}

	void initMSC (KDTree<Reflection> n){
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
				if(re.cluster<0 && KDTree.pointDistSq(re.r, prev.r) < sqCvgRad)
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
		}while(KDTree.pointDistSq(next.r, prev.r) > sqCvgRad);
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
		for(int i = iinit; i<tempClusters.size(); i++){
			for(int j = i+1; j<tempClusters.size(); j++){
				//System.out.println(i+" "+j);
				if(KDTree.pointDistSq(tempClusters.get(i).r.r, tempClusters.get(j).r.r) < sqMergeRad){
					if (tempClusters.get(i).l.size()>tempClusters.get(j).l.size())
						tempClusters.get(j).r = tempClusters.get(i).r;
					tempClusters.get(j).r.weight += tempClusters.get(i).weight;
					tempClusters.get(j).l.addAll(tempClusters.get(i).l);
					tempClusters.remove(i);
					return i;
				}
			}
		}
		return(-1);
	}

	private void pruneNonsignificantClusters(){
		Iterator<Cluster> it = this.tempClusters.iterator();
		while(it.hasNext()){
			if(it.next().l.size()<=1)
				it.remove();
		}
	}
	
	private void sortClusters(){
		PriorityQueue<Cluster> q = new PriorityQueue<Cluster>();
		for(Cluster c :this.tempClusters){
			q.add(c);
		}
		// We put the sorted clusters inside an ArrayList
		this.clusters=new ArrayList<Cluster>();
		while(!q.isEmpty()){
			this.clusters.add(q.remove());
		}
		this.tempClusters=null;
	}

	// Display one cluster and the corresponding reflection plane
	public void displayOneCluster(MeshViewer mv){
		if(this.clusters.size()<1){
			System.out.println("There is no cluster to be displayed !");
			return;
		}
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


