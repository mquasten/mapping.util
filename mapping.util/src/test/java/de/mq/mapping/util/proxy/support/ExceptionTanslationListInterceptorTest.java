package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.model.ActionMock;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistControllerAO;
import de.mq.mapping.util.proxy.model.ArtistControllerImpl;
import de.mq.mapping.util.proxy.model.DoNothingActionMock;

public class ExceptionTanslationListInterceptorTest {
	
	private ModelRepository modelRepository;
	private Interceptor interceptor;

    private AOProxyFactory factory ;
	private BeanResolver beanResolver;
	
	private ArtistControllerImpl controller;
	private final ArtistAO artistAO = Mockito.mock(ArtistAO.class);
	private final ELExpressionParser elExpressionParser = Mockito.mock(ELExpressionParser.class);
	
	
	@Before
	public final void setup() {
		beanResolver=Mockito.mock(BeanResolver.class);
		modelRepository = Mockito.mock(ModelRepository.class);
		Mockito.when(modelRepository.beanResolver()).thenReturn(beanResolver);
		controller = new ArtistControllerImpl();
		Mockito.when(modelRepository.get(ArtistControllerImpl.class)).thenReturn(controller);
		interceptor = new MethodInvocationInterceptorImpl(modelRepository);
		factory = Mockito.mock(AOProxyFactory.class);
		Mockito.when(beanResolver.getBeanOfType(AOProxyFactory.class)).thenReturn(factory);
		Mockito.when(factory.createProxy(ArtistAO.class, modelRepository)).thenReturn(artistAO);
		System.setProperty(ArtistControllerImpl.Artist_HASHCODE_KEY, "");
		
		Mockito.when(elExpressionParser.withExpression(Mockito.anyString())).thenReturn(elExpressionParser);
		Mockito.when(elExpressionParser.withVariable(Mockito.anyString(), Mockito.any())).thenReturn(elExpressionParser);
		Mockito.when(elExpressionParser.withSkipNotReachableOnNullPropertyException(Mockito.anyBoolean())).thenReturn(elExpressionParser);
		Mockito.when(beanResolver.getBeanOfType(ELExpressionParser.class)).thenReturn(elExpressionParser);
	}
	
	
	@Test(expected=ClassNotFoundException.class)
	public final void translateException() throws Throwable {
		Mockito.when(beanResolver.getBeanOfType(ActionMock.class)).thenReturn(new ActionMock());
		
		final Method method = ArtistControllerImpl.class.getMethod("artist", Long.class);
		interceptor.invoke(method, new Object[] {-1L });
	}
	
	
	@Test
	public final void translateExceptionNothingTrown() throws Throwable {
		final Method method = ArtistControllerImpl.class.getMethod("artist", Long.class);
		interceptor.invoke(method, new Object[] {19680528L });
	}
	
	@Test(expected=RuntimeException.class)
	public final void translateExceptionWrongException() throws Throwable {
		final Method method = ArtistControllerImpl.class.getMethod("artist", Long.class);
		interceptor.invoke(method, new Object[] {0L });
	}
	
	
	@Test(expected=IllegalStateException.class)
	public final void translateIllegalState() throws Throwable {
		final Method method = ArtistControllerImpl.class.getMethod("artist");
		
		Mockito.when(beanResolver.getBeanOfType(Action.class)).thenThrow(new IllegalStateException());
		interceptor.invoke(method, new Object[] {});
	}
	
	@Test
	public final void translateExceptionCatchedInAction() throws Throwable {
		final Method method = ArtistControllerImpl.class.getMethod("artistFacesMessage");
		Mockito.when(beanResolver.getBeanOfType(DoNothingActionMock.class)).thenReturn(new DoNothingActionMock());
		Assert.assertEquals(artistAO, interceptor.invoke(method, new Object[] {}));
	}
	
	@Test
	public final void storeArtist() throws Throwable {
		final Method method = ArtistControllerAO.class.getMethod("store" );
		final ArtistAO artistAO = Mockito.mock(ArtistAO.class);
		Mockito.when(beanResolver.getBeanOfType(ArtistAO.class)).thenReturn(artistAO);
		
		Artist artist = Mockito.mock(Artist.class);
		Mockito.when(elExpressionParser.parse()).thenReturn(artist);
		
		interceptor.invoke(method, new Object[]{});
		Assert.assertEquals(artist.hashCode(), (int) Integer.valueOf(System.getProperty(ArtistControllerImpl.Artist_HASHCODE_KEY)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void managedBeanNotFound() throws Throwable {
		final Method method = ArtistControllerAO.class.getMethod("store" );
		
		interceptor.invoke(method, new Object[]{});
	}
	
	@Test
	public final void targetMethodNotExists() throws Throwable {
		final Method method = ArtistControllerAO.class.getMethod("store2" );
		
		Assert.assertNotNull(method);
		final ArtistAO artistAO = Mockito.mock(ArtistAO.class);
		Mockito.when(beanResolver.getBeanOfType(ArtistAO.class)).thenReturn(artistAO);
		interceptor.invoke(method, new Object[]{});
		Assert.assertEquals(artistAO.hashCode(), (int) Integer.valueOf(System.getProperty(ArtistControllerImpl.Artist_HASHCODE_KEY)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void noAction() throws Throwable {
		final Method method = ArtistControllerAO.class.getMethod("dummy" );
		Assert.assertNotNull(method);
		interceptor.invoke(method,new Object[]{});
	}
	
	@Test(expected=IllegalStateException.class)
	public final void notAnnotated() throws Throwable {
		final Method method = ArtistControllerAO.class.getMethod("dummy2" );
		Assert.assertNotNull(method);
		interceptor.invoke(method, new Object[]{});
	}
	

}
