package de.mq.mapping.util.proxy.model;

/**
 * Example Interface for a domain object
 * It describes an artist.
 * @author ManfredQuasten
 *
 */
public interface Artist {
	
	/**
	 * Artists name
	 * @return the name of the artist
	 */
	String name();
	
	/**
	 * The dimension unit of hotness 
	 * @return the hotness of the artist
	 */
	Integer hotScore();
	
	/**
	 * An identifier
	 * @return the identifier
	 */
	Long id();
	
	/**
	 * Assign artist's videos
	 * @param videos the videos of the artist
	 */
	void assign(final Video ... videos );
	
	/**
	 * The current duet partners of the artist, if exist, null otherwise
	 * @param duetPartner the duet partner if exists, otherwise null
	 */
	void assign(final Artist duetPartner);
	
	

}
