package de.mq.mapping.util.proxy.support;

import org.springframework.context.annotation.Scope;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
class SimpleSpelExpressionBuilderImpl implements ELExpressionParser {
	
	 private final ExpressionParser parser = new SpelExpressionParser();        
	 private final EvaluationContext context = new StandardEvaluationContext();
	 private Expression expression;
	 
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ELExpressionParser#withVariable(java.lang.String, java.lang.Object)
	 */
	@Override
	public final ELExpressionParser withVariable(final String name, final Object value) {
		context.setVariable(name, value);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ELExpressionParser#withExpression(java.lang.String)
	 */
	@Override
	public final ELExpressionParser withExpression(final String elExpression){
		this.expression= parser.parseExpression(elExpression);
		return this;
		
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ELExpressionParser#parse()
	 */
	@Override
	public final Object  parse(){
		expressionExistsGuard();
		try {
		    return expression.getValue(context);
		} catch (final SpelEvaluationException spelEvaluationException){
			return handleException(spelEvaluationException);
		}
	}

	private Object handleException(final SpelEvaluationException spelEvaluationException) {
		if( spelEvaluationException.getMessageCode().equals(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL)) {
		   return null;	
		}
		throw spelEvaluationException;
	}

	private void expressionExistsGuard() {
		if ( expression==null){
			throw new IllegalArgumentException("An expression should be given.");
		}
	}

}
