package de.mq.mapping.util.proxy.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.test.util.ReflectionTestUtils;
import de.mq.mapping.util.proxy.model.ArtistAO;

public class ELExpressionParserTest {
	
	private static final String NAME = "Kylie";
	private static final String VARIABLE_NAME = "artist";
	private static final String EL_EXPRESSION = "#artist.name";

	@Test
	public final void withVariable() {
		final ArtistAO artist = Mockito.mock(ArtistAO.class);
		final EvaluationContext evaluationContext = Mockito.mock(EvaluationContext.class);
		final ELExpressionParser elExpressionParser = new SimpleSpelExpressionBuilderImpl();
		ReflectionTestUtils.setField(elExpressionParser, "context", evaluationContext);
		Assert.assertEquals(elExpressionParser, elExpressionParser.withVariable(VARIABLE_NAME, artist));
		Mockito.verify(evaluationContext).setVariable(VARIABLE_NAME, artist);
	}
	
	@Test
	public final void withExpression() {
		final Expression expression = Mockito.mock(Expression.class);
		final ELExpressionParser elExpressionParser = new SimpleSpelExpressionBuilderImpl();
		final ExpressionParser expressionParser = Mockito.mock(ExpressionParser.class);
		ReflectionTestUtils.setField(elExpressionParser, "parser", expressionParser);
		Mockito.when(expressionParser.parseExpression(EL_EXPRESSION)).thenReturn(expression);
		Assert.assertNull(ReflectionTestUtils.getField(elExpressionParser, "expression"));
		Assert.assertEquals(elExpressionParser, elExpressionParser.withExpression(EL_EXPRESSION));
		Assert.assertEquals(expression, ReflectionTestUtils.getField(elExpressionParser, "expression"));
		Mockito.verify(expressionParser).parseExpression(EL_EXPRESSION);
		
	}
	
	@Test
	public final void parse() {
		final Expression expression = Mockito.mock(Expression.class);
		final EvaluationContext evaluationContext = Mockito.mock(EvaluationContext.class);
		final ELExpressionParser elExpressionParser = new SimpleSpelExpressionBuilderImpl();
		ReflectionTestUtils.setField(elExpressionParser, "expression", expression);
		ReflectionTestUtils.setField(elExpressionParser, "context", evaluationContext);
		Mockito.when(expression.getValue(evaluationContext)).thenReturn(NAME);
		Assert.assertEquals(NAME, elExpressionParser.parse());
		Mockito.verify(expression).getValue(evaluationContext);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void parseNoExpression() {
		final ELExpressionParser elExpressionParser = new SimpleSpelExpressionBuilderImpl();
		elExpressionParser.parse();
	}
	
	@Test
	public final void parseWithRealValues() {
		
		final ArtistAO artist = Mockito.mock(ArtistAO.class);
		Mockito.when(artist.getName()).thenReturn(NAME);
		Assert.assertEquals(NAME, new SimpleSpelExpressionBuilderImpl().withVariable(VARIABLE_NAME, artist).withExpression(EL_EXPRESSION).parse());
		
	}
	
	
	

}
