package de.mq.mapping.util.json.support;

import java.util.List;
import java.util.Map;


public interface MapBasedResponse {

	List<MapBasedResultRow> rows();

	<T> List<T> single(Class<? extends T> clazz);

	List<Map<String, Object>> composed();

	<T> List<T> composed(Class<? extends T> targetClass);
	
	<T> List<T> result(final Class<? extends T> targetClass);
	
	enum InfoField {
		
		Status("status"),
		Info("info"),
		Message("message"),
		Description("description");
		
		private String name; 
		InfoField(final String name) {
			this.name=name;
		}
		
		final String field() {
			return name;
		}
		
	}
	
	enum ChildField {
		Value("value"),
		Key("key"),
		Id("id");
		private String name; 
		ChildField(final String name) {
			this.name=name;
		}
		
		final String field() {
			return name;
		}
	}

	<T> T field(final InfoField infoField, final Class<? extends T> targetClass);



}