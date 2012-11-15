package Jcg.polyhedron;

import Jcg.geometry.*;


public class Vertex{

Halfedge halfedge=null;
Point_3 point=null;
public int tag;
public int index;

    public Vertex(Point_3 point) { this.point=point; }
    public Vertex() {}

    public void setEdge(Halfedge halfedge) { this.halfedge=halfedge; }
    public void setPoint(Point_3 point) { this.point=point; }  
    
    public Point_3 getPoint() { return this.point; } 
    public Halfedge getHalfedge() { return this.halfedge; } 
    
    public String toString(){
        return "v"+point.toString();
    }
}


