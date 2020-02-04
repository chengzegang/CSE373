package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

/**
 * Members of team: Hooley Cheng, Hongyang Zhang
 * 
 * All of the static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
	/**
	* Takes the given AstNode node and attempts to convert it into a double.
	*
	* Returns a number AstNode containing the computed double.
	*
	* @throws EvaluationError  if any of the expressions contains an undefined variable.
	* @throws EvaluationError  if any of the expressions uses an unknown operation.
	*/
	public static AstNode toDouble(Environment env, AstNode node) {
		return new AstNode(toDoubleHelper(env.getVariables(), node.getChildren().get(0)));
	}
	
	private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {

		if (node.isNumber()) {
			return node.getNumericValue();
		} else if (node.isVariable()) {
			if (!variables.containsKey(node.getName())) {
		
				throw new EvaluationError("Undefined variable: " + node.getName());
			}
			return toDoubleHelper(variables, variables.get(node.getName()));
		} else {
			String name = node.getName();
			
			AstNode left = node.getChildren().get(0);
			AstNode right = null;
			if (node.getChildren().size() > 1) {
				right = node.getChildren().get(1);
			}
			
			if (name.equals("+")) {
				return toDoubleHelper(variables, left) + toDoubleHelper(variables, right);
			} else if (name.equals("-")) {
				return toDoubleHelper(variables, left) - toDoubleHelper(variables, right);
			} else if (name.equals("*")) {
				return toDoubleHelper(variables, left) * toDoubleHelper(variables, right);
			} else if (name.equals("/")) {
				return toDoubleHelper(variables, left) / toDoubleHelper(variables, right);
			} else if (name.equals("^")) {
				return Math.pow(toDoubleHelper(variables, left), toDoubleHelper(variables, right));
			} else if (name.equals("negate")) {
				return -toDoubleHelper(variables, left);
			} else if (name.equals("sin")) {
				return Math.sin(toDoubleHelper(variables, left));
			} else if (name.equals("cos")) {
				return Math.cos(toDoubleHelper(variables, left));
			} else {
				throw new EvaluationError("Unknown operation: " + name);
			}
		}
	}
	
	/**
	 * Takes the given AstNode node and attempts to convert it into a simplified one.
	 * 
	 * Returns an AstNode containing the simplified equation.
	 * 
	 * @throws EvaluationError  if any of the expressions uses an unknown operation.
	 */
	public static AstNode simplify(Environment env, AstNode node) {
		return simplifyHelper(env.getVariables(), node.getChildren().get(0));
	}
	
	
	private static AstNode simplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
		if (node.isVariable() && variables.containsKey(node.getName())) {
			return simplifyHelper(variables, variables.get(node.getName()));
		} else if (node.isOperation()) {
			String name = node.getName();
	
			AstNode first = simplifyHelper(variables, node.getChildren().get(0));
			AstNode second = null;
			if (node.getChildren().size() == 2) {
				second = simplifyHelper(variables, node.getChildren().get(1));
			}
			if (!name.matches("sin|cos|/")&& first.isNumber() && 
					(second == null || second.isNumber())) {
				return new AstNode(toDoubleHelper(variables, node));
			} else {
				node.getChildren().set(0, simplifyHelper(variables, node.getChildren().get(0)));
				if (node.getChildren().size() == 2) {
					node.getChildren().set(1, simplifyHelper(variables, node.getChildren().get(1)));
				}
			}
		}
		return node;
	}
	
	
	/**
	* Expected signature of plot:
	*
	* >>> plot(exprToPlot, var, varMin, varMax, step)
	*
	* Example 1:
	*
	* >>> plot(3 * x, x, 2, 5, 0.5)
	*
	* This command will plot the equation "3 * x", varying "x" from 2 to 5 in 0.5
	* increments. In this case, this means you'll be plotting the following points:
	*
	* [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
	*
	* ---
	*
	* Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
	* from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
	*
	* >>> c := 4
	* 4
	* >>> step := 0.01
	* 0.01
	* >>> plot(a^2 + c*a + a, a, -10, 10, step)
	*
	* ---
	*
	* @throws EvaluationError  if any of the expressions contains an undefined variable.
	* @throws EvaluationError  if varMin > varMax
	* @throws EvaluationError  if 'var' was already defined
	* @throws EvaluationError  if 'step' is zero or negative
	*/
	public static AstNode plot(Environment env, AstNode node) {
		
		
		AstNode experToPlot = node.getChildren().get(0);
		String var = node.getChildren().get(1).getName();
		double varMin = toDoubleHelper(env.getVariables(), node.getChildren().get(2));
		double varMax = toDoubleHelper(env.getVariables(), node.getChildren().get(3));
		double step = toDoubleHelper(env.getVariables(), node.getChildren().get(4));
		
		
		if (varMin > varMax) {
			throw new EvaluationError("varMin > varMax");
		}
		
		if (env.getVariables().containsKey(var)) {
			throw new EvaluationError("'var' was already defined");
		}
		
		if (step <= 0) {
			throw new EvaluationError("'step' is zero or negative");
		}
		
		IList<Double> xValues = new DoubleLinkedList<Double>();
		IList<Double> yValues = new DoubleLinkedList<Double>();
		for (double i = varMin; i <= varMax; i += step) {
			xValues.add(i);
			env.getVariables().put(var, new AstNode(i));
			yValues.add(toDoubleHelper(env.getVariables(), experToPlot));
		}
		env.getVariables().remove(var);
		env.getImageDrawer().drawScatterPlot("", "", "", xValues, yValues);
		
		return new AstNode(1);
	}
}
