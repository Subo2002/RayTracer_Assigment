package uk.ac.cam.cl.gfxintro.crsid.tick1;

public class Sphere extends SceneObject {

	// Sphere coefficients
	private final double SPHERE_KD = 0.8;
	private final double SPHERE_KS = 1.2;
	private final double SPHERE_ALPHA = 10;
	private final double SPHERE_REFLECTIVITY = 0.3;

	// The world-space position of the sphere
	protected Vector3 position;

	public Vector3 getPosition() {
		return position;
	}

	// The radius of the sphere in world units
	private double radius;

	public Sphere(Vector3 position, double radius, ColorRGB colour) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = SPHERE_KD;
		this.phong_kS = SPHERE_KS;
		this.phong_alpha = SPHERE_ALPHA;
		this.reflectivity = SPHERE_REFLECTIVITY;
	}

	public Sphere(Vector3 position, double radius, ColorRGB colour, double kD, double kS, double alphaS, double reflectivity) {
		this.position = position;
		this.radius = radius;
		this.colour = colour;

		this.phong_kD = kD;
		this.phong_kS = kS;
		this.phong_alpha = alphaS;
		this.reflectivity = reflectivity;
	}

	/*
	 * Calculate intersection of the sphere with the ray. If the ray starts inside the sphere,
	 * intersection with the surface is also found.
	 */
	public RaycastHit intersectionWith(Ray ray) {

		// Get ray parameters
		Vector3 O = ray.getOrigin();
		Vector3 D = ray.getDirection();
		
		// Get sphere parameters
		Vector3 C = position;
		double r = radius;

		// Calculate quadratic coefficients
		double a = D.dot(D);
		double b = 2 * D.dot(O.subtract(C));
		double c = (O.subtract(C)).dot(O.subtract(C)) - Math.pow(r, 2);


		double Disc = b*b - 4*a*c;
		double t1 = (-b+Math.sqrt(Disc))/(2*a*a);
		double t2 = (-b-Math.sqrt(Disc))/(2*a*a);

		if (Disc < 0 || (t1<0 && t2<0))
			return new RaycastHit();
		else {
			t1 = (t1<0 || t2<0) ? Math.max(t1, t2) : Math.min(t1, t2);
			Vector3 P = O.add(D.scale(t1));
			return new RaycastHit(this, t1, P, this.getNormalAt(P) );
		}



		
		// TODO: Determine if ray and sphere intersect - if not return an empty RaycastHit
        // TODO: If so, work out any point of intersection
        // TODO: Then return a RaycastHit that includes the object, ray distance, point, and normal vector
	}

	// Get normal to surface at position
	public Vector3 getNormalAt(Vector3 P) {
		return P.subtract(this.position).normalised();
	}
}
