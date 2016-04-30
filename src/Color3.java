public class Color3 {
	public final double r;
	public final double g;
	public final double b;

	public Color3() {
		this.r = 0.0;
		this.g = 0.0;
		this.b = 0.0;
	}

	public Color3(double red, double green, double blue) {
		this.r = red;
		this.g = green;
		this.b = blue;
	}

	public Color3(Color3 c) {
		this.r = c.r;
		this.g = c.g;
		this.b = c.b;
	}

	public Color3 add(Color3 c) {
		return new Color3(this.r + c.r, this.g + c.g, this.b + c.b);
	}

	public Color3 addConst(double c) {
		return (new Color3(this.r + c, this.g + c, this.b + c));
	}

	public Color3 multi(Color3 c) {
		return new Color3((this.r*c.r), (this.g*c.g), (this.b*c.b));
	}

	public Color3 divideConst(double c) {
		return new Color3(this.r/c, this.g/c, this.b/c);
	}

	public Color3 multiConst(double c) {
		return new Color3(c*this.r, c*this.g, c*this.b);
	}
}

