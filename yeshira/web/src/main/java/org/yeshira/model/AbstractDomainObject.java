package org.yeshira.model;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jcouchdb.document.BaseDocument;

public abstract class AbstractDomainObject {
	private static final Logger logger = Logger
			.getLogger(AbstractDomainObject.class);
	
	public static final String PROPERTY_TYPE  = "type";
	public static final String PROPERTY_ID  = "_id";
	
	private BaseDocument document;
	
	protected AbstractDomainObject() {
		this(new BaseDocument());
	}
	
	protected AbstractDomainObject(BaseDocument document) {
		this.document = document;
		this.setProperty(PROPERTY_TYPE, this.getClass().getSimpleName().toLowerCase());
	}
	
	public static String getClassDescriptor(Object o) {
		return getClassDescriptor(o.getClass());
	}

	public static String getClassDescriptor(Class<? extends Object> clazz) {
		return clazz.getSimpleName().toLowerCase();
	}
	
	protected Object getProperty(String property) {
		return document.getProperty(property);
	}
	
	protected void setProperty(String name, Object value) {
		document.setProperty(name, value);
	}
	
	public BaseDocument getDocument() {
		return document;
	}
	
	public String getId() {
		return document.getId();
	}
	
	public boolean isSaved() {
		return document.getRevision() != null;
	}
	
	/**
	 * Instantiates a new document based on the requested class.
	 * The class must have a constructor with BaseDocument as a single parameter.
	 * The method will return null on any error.
	 * 
	 * @param document
	 * @param clazz the class to instantiate
	 * @return null if the document is of the wrong type, or a new instance of the requested class
	 */
	public static <V extends AbstractDomainObject> V fromDocument(BaseDocument document, Class<V> clazz) {
		if (AbstractDomainObject.getClassDescriptor(clazz).equals(document.getProperty(PROPERTY_TYPE))) {
			try {
				return clazz.getConstructor(BaseDocument.class).newInstance(document);
			} catch (SecurityException e) {
				logger.error(e);
			} catch (NoSuchMethodException e) {
				logger.error(e);
			} catch (IllegalArgumentException e) {
				logger.error(e);
			} catch (InstantiationException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (InvocationTargetException e) {
				logger.error(e);
			}
		}
		return null;
	}
	
	public static AbstractDomainObject fromDocument(BaseDocument document) {
		try {
			String className = new StringBuilder(AbstractDomainObject.class.getPackage().getName()).append(".").append(StringUtils.capitalize((String)document.getProperty(PROPERTY_TYPE))).toString();
			@SuppressWarnings("unchecked")
			Class<AbstractDomainObject> destinationClass = (Class<AbstractDomainObject>) Class.forName(className);
			return fromDocument(document, destinationClass);
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}
		
		return null;
	}

	@Override
	public int hashCode() {
		// TODO: also take revision into account
		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDomainObject other = (AbstractDomainObject) obj;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		return true;
	}

}
