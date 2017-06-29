
/**
 * Information of post-condition
 * @author moeka
 *
 */
public class Condition {
	// Info.
	public String name;

	// For calc.
	public double value;
	public double pre_value;
	public double grad;
	public int count;

	Condition(String n){
		this.name=n;
		this.value=1.0;
		this.pre_value=1.0;
		this.grad=0;
		this.count=1;
	}
}
