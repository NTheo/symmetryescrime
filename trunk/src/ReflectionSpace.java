import java.util.List;


public class ReflectionSpace extends KDTree2<Reflection> {
	public ReflectionSpace(SignatureMap SM, MeshViewer mv){
		super(6);
		System.out.print("Pairing...");
		List<Signature> neighbors;
		Reflection r;
		for(Signature s: SM.p){
			neighbors = SM.q.getRange(new double[]{s.getPrincipalCurvature1()-Parameters.pairingRange/2,
					s.getPrincipalCurvature2()-Parameters.pairingRange/2},
					new double[]{s.getPrincipalCurvature1()+Parameters.pairingRange/2,
					s.getPrincipalCurvature2()+Parameters.pairingRange/2});
			System.out.print(neighbors.size()+" ");
			mv.noStroke();
			mv.fill(4*neighbors.size()%255, 255, 4*neighbors.size()%255);		
			mv.mesh.drawVertex(s.getVertex().getPoint());
			for(Signature t: neighbors){
				r=new Reflection(s, t);
				this.add(r.r, r);
			}
		}
		System.out.println("ok");
	}
}
