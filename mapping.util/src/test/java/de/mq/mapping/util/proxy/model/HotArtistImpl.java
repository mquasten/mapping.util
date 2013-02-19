package de.mq.mapping.util.proxy.model;

public class HotArtistImpl extends ArtistImpl {

	private Double price; 
	
	public HotArtistImpl(final String name, final double price) {
		super(name);
		this.price=price;
	}
	
	public final Double price() {
		return this.price;
	}

}
