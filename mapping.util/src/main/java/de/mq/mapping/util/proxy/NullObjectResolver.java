package de.mq.mapping.util.proxy;

/**
 * Stellt NullObjekte als Default-Implementierungen zur Verfuegung
 * Martin Fowler: Refactoring. Improving The Design Of Existing Code
 * @author mquasten
 *
 */
public interface NullObjectResolver {
	/**
	 * Gibt das zum uebergebenen Typ passende NullObject zurureck
	 * @param clazz die Klasse, der Typ fuer den eine Implementierung eines NullObjektes zurueck gegeben werden soll
	 * @return das zum Typ passende NullObjekt 
	 */
	public <T> T forType(final Class<? extends T> clazz );

}
