package de.mq.mapping.util.proxy;
/**
 * The cdi shit for test only
 * @author ManfredQuasten
 *
 */
public interface Conversation {

	void begin();

	void begin(final String id);

	void end();

	String getId();

	long getTimeout();

	void setTimeout(long milliseconds);

	boolean isTransient();

}
