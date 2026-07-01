/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.api;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlType;

import java.util.Optional;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service interface for OrderExpansion module providing business logic for Procedures and
 * MedicalSupplyDispenses.
 */
@Transactional
public interface OrderExpansionService extends OpenmrsService {
	
	// MedicalSupplyDispense methods
	
	/**
	 * Gets a MedicalSupplyDispense by its UUID
	 *
	 * @param uuid the UUID of the MedicalSupplyDispense
	 * @return an Optional containing the MedicalSupplyDispense if found
	 */
	Optional<MedicalSupplyDispense> getMedicalSupplyDispenseByUuid(@NonNull String uuid);
	
	/**
	 * Saves or updates a MedicalSupplyDispense
	 *
	 * @param medicalSupplyDispense the MedicalSupplyDispense to save or update
	 * @return the saved MedicalSupplyDispense
	 */
	MedicalSupplyDispense saveOrUpdate(@NonNull MedicalSupplyDispense medicalSupplyDispense);
	
}
