import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class DiffuseReflection {
	private int width = 1400;
	private int height = 1400;
	private Vector3D eye = new Vector3D(-0.5, 1.0, 0.0);
	private Sphere[] sphere; //list of all things
	private Sphere[] light; //list of all light sources

	public DiffuseReflection() {
		// Initialize all spheres
		sphere = new Sphere[11];
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("C:\\Users\\Xuan\\Desktop\\cmpt360\\pj\\earth.jpg"));
		} catch (IOException e) {
		}
		sphere[0] = new Sphere(new Vector3D(0.0, -0.8, -3.0), 0.7, new Color3(1.0,0.5,0.5)); //red 
		sphere[1] = new Sphere(new Vector3D(1.0, -1.2, -1.8), 0.3, new Color3(0.5,1.0,0.5)); //green
		sphere[2] = new Sphere(new Vector3D(-1.5, -0.9, -4.0), 1.0, img); //blue new Color3(0.5,0.5,1.0)
		sphere[3] = new Sphere(new Vector3D(0.0, -100, 0.0), 98.5, new Color3(1.0,1.0,1.0));
		sphere[4] = new Sphere(new Vector3D(0.0, 10.0, -2.0), 1.0, new Color3(1.0,1.0,1.0)); //light
		sphere[5] = new Sphere(new Vector3D(-1.9, 0.3, -3.0), 0.6, new Color3(0.8,0.5,0.3));
		sphere[6] = new Sphere(new Vector3D(-2.2, -0.8, -2.5), 0.7, new Color3(0.9,1.0,0.0)); //yellow
		sphere[7] = new Sphere(new Vector3D(1.4, -0.9, -4.1), 0.7, new Color3(1.0,0.4,0.4));
		sphere[8] = new Sphere(new Vector3D(3.2, -1.0, -5.2), 0.7, new Color3(1.0,0.3,0.3));
		sphere[9] = new Sphere(new Vector3D(0.1, -1.3, -9.0), 0.6, new Color3(1.0,0.2,0.2));
		sphere[10] = new Sphere(new Vector3D(-2.0, -0.8, -1.5), 0.5, new Color3(1.0,0.2,0.2));

		// Set type of sphere
		sphere[0].setToObject();
		sphere[1].setToObject();
		sphere[2].setToObject();
		sphere[3].setToReflective();
		sphere[4].setToLight();
		sphere[5].setToObject();
		sphere[6].setToObject();
		sphere[7].setToObject();
		sphere[8].setToObject();
		sphere[9].setToObject();
		sphere[10].setToReflective();
		

		// Initialize and set all lights
		light = new Sphere[1];
		light[0] = sphere[4];
	}

	/*
	 * Inner class
	 * Used to store the result of findSphere method
	 */
	final class FindSphere {
		private final double t;
		private final Sphere sphere;

		public FindSphere(double t, Sphere s) {
			this.t = t;
			this.sphere = s;
		}

		public double getT() {
			return this.t;
		}

		public Sphere getSphere() {
			return this.sphere;
		}
	}

	/*
	 * Calculate the distance between current spot and a given sphere
	 * 
	 */
	private static double hitSphere(Vector3D origin, Vector3D ray_direction, Vector3D sc, double r) {
		double a = ray_direction.dot(ray_direction);
		double b = 2.0*(ray_direction.dot(origin.subtract(sc)));
		double c = (origin.subtract(sc)).dot(origin.subtract(sc)) - r*r;
		double t = 0.0;

		double disc = b*b - 4.0*a*c;

		if ((disc > 0) && (a != 0))
			t = (-b - Math.sqrt(disc))/(2.0*a);

		return t;
	}

	/*
	 * Find any sphere that the ray will hit in direction 'rayD', start from 'origin'
	 * Return FindSphere - inner class
	 */
	private FindSphere findSphere(Vector3D origin, Vector3D ray_direction) {
		double t;
		double tmin = Double.POSITIVE_INFINITY;
		Sphere hitSphere = null;

		for (int k = 0; k < sphere.length; k++) {
			t = hitSphere(origin, ray_direction, sphere[k].getCenter(), sphere[k].getRadius());
			if (t > 0.00001) {
				if (t < tmin) {
					tmin = t;
					hitSphere = sphere[k];
				}
			}
		}

		if ((tmin == Double.POSITIVE_INFINITY) || (hitSphere == null)) //doesn't hit anything
			return null;

		FindSphere sphere = new FindSphere(tmin, hitSphere);
		return sphere;
	}

	/*
	 * Check what color that the ray from 'origin', go in 'ray_direction' will see
	 * Return an array of size 3 that contains r, g, b value
	 */
	private Color3 rcolor(Vector3D origin, Vector3D ray_direction, int depth) {
		if (depth > 5) {
			return new Color3(0.0, 0.0, 0.0);
		}

		double t_min, value;
		Vector3D refl_direction, hit_point, unit_vect, normal_vect;
		Sphere hit_sphere = null;
		Color3 shading;
		Color3 rgb = new Color3(0.0, 0.0, 0.0);

		FindSphere sphere = findSphere(origin, ray_direction);

		if (sphere != null) {
			t_min = sphere.getT();
			hit_sphere = sphere.getSphere();

			hit_point = new Vector3D(origin.add(ray_direction.multiConst(t_min)));
			unit_vect = new Vector3D(hit_point.subtract(hit_sphere.getCenter()));

			normal_vect = new Vector3D(unit_vect.getX()/Math.sqrt(unit_vect.dot(unit_vect)), //normal line x 
					unit_vect.getY()/Math.sqrt(unit_vect.dot(unit_vect)), //normal line y
					unit_vect.getZ()/Math.sqrt(unit_vect.dot(unit_vect))); //normal line z

			if (hit_sphere.isReflective()) { //if object is reflective
				value = 2.0*(ray_direction.dot(normal_vect));
				refl_direction = new Vector3D(ray_direction.subtract(normal_vect.multiConst(value)));
				Vector3D random_direction = new Vector3D (-1.0+2*Math.random(), -1.0+2*Math.random(), -1.0+2*Math.random());
				refl_direction = refl_direction.add(random_direction.multiConst(0.05));

				rgb = rgb.add(rcolor(hit_point, refl_direction, depth+1));
			}
			else { //if not reflective
				if (hit_sphere.isLight()) { //if light
					rgb = new Color3(1.0, 1.0, 1.0);
				}
				else if (isShadow(hit_point) != null) { //if not light and 'hit_point' is in shadow
					Vector3D random_direction = this.randomPointInUnitSphere(hit_point);

					rgb = rgb.add((rcolor(hit_point, normal_vect.add(random_direction), depth+1)));
				}
				else { //hit the object
					if (hit_sphere.hasTexture()) 
						hit_sphere.setTextureToColor(normal_vect);
					
					Vector3D random_direction = this.randomPointInUnitSphere(hit_point);
					shading = lightSource(hit_point, normal_vect);

					rgb = rgb.add(shading);
					rgb = rgb.add((rcolor(hit_point, normal_vect.add(random_direction), depth+1)));
					rgb = rgb.multi(hit_sphere.getColor());
				}
			}
		}
		else { //doesn't hit anything aka hit background
			rgb = new Color3(0.5, 0.8*(1.0-ray_direction.getY()*0.3), 0.98);
		}

		return rgb;
	}

	/*
	 * Check whether 'hit_point' is a shadow or not
	 */
	private FindSphere isShadow(Vector3D hit_point) {
		FindSphere shadow_owner = null;
		Vector3D uvect_light;
		
		if (light != null) {
			for (int i = 0; i < this.light.length; i++) {
				uvect_light = makeUnitVector(light[i].getCenter().subtract(hit_point));
				
				Vector3D default_direction = new Vector3D(uvect_light);
				Vector3D random_direction = this.randomPointInUnitSphere(new Vector3D (0.0, 0.0, 0.0));
				Vector3D ray_direction = new Vector3D(default_direction.add(random_direction.multiConst(0.05)));
				shadow_owner = findSphere(hit_point, ray_direction);

				if (shadow_owner != null) {
					if (shadow_owner.getSphere().isLight())
						shadow_owner = null;
				}
			}
		}
		
		return shadow_owner;
	}

	private Color3 lightSource(Vector3D hit_point, Vector3D normal_vect) {
		Color3 shading = new Color3 (0.0, 0.0, 0.0);
		Vector3D light_vect, uvect_light;

		if (light != null) {
			for (int i = 0; i < this.light.length; i++) {
				light_vect = hit_point.subtract(light[i].getCenter());
				uvect_light = new Vector3D(Math.abs(light_vect.getX()/Math.sqrt(light_vect.dot(light_vect))),
						Math.abs(light_vect.getY()/Math.sqrt(light_vect.dot(light_vect))),
						Math.abs(light_vect.getZ()/Math.sqrt(light_vect.dot(light_vect))));

				shading = shading.addConst(Math.max(0.0, uvect_light.dot(normal_vect))); // dot product = cosine of 2 unit vectors
			}
		}

		return shading;
	}

	private Vector3D randomPointInUnitSphere(Vector3D hit_point) {
		Vector3D random_ray;

		do {  
			random_ray = new Vector3D(- 1.0 + 2*Math.random(),
					-1.0 + 2*Math.random(),
					-1.0 + 2*Math.random());
		} while (random_ray.dot(random_ray) > 1.0);

		return random_ray;
	}

	private Vector3D makeUnitVector(Vector3D v) {
		Vector3D unit_vect = new Vector3D (v.getX()/Math.sqrt(v.dot(v)), 
				v.getY()/Math.sqrt(v.dot(v)),
				v.getZ()/Math.sqrt(v.dot(v)));

		return unit_vect;
	}

	public void diffuseReflection() {
		Vector3D ray_direction, w, u, v;
		Vector3D view_to = new Vector3D(-0.5, 0.0, -1.0);
		String path = "C:\\Users\\Xuan\\Desktop\\cmpt360\\pj\\";
		BufferedImage pict = new BufferedImage(width, height, 1);
		Color3 object_color;
		double r,g,b;
		double time, time0, time1;
		Vector3D centerg0, centerg1, gnew_center;
		Vector3D centerr0, centerr1, rnew_center;
		Color rgb;
		int rpp = 10000;
		
		time0 = 0.0;
		time1 = 1.0;
		centerg0 = new Vector3D(0.9, -1.2, -2.0);
		centerg1 = new Vector3D(1.2, -1.2, -2.0);
		centerr0 = new Vector3D(0.1, -1.3, -9.0);
		centerr1 = new Vector3D(0.5, -1.2, -7.0);

		w = eye.subtract(view_to);
		u = w.crossProduct(new Vector3D(0.0, -1.0, 0.0));
		v = w.crossProduct(u);

		w = makeUnitVector(w);
		u = makeUnitVector(u);
		v = makeUnitVector(v);

		for (int i = 0; i <= width-1; i++) {
			for (int j = 0; j <= height-1; j++) { //for every single pixel
				r = 0;
				g = 0;
				b = 0;
				for (int a = 0; a < rpp; a++) { //anti-aliasing
					ray_direction = new Vector3D((-1.0 + 2.0*((double)(i+Math.random())/(width-1.0))), (1.0 - 2.0*((double)(j+Math.random())/(height-1.0))), -1.0);
					ray_direction = (new Vector3D(u.multiConst(ray_direction.getX())).add(new Vector3D(v.multiConst(ray_direction.getY())))).add(new Vector3D(w.multiConst(ray_direction.getZ()))); 

					time = time0 + Math.random()*(time1-time0);
					gnew_center = centerg0.add((centerg1.subtract(centerg0)).multiConst((time-time0)/(time1-time0)));
					rnew_center = centerr0.add((centerr1.subtract(centerr0)).multiConst((time-time0)/(time1-time0)));
					sphere[1] = new Sphere(gnew_center, sphere[1].getRadius(), sphere[1].getColor());
					sphere[9] = new Sphere(rnew_center, sphere[9].getRadius(), sphere[9].getColor());
					
					object_color = rcolor(eye, ray_direction, 0);

					r += object_color.r;
					g += object_color.g;
					b += object_color.b;

					object_color = new Color3();
				}

				r = (r*255.0)/rpp;
				g = (g*255.0)/rpp;
				b = (b*255.0)/rpp;

				//clamp
				if (r > 255.0)
					r = 255.0;
				else if (r < 0.0)
					r = 0.0;
				if (g > 255.0)
					g = 255.0;
				else if (g < 0.0)
					g = 0.0;
				if (b > 255.0)
					b = 255.0;
				else if (b < 0.0)
					b = 0.0;

				rgb = new Color((int)r, (int)g, (int)b);

				pict.setRGB(i, j, rgb.getRGB());
			}
		}

		try {
			File out = new File(path + "pj.png");
			ImageIO.write(pict, "png", out);
		}
		catch (IOException e) {
		}
	}

	public static void main(String[] args) {
		DiffuseReflection test = new DiffuseReflection();

		long start_time = System.nanoTime();
		test.diffuseReflection();
		long elapsed_time = System.nanoTime() - start_time;

		System.out.println(elapsed_time/1000000000.0);
	}
}
