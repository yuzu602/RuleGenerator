import java.util.ArrayList;
import java.util.List;

public class Rule {
	// Info.
	public int num;
	public String pre;
	public String act;
	public List<Condition> post;

	Rule(int n, String pre, String act, String post_name){
		this.num=n;
		this.pre=pre;
		this.act=act;
		post = new ArrayList<Condition>();
		post.add(new Condition(post_name));
	}
}
