package de.mq.mapping.util.proxy.support;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelMessage;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.NullObjectResolver;
import de.mq.mapping.util.proxy.model.ArtistAO;

public class ELExpressionParserTest {
	
	private static final String NAME = "Kylie";
	private static final String VARIABLE_NAME = "artist";
	private static final String EL_EXPRESSION = "#artist.name";

	final Expression expression = Mockito.mock(Expression.class);
	final EvaluationContext evaluationContext = Mockito.mock(EvaluationContext.class);
	final ELExpressionParser elExpressionParser = new SimpleSpelExpressionBuilderImpl();
	
	@Before
	public final void setup() {
	
		ReflectionTestUtils.setField(elExpressionParser, "expression", expression);
		ReflectionTestUtils.setField(elExpressionParser, "context", evaluationContext);
	}
	
	
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
	public final void withSkipNotReachableOnNullPropertyExceptionTrue() {
		final ELExpressionParser elExpressionParser = new SimpleSpelExpressionBuilderImpl();
		Assert.assertEquals(elExpressionParser, elExpressionParser.withSkipNotReachableOnNullPropertyException(true));
		@SuppressWarnings("unchecked")
		final Set<SpelMessage> results = (Set<SpelMessage>) ReflectionTestUtils.getField(elExpressionParser, "skippedException");
		Assert.assertEquals(2, results.size());
		Assert.assertTrue( results.contains(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL));
		Assert.assertTrue( results.contains(SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED));
	}
	
	@Test
	public final void withSkipNotReachableOnNullPropertyExceptionFalse() {
		final ELExpressionParser elExpressionParser = new SimpleSpelExpressionBuilderImpl();
		@SuppressWarnings("unchecked")
		final Set<SpelMessage> results = (Set<SpelMessage>) ReflectionTestUtils.getField(elExpressionParser, "skippedException");
		results.add(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL);
		Assert.assertEquals(elExpressionParser, elExpressionParser.withSkipNotReachableOnNullPropertyException(false));
		Assert.assertTrue(results.isEmpty());
	}

	
	@Test
	public final void parse() {
		
		Mockito.when(expression.getValue(evaluationContext)).thenReturn(NAME);
		Assert.assertEquals(NAME, elExpressionParser.parse(String.class));
		Mockito.verify(expression).getValue(evaluationContext);
		
	}
	
	
	@Test
	public final void parseWithNullProperty() {
		@SuppressWarnings("unchecked")
		final Set<SpelMessage> skipped = (Set<SpelMessage>) ReflectionTestUtils.getField(elExpressionParser, "skippedException");
		skipped.add(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL);
		Mockito.when(expression.getValue(evaluationContext)).thenThrow(new SpelEvaluationException(SpelMessage.PROPERTY_OR_FIELD_NOT_READABLE_ON_NULL));
		Assert.assertNull(elExpressionParser.parse(String.class));
	}
	
	
	@Test(expected=SpelEvaluationException.class)
	public final void parseException() {
		Mockito.when(expression.getValue(evaluationContext)).thenThrow(new SpelEvaluationException(SpelMessage.ARGLIST_SHOULD_NOT_BE_EVALUATED));
		elExpressionParser.parse(String.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void parseNoExpression() {
		final ELExpressionParser elExpressionParser = new SimpleSpelExpressionBuilderImpl();
		elExpressionParser.parse(String.class);
	}
	
	@Test
	public final void parseWithRealValues() {
		
		final ArtistAO artist = Mockito.mock(ArtistAO.class);
		Mockito.when(artist.getName()).thenReturn(NAME);
		Assert.assertEquals(NAME, new SimpleSpelExpressionBuilderImpl().withVariable(VARIABLE_NAME, artist).withExpression(EL_EXPRESSION).parse(String.class));
		
	}
	
	@Test
	public final void withNullObjectResolver() {
		
		final ELExpressionParser elExpressionParser = new  SimpleSpelExpressionBuilderImpl();
		final NullObjectResolver nullObjectResolver = Mockito.mock(NullObjectResolver.class);
		Assert.assertEquals(elExpressionParser, elExpressionParser.withNullObjectResolver(nullObjectResolver));
		Assert.assertEquals(nullObjectResolver, ReflectionTestUtils.getField(elExpressionParser, "nullObjectResolver"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void withNullObjectReslverNullGuard() {
		new  SimpleSpelExpressionBuilderImpl().withNullObjectResolver(null);
	}
	
	@Test
	public final void parseWithSPELExceptionAndNullObjekt() {
		final ArtistAO artist =  Mockito.mock(ArtistAO.class); 
		final ArtistAO duetPartner =  Mockito.mock(ArtistAO.class); 
		final NullObjectResolver nullObjectResolver = Mockito.mock(NullObjectResolver.class);
		Mockito.when(nullObjectResolver.forType(ArtistAO.class)).thenReturn(duetPartner);
		final ELExpressionParser elExpressionParser = new  SimpleSpelExpressionBuilderImpl().withVariable(VARIABLE_NAME, artist).withNullObjectResolver(nullObjectResolver).withSkipNotReachableOnNullPropertyException(true).withExpression("#artist.duetPartner.duetPartner");
		Assert.assertEquals(duetPartner, elExpressionParser.parse(ArtistAO.class));
	}
	
	@Test
	public final void parseSustitudeNullResult(){
		final ArtistAO artist =  Mockito.mock(ArtistAO.class); 
		final ArtistAO duetPartner =  Mockito.mock(ArtistAO.class); 
		final NullObjectResolver nullObjectResolver = Mockito.mock(NullObjectResolver.class);
		Mockito.when(nullObjectResolver.forType(ArtistAO.class)).thenReturn(duetPartner);
		final ELExpressionParser elExpressionParser = new  SimpleSpelExpressionBuilderImpl().withVariable(VARIABLE_NAME, artist).withNullObjectResolver(nullObjectResolver).withNvl(true).withExpression("#artist.duetPartner");
		Assert.assertEquals(duetPartner, elExpressionParser.parse(ArtistAO.class));
		
	}
	
	@Test
	public final void parseNotSustitudeNullResult(){
		final ArtistAO artist =  Mockito.mock(ArtistAO.class); 
	    final ELExpressionParser elExpressionParser = new  SimpleSpelExpressionBuilderImpl().withVariable(VARIABLE_NAME, artist).withNvl(false).withExpression("#artist.duetPartner");
		Assert.assertNull(elExpressionParser.parse(ArtistAO.class));
		
	}

}
