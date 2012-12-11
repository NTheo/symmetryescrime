import java.util.LinkedList;
import java.util.Queue;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jcg.geometry.*;
import Jcg.polyhedron.*;

/**
 * Class for point signatures. Computes principal directions and curvatures
 * @author Antoine & NTheo (2012)
 *
 */
public class Signature{

	final double threshold = Parameters.pruningThreshold;
	final double radius = Parameters.radius;
	
	private static int nOC = 0; // to avoid reinitializing the tags (see constructor)
	
	private Vector_3 normale;
	private Vector_3 principalDirection1;
	private Vector_3 principalDirection2;
	private double principalCurvature1;
	private double principalCurvature2;
	private boolean looksOK = true;
	
	private Vertex vRoot;
	
	public Vertex getVertex(){
		return this.vRoot;
	}
	
	public Vector_3 getNormale(){
		return this.normale;
	}
	
	
	/**
	 * @return returns true iff the signature is reliable for the computation of transformations 
	 */
	public boolean isValid() {
		return (looksOK && principalCurvature2>0. && principalCurvature1/principalCurvature2 < threshold);
	}
	
	
	/**
	 * @return beta as defined by Alliez & Cie
	 */
	public double betaAngle(Halfedge e){
		Vector_3 c = e.getFace().normale().crossProduct(e.getOpposite().getFace().normale());
		double angle = Math.asin(Math.sqrt(c.squaredLength().doubleValue()));
		return (c.innerProduct(e.vector()).doubleValue()<0)?-angle:angle;
	}
	
	
    public Signature(Vertex vRoot) {
    	nOC++;
    	//System.out.println("computing signature for vertex number " + nOC);
    	//the queue of vertexes we need to see in our BFS.
    	Queue<Vertex> q = new LinkedList<Vertex>();
    	Point_3 pRoot = vRoot.getPoint();
    	this.vRoot = vRoot;
    	//the current position of our algorithm
    	Halfedge e0; Halfedge e; Vertex v; Face f;
    	
    	//current state in the sum
    	double area = 0.;
    	Matrix M = new Matrix(3, 3);
    	q.add(vRoot);
    	while(!q.isEmpty()){ //loop on the points of the zone
    		
    		v = q.poll();
    		e0 = v.getHalfedge();
    		e = v.getHalfedge();
    		do{ //loop on the edges that go to this point
    			if(e.tag<nOC){ //this edge has not yet been seen
    				e.tag = nOC;
    				e.getOpposite().tag = nOC;
    				M=M.plus(e.toColumn().times(e.toLine()).times(betaAngle(e)/Math.sqrt(e.vector().squaredLength().doubleValue())));

    				f = e.getFace();
    				if(f.tag<nOC){ //this face has not yet been seen
    					f.tag = nOC;
    					area+=f.areaOfFace();
    				}
    			}
    			e = e.getNext();
    			v = e.getVertex();
    			if(v.tag<nOC && v.getPoint().squareDistance(pRoot).doubleValue()<radius*radius){ //this vertex has not yet been seen
    				v.tag = nOC;
    				q.add(v);
    			}
    			e = e.getOpposite();
    		}while(e!=e0);
    	}

    	M=M.times(1./area);
    	EigenvalueDecomposition evd = M.eig();

    	double d0 = evd.getD().get(0, 0);
    	double d1 = evd.getD().get(1, 1);
    	double d2 = evd.getD().get(2, 2);
    	if(d0<d1){
    		if(d1<d2){
    			this.normale = new Vector_3(evd.getV().get(0, 0), evd.getV().get(1, 0), evd.getV().get(2, 0));
    			this.principalDirection1 = new Vector_3(evd.getV().get(0, 1), evd.getV().get(1, 1), evd.getV().get(2, 1));
    			this.principalDirection2 = new Vector_3(evd.getV().get(0, 2), evd.getV().get(1, 2), evd.getV().get(2, 2));
    			this.principalCurvature1 = d1;
    			this.principalCurvature2 = d2;
    		}
    		else if(d2<d0){
    			this.normale = new Vector_3(evd.getV().get(0, 2), evd.getV().get(1, 2), evd.getV().get(2, 2));
    			this.principalDirection1 = new Vector_3(evd.getV().get(0, 0), evd.getV().get(1, 0), evd.getV().get(2, 0));
    			this.principalDirection2 = new Vector_3(evd.getV().get(0, 1), evd.getV().get(1, 1), evd.getV().get(2, 1));
    			this.principalCurvature1 = d0;
    			this.principalCurvature2 = d1;
    		}
    		else {
    			this.normale = new Vector_3(evd.getV().get(0, 0), evd.getV().get(1, 0), evd.getV().get(2, 0));
    			this.principalDirection1 = new Vector_3(evd.getV().get(0, 2), evd.getV().get(1, 2), evd.getV().get(2, 2));
    			this.principalDirection2 = new Vector_3(evd.getV().get(0, 1), evd.getV().get(1, 1), evd.getV().get(2, 1));
    			this.principalCurvature1 = d2;
    			this.principalCurvature2 = d1;   			
    		}
    	}
    	else {
    		if(d0<d2){
    			this.normale = new Vector_3(evd.getV().get(0, 1), evd.getV().get(1, 1), evd.getV().get(2, 1));
    			this.principalDirection1 = new Vector_3(evd.getV().get(0, 0), evd.getV().get(1, 0), evd.getV().get(2, 0));
    			this.principalDirection2 = new Vector_3(evd.getV().get(0, 2), evd.getV().get(1, 2), evd.getV().get(2, 2));
    			this.principalCurvature1 = d0;
    			this.principalCurvature2 = d2;
    		}
    		else if(d2<d1){
    			this.normale = new Vector_3(evd.getV().get(0, 2), evd.getV().get(1, 2), evd.getV().get(2, 2));
    			this.principalDirection1 = new Vector_3(evd.getV().get(0, 1), evd.getV().get(1, 1), evd.getV().get(2, 1));
    			this.principalDirection2 = new Vector_3(evd.getV().get(0, 0), evd.getV().get(1, 0), evd.getV().get(2, 0));
    			this.principalCurvature1 = d1;
    			this.principalCurvature2 = d0;
    		}
    		else {
    			this.normale = new Vector_3(evd.getV().get(0, 1), evd.getV().get(1, 1), evd.getV().get(2, 1));
    			this.principalDirection1 = new Vector_3(evd.getV().get(0, 2), evd.getV().get(1, 2), evd.getV().get(2, 2));
    			this.principalDirection2 = new Vector_3(evd.getV().get(0, 0), evd.getV().get(1, 0), evd.getV().get(2, 0));
    			this.principalCurvature1 = d2;
    			this.principalCurvature2 = d0;   			
    		}    		
    	}
    	if(this.normale.innerProduct(vRoot.getHalfedge().getFace().normale()).doubleValue()<0.){
    		this.normale = this.normale.opposite();
    		this.principalDirection1 = this.principalDirection1.opposite();
    		this.principalDirection2 = this.principalDirection2.opposite();
    	}
    	looksOK = this.normale.innerProduct(vRoot.getHalfedge().getFace().normale()).doubleValue()>0.35;  	
    }

    /**
     * @param s the second signature
     * @return the square of the distance between the signatures in Omega6
     */
    public double distance6(Signature s){
    	return (s.principalCurvature2 - this.principalCurvature2)*(s.principalCurvature2 - this.principalCurvature2)
    			+(s.principalCurvature1 - this.principalCurvature1)*(s.principalCurvature1 - this.principalCurvature1);
    }
    /**
     * @param s the second signature
     * @return the square of the distance between the signatures in Omega7
     */
    public double distance7(Signature s){
    	return Math.pow((s.principalCurvature2/s.principalCurvature1)-(this.principalCurvature2/this.principalCurvature1), 2);
    }

	/**
	 * @return the principalDirection1
	 */
	public Vector_3 getPrincipalDirection1() {
		return principalDirection1;
	}


	/**
	 * @return the principalDirection2
	 */
	public Vector_3 getPrincipalDirection2() {
		return principalDirection2;
	}

	public double getPrincipalCurvature1() {
		return principalCurvature1;
	}

	public double getPrincipalCurvature2() {
		return principalCurvature2;
	}
}
