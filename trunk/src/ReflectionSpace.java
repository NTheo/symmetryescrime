import java.util.List;


public class ReflectionSpace extends KDTree2<Reflection> {
	public ReflectionSpace(SignatureMap SM){
		super(6);
		System.out.println("Pairing...");
		List<Signature> neighbors;
		Reflection r;
		int valids;
		for(Signature s: SM.p){
			neighbors = SM.q.getRange(new double[]{s.getPrincipalCurvature1()-Parameters.pairingRange/2,
					s.getPrincipalCurvature2()-Parameters.pairingRange/2},
					new double[]{s.getPrincipalCurvature1()+Parameters.pairingRange/2,
					s.getPrincipalCurvature2()+Parameters.pairingRange/2});
			valids=0;
			for(Signature t: neighbors){
				r=new Reflection(s, t);
				if(r.valid){
					this.add(r.r, r);
					valids++;
				}
			}
			//System.out.println("neighbor : "+neighbors.size()+" -> "+valids);
		}
	}
	public ReflectionSpace(RefSignatureMap SM){
		super(6);
		System.out.println("Pairing...");
		List<RefSignature> neighbors;
		Reflection r;
		int valids;
		for(RefSignature s: SM.p){
			neighbors = SM.q.getRange(new double[]{s.getAngleSum()-Parameters.pairingRange/400},
					new double[]{s.getAngleSum()+Parameters.pairingRange/400});
			valids=0;
			for(RefSignature t: neighbors){
				r=new Reflection(s, t);
				if(r.valid){
					this.add(r.r, r);
					valids++;
				}
			}
			System.out.println("neighbor : "+neighbors.size()+" -> "+valids);
		}
	}
	
}
