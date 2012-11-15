package Jcg.io;

//import Jcg.meshGeneration.RandomSamplingTriangulations;
import Jcg.polyhedron.LoadMesh;
import Jcg.polyhedron.Polyhedron_3;
import Jcg.polyhedron.SharedVertexRepresentation;

public class MeshLoader {

	/**
	 * Load a surface triangle mesh (Polyhedron_3<Point_3>) from an .off file
	 * It uses a shared vertex representation as intermediate data structure
	 */
	public static Polyhedron_3 getTriangleMesh(String filename) {
		SharedVertexRepresentation m=new SharedVertexRepresentation(filename);
    	
    	LoadMesh load3D=new LoadMesh();
    	//Point_2[] points2D=LoadMesh.Point3DToPoint2D(m.points);
    	Polyhedron_3 planarTriangleMesh=
    		//load2D.createPolyhedron(points2D,m.faceDegrees,m.faces,m.sizeHalfedges);
    		load3D.createTriangleMesh(m.points, m.faceDegrees, m.faces, m.sizeHalfedges);

    	planarTriangleMesh.isValid(false);
    	return planarTriangleMesh;
	}
}
