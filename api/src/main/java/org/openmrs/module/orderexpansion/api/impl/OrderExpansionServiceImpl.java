/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.api.impl;

import java.util.Optional;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.orderexpansion.api.OrderExpansionService;
import org.openmrs.module.orderexpansion.api.dao.OrderExpansionDao;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense;

/**
 * Service implementation for OrderExpansion module providing business logic for
 * MedicalSupplyDispenses.
 */
public class OrderExpansionServiceImpl extends BaseOpenmrsService implements OrderExpansionService {
	
	private OrderExpansionDao dao;
	
	public void setDao(OrderExpansionDao dao) {
		this.dao = dao;
	}
	
	// MedicalSupplyDispense methods
	
	@Override
	public Optional<MedicalSupplyDispense> getMedicalSupplyDispenseByUuid(String uuid) {
		return dao.getMedicalSupplyDispenseByUuid(uuid);
	}
	
	@Override
	public MedicalSupplyDispense saveOrUpdate(MedicalSupplyDispense medicalSupplyDispense) {
		return dao.saveOrUpdate(medicalSupplyDispense);
	}
	
}
