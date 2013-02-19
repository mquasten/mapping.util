package de.mq.mapping.util.proxy.model;

import java.util.List;

/**
 * Example Interface for a domain object
 * Artists videos
 * @author Admin
 *
 */
public interface Video {

	/**
	 * The identifier for a video
	 * @return identifier for video
	 */
	public abstract Long id();

	/**
	 * The name, title of a video
	 * @return title of the video
	 */
	public abstract String title();
	
	/**
	 * At the artist who performs in the video
	 * @param artist the artiist which performs in the video
	 */
	void assign(final Artist artist);
	
    /**
     * The artists that perform in the video
     * @return artists in video
     */
	 List<Artist> artists();

}