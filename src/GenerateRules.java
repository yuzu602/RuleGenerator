import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for rules generation in large case
 * Input: "Trace.txt"
 * Output: "AllRules.txt"
 *
 * @author moeka
 *
 */
public class GenerateRules {

	// for Get rules from trace
	private static List<Rule> rules;
	private static List<String> traces;

	private String traceName = "Trace.txt";
	private List<Rule> getrules = new ArrayList<Rule>();

	// File name of obtained rules from trace
	private String getRulesName = "GetRules.txt";
	// File name of generated rules from map info.
	private String generatedRulesName = "GeneratedRules.txt";

	private List<Rule> allrules = new ArrayList<Rule>();

	// Name of output rules file
	private String rulesName = "AllRules.txt";

	// X and Y are size of map
	private static int X = 9;
	private static int Y = 14;

	public void GenerateAllRules() {
		Get();
		RulesCheck();
		Print();

		Generate();
		Integrate();
		CleanRules();
		Output();
	}

	/**
	 * Get rules from trace
	 */
	private void Get() {
		String str;
		String pre, act, post;
		boolean add_rule, add_post;
		Condition Condition;
		traces = new ArrayList<String>();
		rules = new ArrayList<Rule>();

		try {
			File file = new File(traceName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			post = br.readLine();
			traces.add(post);
			while ((str = br.readLine()) != null) {
				pre = post;
				act = str;
				post = br.readLine();

				traces.add(act);
				traces.add(post);

				add_rule = true;
				add_post = true;
				for (Rule r : rules) {
					if (r.pre.equals(pre) && r.act.equals(act)) {
						add_rule = false;
						for (Condition el : r.post) {
							if (el.name.equals(post)) {
								add_post = false;
							}
						}
						if (add_post == true) {
							Condition = new Condition(post);
							r.post.add(Condition);
						}
					}
				}
				if (add_rule == true) {
					Rule t = new Rule(rules.size(), pre, act, post);
					rules.add(t);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Remove duplicate rules
	 */
	private void RulesCheck() {
		Rule x;
		Condition y;
		boolean addR = true, addE = true;
		for (Rule r : rules) {
			for (Rule ar : getrules) {
				if (r.pre.equals(ar.pre) && r.act.equals(ar.act)) {
					for (Condition c : r.post) {
						for (Condition c2 : ar.post) {
							if (c.name.equals(c2.name)) {
								addE = false;
							}
						}
						if (addE == true) {
							y = new Condition(c.name);
							ar.post.add(y);
						}
						addE = true;
					}
					addR = false;
				}
			}
			if (addR == true) {
				x = new Rule(0, r.pre, r.act, r.post.get(0).name);
				for (int i = 1; i < r.post.size(); i++) {
					x.post.add(new Condition(r.post.get(i).name));
				}
				getrules.add(x);
			}
			addR = true;
		}
	}

	/**
	 * Output obtained rules from trace
	 */
	private void Print() {
		try {
			File file = new File(getRulesName);
			PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(
					file)));

			for (Rule r : getrules) {
				for (Condition el : r.post) {
					w.println(r.pre + "," + r.act + "," + el.name);
				}
			}
			w.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Generate rules from map info.
	 */
	private void Generate() {
		try {
			File file = new File(generatedRulesName);
			PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(
					file)));

			for (int i = 0; i <= X; i++) {
				for (int j = 0; j <= Y; j++) {
					if (i > 0) {
						w.println("arrive[" + i + "][" + j + "],move.w,arrive["
								+ i + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.s,arrive["
								+ (i - 1) + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.n,arrive["
								+ (i - 1) + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.w,arrive["
								+ (i - 1) + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.e,arrive["
								+ (i - 1) + "][" + j + "]");
					}
					if (j > 0) {
						w.println("arrive[" + i + "][" + j + "],move.n,arrive["
								+ i + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.s,arrive["
								+ i + "][" + (j - 1) + "]");
						w.println("arrive[" + i + "][" + j + "],move.n,arrive["
								+ i + "][" + (j - 1) + "]");
						w.println("arrive[" + i + "][" + j + "],move.w,arrive["
								+ i + "][" + (j - 1) + "]");
						w.println("arrive[" + i + "][" + j + "],move.e,arrive["
								+ i + "][" + (j - 1) + "]");
					}
					if (i < X) {
						w.println("arrive[" + i + "][" + j + "],move.e,arrive["
								+ i + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.s,arrive["
								+ (i + 1) + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.n,arrive["
								+ (i + 1) + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.w,arrive["
								+ (i + 1) + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.e,arrive["
								+ (i + 1) + "][" + j + "]");
					}
					if (j < Y) {
						w.println("arrive[" + i + "][" + j + "],move.s,arrive["
								+ i + "][" + j + "]");
						w.println("arrive[" + i + "][" + j + "],move.s,arrive["
								+ i + "][" + (j + 1) + "]");
						w.println("arrive[" + i + "][" + j + "],move.n,arrive["
								+ i + "][" + (j + 1) + "]");
						w.println("arrive[" + i + "][" + j + "],move.w,arrive["
								+ i + "][" + (j + 1) + "]");
						w.println("arrive[" + i + "][" + j + "],move.e,arrive["
								+ i + "][" + (j + 1) + "]");
					}
				}
			}
			w.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Integrate got rules and generated rules
	 */
	private void Integrate() {
		String str;

		try {
			File wfile = new File(rulesName);
			PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(
					wfile)));

			File rfile = new File(getRulesName);
			BufferedReader br = new BufferedReader(new FileReader(rfile));
			while ((str = br.readLine()) != null) {
				w.println(str);
			}
			br.close();

			rfile = new File(generatedRulesName);
			br = new BufferedReader(new FileReader(rfile));
			while ((str = br.readLine()) != null) {
				w.println(str);
			}
			br.close();
			w.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Remove duplicate rules
	 */
	private void CleanRules() {
		String[] line;
		String str;
		boolean addR;

		try {
			File file = new File(rulesName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((str = br.readLine()) != null) {
				line = new String[3];
				line = str.split(",", 0);
				addR = true;

				for (Rule r : allrules) {
					if (r.pre.equals(line[0]) && r.act.equals(line[1])) {
						for (Condition c : r.post) {
							if (c.name.equals(line[2])) {
								addR = false;
							}
						}
						if (addR == true) {
							r.post.add(new Condition(line[2]));
							addR = false;
						}
					}
				}
				if (addR == true) {
					allrules.add(new Rule(allrules.size(), line[0], line[1],
							line[2]));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Output rules
	 */
	private void Output() {
		try {
			File file = new File(rulesName);
			PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(
					file)));

			for (Rule r : allrules) {
				for (Condition c : r.post) {
					w.println(r.pre + "," + r.act + "," + c.name);
				}
			}
			w.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
