package de.mq.mapping.util.proxy.model;

import java.util.HashSet;
import java.util.Set;

public class ArtistImpl implements Artist {
	
	private String name;
	
	private Integer hotScore;
	
	private Long id;
	
	private Set<Video> videos=new HashSet<Video>();
	
	
	@SuppressWarnings("unused")
	private Artist duetPartner;
	
	@SuppressWarnings("unused")
	private ArtistImpl() {
		
	}
	
	public ArtistImpl(final String name) {
		this.name=name;
	}
	
	public ArtistImpl(final String name, final int hotScore) {
		this.name=name;
		this.hotScore=hotScore;
	}
	
	
	public final String name() {
		return name;
	}
	
	public final Integer hotScore(){
		return this.hotScore;
	}
	
	public final Long id() {
		return this.id;
	}
	
	
	public final void assign(final Video ...videos ) {
		for(Video video: videos){
		   this.videos.add(video);
		}
	}
	
	public final void assign(Artist duetPartner){
		this.duetPartner=duetPartner;
	}
	
	

}
