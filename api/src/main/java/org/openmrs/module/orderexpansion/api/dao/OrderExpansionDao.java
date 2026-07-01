/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.api.dao;

import javax.validation.constraints.NotNull;

import java.util.Optional;

import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense;

/**
 * DAO interface for OrderExpansion module providing data access for MedicalSupplyDispenses.
 */
public interface OrderExpansionDao {
	
	// MedicalSupplyDispense methods
	Optional<MedicalSupplyDispense> getMedicalSupplyDispenseById(@NotNull Integer id);
	
	Optional<MedicalSupplyDispense> getMedicalSupplyDispenseByUuid(@NotNull String uuid);
	
	MedicalSupplyDispense saveOrUpdate(@NotNull MedicalSupplyDispense medicalSupplyDispense);
}
