package de.mq.mapping.util.proxy.support;

import de.mq.mapping.util.proxy.NullObjectResolver;

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
	ELExpressionParser withVariable(final String name, Object value);

	/**
	 * Assign the el expression to the builder
	 * @param elExpression the el expression that shoulb be parsed
	 * @return the builder itself
	 */
	ELExpressionParser withExpression(final String elExpression);
	
	/**
	 * Assign the nullObjectResolver to the builder
	 * @param nullObjectResolver the nullObject
	 * @return the builder itself
	 */
	ELExpressionParser withNullObjectResolver(final NullObjectResolver nullObjectResolver);

	
	
	/**
	 * The not readable on Null Exception is be catched. Null is returned.
	 * @param skip exception true/false
	 * @return the builder itself
	 */
	ELExpressionParser withSkipNotReachableOnNullPropertyException(final boolean skipException);
	
	
	/**
	 * Parse the expression with the assigned variables
	 * @param resultClass the Type of the result
	 * @return the result of the expression
	 */
	<T> T parse(final Class<? extends T> resultClass);

	/**
	 * Substitut  null results with a NullObject that ist resolved by NullObjectResolver.
	 * @param substitutNullResults
	 * @return  the result of the expression
	 */
	ELExpressionParser withNvl(boolean substitutNullResults);

}