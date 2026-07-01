/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.api.dao.impl;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.Optional;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.module.orderexpansion.api.dao.OrderExpansionDao;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense;

/**
 * DAO implementation for OrderExpansion module providing data access for MedicalSupplyDispenses.
 */
public class OrderExpansionDaoImpl implements OrderExpansionDao {
	
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	// MedicalSupplyDispense methods
	
	@Override
	public Optional<MedicalSupplyDispense> getMedicalSupplyDispenseById(Integer id) {
		Criteria criteria = getCurrentSession().createCriteria(MedicalSupplyDispense.class);
		return Optional.ofNullable((MedicalSupplyDispense) criteria.add(eq("medicalSupplyDispenseId", id)).uniqueResult());
	}
	
	@Override
	public Optional<MedicalSupplyDispense> getMedicalSupplyDispenseByUuid(String uuid) {
		Criteria criteria = getCurrentSession().createCriteria(MedicalSupplyDispense.class);
		return Optional.ofNullable((MedicalSupplyDispense) criteria.add(eq("uuid", uuid)).uniqueResult());
	}
	
	@Override
	public MedicalSupplyDispense saveOrUpdate(MedicalSupplyDispense medicalSupplyDispense) {
		getCurrentSession().saveOrUpdate(medicalSupplyDispense);
		return medicalSupplyDispense;
	}
	
}
