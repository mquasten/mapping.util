package de.mq.mapping.util.proxy.support;

import java.util.UUID;


  
	
	
	
   class KeyImpl implements Key {	
	
   	protected enum KeyType {
   		Domain(false),
   		Map(false),
   		Error(true),
   		Cache(true);
   		private boolean isChild;
   		KeyType(boolean isChild){
   			this.isChild=isChild;
   		}
   		
   		
   	}	
   	
   	
   	
	private final KeyType keyType;
	
	private final String name;
	
	
	KeyImpl(final Class<?> clazz){
		this.keyType=KeyType.Domain;
		this.name=clazz.getName();
	}
	
	KeyImpl(final String name){
		this.keyType=KeyType.Map;
		this.name=name;
	}
	
	KeyImpl(final Class<?> clazz, final UUID uuid){
		this.keyType=KeyType.Cache;
		this.name=clazz.getName() +"." +uuid.toString();
	}
	
	KeyImpl(final Class<?> clazz, final String name){
		this.keyType=KeyType.Error;
		this.name=clazz.getName() +"." + name;
	}
	
	KeyImpl(final Class<?> clazz, final String name, final KeyType childType){
		isChildTypeGuard(childType);
		this.keyType=childType;
		this.name=clazz.getName() +"." + name;
	}

	private void isChildTypeGuard(final KeyType childType) {
		if(! childType.isChild){
			throw new IllegalArgumentException("KeyType isn't childType: " + childType);
		}
	}
	
	

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof KeyImpl)) {
			return false;
			
		}
		
		final KeyImpl key = (KeyImpl) obj;
		return  (keyType == key.keyType) && name.equals(key.name);
	}

	@Override
	public int hashCode() {
		return keyType.hashCode() + name.hashCode();
	}

	@Override
	public String toString() {
		return "type=" +keyType.name() +  ", name=" +name;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.util.proxy.support.Key#hasParent(java.lang.Class)
	 */
	public final boolean hasParent(final Class<?> clazz) {
		 if (! keyType.isChild){
			 return false;
		 }
		 return name.startsWith(clazz.getName()+".");
	}

	@Override
	public boolean isMapKey() {
		return (keyType == KeyType.Map);
	}

	@Override
	public final  String name() {
		return name;
	}

	
	


}
