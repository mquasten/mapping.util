package de.mq.mapping.util.proxy.support;

/**
 * A builder for el expressions
 * Eric Evans specification pattern
 * @author Admin
 *
 */
public interface ELExpressionParser {

	/**
	 * Assign a varible and its name to the builder
	 * @param name the name of the variable
	 * @param value the variables value
	 * @return the builder itself
	 */
	ELExpressionParser withVariable(String name, Object value);

	/**
	 * Assign the el expression to the builder
	 * @param elExpression the el expression that shoulb be parsed
	 * @return the builder itself
	 */
	ELExpressionParser withExpression(String elExpression);

	
	
	/**
	 * The not eadable on Null Exception is be catched. Null is returned.
	 * @param skip exception true/false
	 * @return the builder itself
	 */
	ELExpressionParser withSkipNotReachableOnNullPropertyException(final boolean skipException);
	
	/**
	 * Parse the expression with the assigned variables
	 * @return the result of the expression
	 */
	Object parse();

}