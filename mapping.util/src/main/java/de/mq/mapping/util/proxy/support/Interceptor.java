package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;


interface Interceptor {
	
	Object invoke(final Method method, final Object[] args) throws Throwable;
}
