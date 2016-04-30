import java.awt.image.BufferedImage;
import java.lang.Math;

public class Sphere {
	private Vector3D center;
	private double radius;
	private Color3 color;
	private boolean reflective = false;
	private boolean light = false;
	private boolean shadow = false;
	private BufferedImage texture = null;
	
	private static Vector3D uvect_pole = new Vector3D(0.0, 1.0, 0.0);
	private static Vector3D uvect_equator = new Vector3D(1.0, 0.0, 0.0);

	public Sphere(Vector3D c, double r, Color3 cl) {
		this.center = c;
		this.radius = r;
		this.color = cl;
	}

	public Sphere(Vector3D c, double r, BufferedImage i) {
		this.center = c;
		this.radius = r;
		this.texture = i;
		if (this.texture == null)
			System.out.println("here");
	}

	public void setToLight() {
		this.light = true;
		this.reflective = false;
		this.shadow = false;
	}

	public void setToReflective() {
		this.reflective = true;
		this.light = false;
		this.shadow = true;
	}

	public void setToObject() {
		this.shadow = true;
		this.reflective = false;
		this.light = false;
	}

	public Vector3D getCenter() {
		return this.center;
	}

	public double getRadius() {
		return this.radius;
	}

	public Color3 getColor() {
		return this.color;
	}

	public void setTextureToColor(Vector3D normal_vect) {
		if (this.texture != null) {
			int color = 0;
			int pixel_x, pixel_y, r, g, b;

			double phi = Math.acos((normal_vect.multiConst(-1)).dot(Sphere.uvect_pole));
			double v = phi/Math.PI;

			double theta = (Math.acos((Sphere.uvect_equator.dot(normal_vect))/Math.sin(phi)))/(2.0*Math.PI);
			double u = 0.0;

			if (((Sphere.uvect_pole.crossProduct(Sphere.uvect_equator)).dot(normal_vect)) > 0)
				u = theta;
			else
				u = 1 - theta;

			pixel_x = (int)(u * this.texture.getWidth());
			pixel_y = (int)(v * this.texture.getHeight());
			
			color = this.texture.getRGB(pixel_x, pixel_y);
			r = (color & 0x00ff0000) >> 16;
			g = (color & 0x0000ff00) >> 8;
			b = (color & 0x000000ff);
			
			this.color = (new Color3(r/255.0, g/255.0, b/255.0));
		}
	}
	
	public boolean isReflective() {
		return this.reflective;
	}

	public boolean isLight() {
		return this.light;
	}

	public boolean hasShadow() {
		return this.shadow;
	}
	
	public boolean hasTexture() {
		if (this.texture != null)
			return true;
		else
			return false;
	}
}
