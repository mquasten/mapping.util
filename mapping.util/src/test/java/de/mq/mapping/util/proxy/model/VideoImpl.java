package de.mq.mapping.util.proxy.model;

import java.util.ArrayList;
import java.util.List;

public class VideoImpl implements Video {
	
	private Long id;
	
	private String title;
	
	private final List<Artist> artists = new ArrayList<>();
	
	public VideoImpl(final String title) {
		this.title=title;
	}
	
	public VideoImpl(final String title, final long id) {
		this(title);
		this.id=id;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.model.Video#id()
	 */
	@Override
	public final Long id() {
		return this.id;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.mapping.util.proxy.model.Video#title()
	 */
	@Override
	public final String title() {
		return this.title;
	}
	
	public final void assign(final Artist artist) {
		this.artists.add(artist);
	}
	
	public final List<Artist> artists() {
		return this.artists;
	}

}
