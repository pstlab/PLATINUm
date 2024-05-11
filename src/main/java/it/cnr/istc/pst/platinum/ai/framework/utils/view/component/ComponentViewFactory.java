package it.cnr.istc.pst.platinum.ai.framework.utils.view.component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.cnr.istc.pst.platinum.ai.framework.domain.component.DomainComponent;

/**
 * 
 * @author anacleto
 *
 */
public class ComponentViewFactory {

	/**
	 * 
	 * @param type
	 * @param component
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends ComponentView> T create(ComponentViewType type, DomainComponent component) {
		// view instance
		T view = null;
		try {
			// get class
			Class<T> clazz = (Class<T>) Class.forName(type.getComponentViewClassName());
			// get constructor
			Constructor<T> c = clazz.getDeclaredConstructor(DomainComponent.class);
			c.setAccessible(true);
			// create instance
			view = c.newInstance(component);
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage());  
		} 
		catch (InstantiationException | SecurityException | NoSuchMethodException | ClassNotFoundException ex) {
			throw new RuntimeException(ex.getMessage());
		}
		
		// get view object
		return view;
	}
}
