public class Vector3D {
	private double x;
	private double y;
	private double z;
	
	public Vector3D() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D(Vector3D v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public Vector3D add(Vector3D v) {
		Vector3D sum = new Vector3D(this.x + v.getX(), this.y + v.getY(), this.z + v.getZ());
		
		return sum;
	}
	
	public Vector3D subtract(Vector3D v) {
		Vector3D difference = new Vector3D(this.x - v.getX(), this.y - v.getY(), this.z - v.getZ());
		
		return difference;
	}
	
	public Vector3D multiVect(Vector3D v) {
		Vector3D multi = new Vector3D(this.x * v.getX(), this.y * v.getY(), this.z * v.getZ());
		
		return multi;
	}
	
	public Vector3D multiConst(double c) {
		Vector3D multi = new Vector3D(this.x * c, this.y * c, this.z * c);
		
		return multi;
	}
	
	public Vector3D divideConst(double c) {
		return (new Vector3D (this.x/(c*1.0), this.y/(c*1.0), this.z/(c*1.0)));
	}
	
	public Vector3D crossProduct(Vector3D v) {
		Vector3D cproduct = new Vector3D(this.y*v.getZ() - this.z*v.getY(),
									this.z*v.getX() - this.x*v.getZ(),
									this.x*v.getY() - this.y*v.getX());
		
		return cproduct;
	}
	
	public double dot(Vector3D v) {
		Vector3D dot = new Vector3D(this.x * v.getX(), this.y * v.getY(), this.z * v.getZ());
		
		return (dot.getX() + dot.getY() + dot.getZ());
	}
}
