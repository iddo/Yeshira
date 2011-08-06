package org.yeshira.model.service.impl;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.yeshira.model.AbstractDomainObject;

public abstract class AbstractService {
	private static final Logger logger = Logger
			.getLogger(AbstractService.class);

	protected Database db;

	@Autowired
	public void setDb(Database db) {
		this.db = db;
	}

	protected AbstractDomainObject getById(String id) {
		try {
			return AbstractDomainObject.fromDocument(db.getDocument(
					BaseDocument.class, id));
		} catch (NotFoundException e) {
			logger.debug(e);
		}
		return null;
	}

	protected List<? extends AbstractDomainObject> getByIds(String[] ids) {
		// TODO: get multiple documents directly from db
		List<AbstractDomainObject> baseDocuments = new Vector<AbstractDomainObject>();
		for (String id : ids) {
			baseDocuments.add(getById(id));
		}
		return baseDocuments;
	}

}
