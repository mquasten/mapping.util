package de.mq.mapping.util.proxy.support;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.NullObjectResolver;

@Component
@Scope("prototype")
class SimpleSpelExpressionBuilderImpl implements ELExpressionParser {
	
	 private final ExpressionParser parser = new SpelExpressionParser();        
	 private final EvaluationContext context = new StandardEvaluationContext();
	 private Expression expression;
	 
	 private Set<SpelMessage> skippedException=new HashSet<>();
	 
	 private NullObjectResolver nullObjectResolver = new BasicNullObjectResolverImpl();
	 
	 private boolean substitutNullResults = false;
	 
	
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
	
	@Override
	public final ELExpressionParser withNvl(final boolean substitutNullResults){
		this.substitutNullResults=substitutNullResults;
		return this;
	}
	
	
	@Override
	public ELExpressionParser withSkipNotReachableOnNullPropertyException(final boolean skipException ) {
		this.skippedException.remove(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL);
		this.skippedException.remove(SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED);
		if(skipException) {
			this.skippedException.add(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL);
			this.skippedException.add(SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED);
		}
		return this;
	}
	
	
	@Override
	public ELExpressionParser withNullObjectResolver(final NullObjectResolver nullObjectResolver) {
		nullObjectResolverExistsGuard(nullObjectResolver);
		this.nullObjectResolver=nullObjectResolver;
		return this;
	}

	private void nullObjectResolverExistsGuard(final NullObjectResolver nullObjectResolver) {
		if(nullObjectResolver==null){
			throw new IllegalArgumentException("NullObjectResolver should be given");
		}
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.support.ELExpressionParser#parse()
	 */
	
	@Override
	public final <T> T parse(final Class<? extends T> resultClass){
		expressionExistsGuard();
		
		try {
		    return  nvl(resultClass, expression.getValue(context));   
		} catch (final SpelEvaluationException spelEvaluationException){
			return handleException(spelEvaluationException, resultClass);
		}
	}
	@SuppressWarnings("unchecked")
	private <T> T nvl(final Class<? extends T> resultClass, final Object result) {
		if(result!=null){
			return (T) result;
		}
		if( ! this.substitutNullResults) {
			return null;
		}
		
		return  nullObjectResolver.forType(resultClass);
	}

	
	
	
	
	
	private <T> T handleException(final SpelEvaluationException spelEvaluationException, final Class<? extends T> resultClass) {
		if (skippedException.contains(spelEvaluationException.getMessageCode())){
			return nullObjectResolver.forType(resultClass);
		}
		throw spelEvaluationException;
	}

	
	
	
	private void expressionExistsGuard() {
		if ( expression==null){
			throw new IllegalArgumentException("An expression should be given.");
		}
	}

	

	

}
