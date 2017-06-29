import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Class for answer calculation
 *
 * @author moeka
 *
 */
public class CountRules {
	public List<Rule> rules;

	// Name of trace file
	private String traceName = "Trace.txt";

	// Name of rules file
	private String rulesName = "AllRules.txt";

	// Name of output file
	private String answerFileName = "Answers.txt";

	// Name of file which the number of observations of rules is written
	private String resultCount = "ResultCount.txt";

	// Name of file which the number of observations of rules and rule's name
	// are written
	private String resultCountWithName = "ResultCountWithName.txt";

	public void GetCountRules() {
		Get();
		Init();
		Count();
		CalcAnswers();
	}

	/**
	 * Get rules from "AllRules.txt"
	 */
	private void Get() {
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

				for (Rule r : rules) {
					if (r.pre.equals(line[0]) && r.act.equals(line[1])) {
						r.post.add(new Condition(line[2]));
						addR = false;
					}
				}
				if (addR == true) {
					rules.add(new Rule(rules.size(), line[0], line[1], line[2]));
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
	 * Initialize values
	 */
	private void Init() {
		for (Rule r : rules) {
			for (Condition c : r.post) {
				c.value = 0.5;
				c.count = 0;
			}
		}
	}

	/**
	 * Count the number of observations of rules
	 */
	private void Count() {
		String str;

		// Actions
		String pre, act, post;

		for (Rule r : rules) {
			for (Condition c : r.post) {
				c.count = 0;
			}
		}
		try {
			File file = new File(traceName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			post = br.readLine();
			while ((str = br.readLine()) != null) {
				// Check the trace
				pre = post;
				act = str;
				post = br.readLine();

				for (Rule r : rules) {
					if (r.pre.equals(pre) && r.act.equals(act)) {
						for (Condition c : r.post) {
							if (c.name.equals(post)) {
								c.count++;
							}
						}
					}
				}
			}
			br.close();

			file = new File(resultCountWithName);
			PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(
					file)));

			for (Rule r : rules) {
				for (Condition c : r.post) {
					w.println(r.pre + "," + r.act + "," + c.name + ","
							+ c.count);
				}
			}
			w.close();

			file = new File(resultCount);
			w = new PrintWriter(new BufferedWriter(new FileWriter(file)));

			for (Rule r : rules) {
				for (Condition c : r.post) {
					w.println(c.count);
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
	 * Calculate the answer values from trace
	 */
	private void CalcAnswers() {
		double sum;

		try {
			File file = new File(answerFileName);
			PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(
					file)));

			for (Rule r : rules) {
				sum = 0;
				for (Condition c : r.post) {
					sum += c.count;
				}
				if (sum == 0) {
					for (int i = 0; i < r.post.size(); i++) {
						w.println(1.0 / r.post.size());
					}
				} else {
					for (Condition c : r.post) {
						w.println(c.count / sum);
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
}
