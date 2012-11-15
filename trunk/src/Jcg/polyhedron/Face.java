package Jcg.polyhedron;

import Jcg.geometry.Vector_3;

/**
 * 
 * @author modified by Antoine & NTheo
 *
 */
public class Face{

Halfedge halfedge=null;
public int tag;

    public Face() {}
        
    public int degree() {
    	Halfedge e,p;
    	if(this.halfedge==null) return 0;
    	
    	e=halfedge; p=halfedge.next;
    	int cont=1;
    	while(p!=e) {
    		cont++;
    		p=p.next;
    	}
    	return cont;
    }
    
	/**
	 * true iff the face is a triangle
	 */	
	public boolean isTriangle() {
		Halfedge h=this.getEdge();
		if(h.getNext().getNext().getNext()!=h)
			return false;
		return true;
	}

	/**
	 * true iff the face is a quad
	 */	
	public boolean isQuad() {
		if(this.degree()==4)
			return true;
		return false;
	}

    
    public int[] getVertexIndices(Polyhedron_3 polyhedron) {
    	int d=this.degree();
    	int[] result=new int[d];
    	Vertex v;
    	
    	Halfedge e,p;
    	if(this.halfedge==null) return null;
    	
    	e=halfedge; p=halfedge.next;
    	v=e.getVertex();
    	result[0]=polyhedron.vertices.indexOf(v);
    	int cont=1;
    	while(p!=e) {
    		v=p.getVertex();
    		result[cont]=polyhedron.vertices.indexOf(v);
    		cont++;
    		p=p.next;
    	}
    	return result;
    }
    
    
    public void setEdge(Halfedge halfedge) { this.halfedge=halfedge; }
    public Halfedge getEdge() { return this.halfedge; }
       
	/**
	 * @author Antoine & NTheo
	 * @return the associated normal vector
	 */
	public Vector_3 normale(){
		Vector_3 v = this.getEdge().vector().crossProduct(this.getEdge().getNext().vector());
		double l = Math.sqrt((v.squaredLength().doubleValue()));
		return (l>0.)?v.divisionByScalar(l):v;
	}
	/**
	 *  assumes the Face is a triangle
	 *  @author NTheo & Antoine
	 *  @return the associated area
	 */
	public double areaOfFace(){
		Vector_3 v = this.getEdge().vector().crossProduct(this.getEdge().getNext().vector());
		return v.squaredLength().doubleValue()/2.;
	}
	
    public String toString(){
    	String result="";
    	Halfedge e,p;
    	if(this.halfedge==null) {
    		System.out.println("face.toString() error: null incident halfedge");
    		return null;
    	}
    	
    	e=halfedge;
    	p=halfedge.next;
    	result=result+e.getVertex().toString();
    	while(p!=e) {
    		result=result+"\t"+p.getVertex().toString();
    		p=p.next;
    	}
    	return result;
    }

}
