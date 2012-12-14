

import java.io.*;
import java.math.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.awt.Color;

import Jcg.geometry.*;

/**
 * This class contains the main methods implementing the Mean-Shift clustering
 */
public class MeanShiftClustering {

    KDTree2<Reflection> N;
    KDTree2<Reflection> seeds;
    double sqCvgRad;  // convergence radius
    double sqAvgRad;  // averaging radius (definining the window)
    double sqInflRad;  // influence radius
    double sqMergeRad;  // merging radius
    private double cvgRad;
    private double inflRad;
    public List<Cluster> clusters;
    //RangeSearch Rs;  // data structure for nearest neighbor search


    // Constructors

    void initMSC (KDTree2<Reflection> n, /*KDTree2<Reflection> s,*/ double cr, double ar, double ir, double mr){
	N = n;
	/*seeds = s;*/
	sqCvgRad = cr*cr;
	sqAvgRad = ar*ar;
	sqInflRad = ir*ir;
	cvgRad = cr;
	inflRad = ir;
	sqMergeRad = mr*mr;
	//Rs = new RangeSearch (N);
    }

    MeanShiftClustering (KDTree2<Reflection> n, KDTree2<Reflection> s, 
			 double cr, double ar, double ir, double mr) {
    		initMSC (n,/*s,*/cr,ar,ir, mr);
    }

    MeanShiftClustering (KDTree2<Reflection> n, double bandWidth) {
	initMSC (n, /*n,*/ bandWidth*1e-3, bandWidth, bandWidth/4, bandWidth);
    } 

    /**
     * Single cluster detection -- returns approximate peak and cluster points
     * The output is a list of point containing:
     *  - all the points belonging to the detected cluster
     *  - the peak point (at the top of the list)
     */
    private final static int d = 6;
    public /*KDTree2<Reflection>*/ Cluster detectCluster (Reflection seed, int clusterIndex) { 
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
    			low[i] = prev.r[i]-cvgRad;
    			high[i] = prev.r[i]+cvgRad;
    		}
    		List<Reflection> closeRef = N.getRange(low, high);
    		List<Reflection> l = new LinkedList<Reflection>();
    		for(Reflection re:closeRef){//removing all already-clustered points
    			if(re.cluster<0)
    				l.add(re);
    		}
    		double[] mean = new double[d];
    		double k = (double)l.size();
    		for(Reflection r: l) for(int i=0; i<d; i++){
    			mean[i]+=r.r[i]/k;
    		}
    		next = N.getNearestNeighbors(mean, 1).removeMax();
    		pointsOfPath.add(next);
    	}while(KDTree2.pointDistSq(next.r, prev.r) < sqCvgRad);
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
    public boolean mergeCluster(ArrayList<Cluster> clusters){
    	List<Integer> clustersToRemove = new LinkedList<Integer>();
    	for(int i = 0; i<clusters.size(); i++) for(int j = i+1; j<clusters.size(); j++){
    		if(KDTree2.pointDistSq(clusters.get(i).r.r, clusters.get(j).r.r) < sqMergeRad){
    			if (clusters.get(i).weight>clusters.get(j).weight)
    				clusters.get(j).r = clusters.get(i).r;
    			clusters.get(j).r.weight += clusters.get(i).weight;
    			clusters.get(j).l.addAll(clusters.get(i).l);
    			clustersToRemove.add(new Integer(i));
    		}
    	}
    	for(Integer I: clustersToRemove){
    		clusters.remove(I.intValue());
    	}
    	return(clustersToRemove.isEmpty());
    }


    /**
     * Main algorithm for detecting all clusters
     * Clusters are detected iteratively (until all points are processed)
     * Clusters are merged if required: when the corresponding peaks are close
     * output: an array of points, of size n
     *   - first i elements are cluster centers (non null points)
     *   - remaining n-i elements must be null
     */
    MeanShiftClustering(KDTree2<Reflection> n){
    	N = n;
    	initMSC(n, Parameters.clusterRadius, 0., Parameters.clusterRadius*2/3., Parameters.clusterRadius*3.);
    	double[] low = new double[n.dimensions];
    	double[] high = new double[n.dimensions];
    	for(int i = 0; i<n.dimensions; i++){
    		low[i] = -9999.;
    		high[i] = 9999.;
    	}
    	int clusterIndex = 0;
    	ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    	for(Reflection r:N.getRange(low, high)){
    		clusterIndex++;
    		if(r.cluster<0){
    			clusters.add(detectCluster(r, clusterIndex));
    		}
    	}
    	do{}while(mergeCluster(clusters));
    	
    }

    
    
//-------------------------------    
//------ Test (exercice 2)-------
//-------------------------------

    
/*    
    public static void main(String[ ] args) throws Exception {
    	System.out.println("Exercice 2:");
    	double bandWidth;
    	PointCloud N=null;
    	if (args.length < 1) {
    		System.out.println("Usage: java MeanShiftClustering (datafile.dat) bandwidth");
    		System.out.println("dataFile.dat optionnel");
    		System.exit(0);
    	}    	
    	if (args.length == 1 ) {
    		N=PointCloud.randomPoints(3000, 3);
    		//N=PointCloud.randomPointsOnCircle(3000, 3);
    		bandWidth = Double.parseDouble(args[0]);
    	}
    	else { 
    		N=Clustering.readFile(args[0]);
    		bandWidth = Double.parseDouble(args[1]);
    	}
    	
    	System.out.println("point cloud of size: "+PointCloud.size(N));
    	Draw.draw2D(N, "original point cloud");

    	Calendar rightNow = Calendar.getInstance(); // to compute time performances
    	long time0 = rightNow.getTimeInMillis();

//--------- choose test to perform ---------------
    	//testDetectCluster(N, bandWidth); // ex 2.1
    	testMeanShift(N, bandWidth); // ex 2.3
//------------------------------------------------

    	rightNow = Calendar.getInstance(); // to compute time performances
    	long time = rightNow.getTimeInMillis();
    	System.out.println("Total time to find clusters: " + (time-time0)/1000 + "s " + (time-time0)%1000 + "ms");
      
    }
    
    public static void testMeanShift(PointCloud N, double bandwidth) {
    	System.out.println("Exercice 2.3: testing Mean-Shift clustering");
    	
    	MeanShiftClustering msc = new MeanShiftClustering (N, bandwidth);
    	
    	Point_D[] clusterCenters = msc.detectClusters();
    	int nDetectedClusters=0;
    	for(int i=0;i<clusterCenters.length;i++)
    		if(clusterCenters[i]!=null) 
    			nDetectedClusters++;
    	System.out.println("Number of clusters detected: "+nDetectedClusters);
    	
    	Draw.draw3D(N);
    	msc.Rs.timePerformance();
    }
    
    public static void testDetectCluster(PointCloud N, double bandwidth) {
    	System.out.println("Exercice 2.1: testing detecting a cluster");
    	
    	MeanShiftClustering msc = new MeanShiftClustering (N, bandwidth);
    	
    	int clusterNumber=0;
    	PointCloud cluster=msc.detectCluster(N.next.p, clusterNumber);
    	
    	PointCloud t=cluster;
    	int i=0;
    	while(t!=null) {
    		if(t.p.cluster==clusterNumber)
    			i++;
    		t=t.next;
    	}
    	System.out.println("size of the detected cluster: "+PointCloud.size(cluster));
    	System.out.println("size of the cluster: "+i);
    	
    	Draw.draw3D(N);
    	//Draw.draw3D(cluster);
    	msc.Rs.timePerformance();
    }
*/
}


