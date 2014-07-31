package de.mq.mapping.util.json.support;

import de.mq.mapping.util.json.FieldMapping;


public class PetPriceKey {
	
	
	@FieldMapping
	public String unit;
	
	@FieldMapping
	public String  quality;
	
	

	public PetPriceKey(String quality, String unit) {
	
		this.quality = quality;
		this.unit = unit;
	}
	
	@SuppressWarnings("unused")
	private PetPriceKey() {
		
	}

	
}