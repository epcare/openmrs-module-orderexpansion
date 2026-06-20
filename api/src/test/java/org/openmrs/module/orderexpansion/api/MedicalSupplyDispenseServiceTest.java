/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.orderexpansion.api.dao.MedicalSupplyDispenseDao;
import org.openmrs.module.orderexpansion.api.impl.MedicalSupplyDispenseServiceImpl;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense.MedicalSupplyStatus;

/**
 * Unit tests for {@link MedicalSupplyDispenseService}. These tests verify the behavior of the
 * MedicalSupplyDispenseService implementation using Mockito for dependency injection and mocking.
 */
public class MedicalSupplyDispenseServiceTest {
	
	private MedicalSupplyDispenseServiceImpl medicalSupplyDispenseService;
	
	private MedicalSupplyDispenseDao medicalSupplyDispenseDao;
	
	@Before
	public void setup() {
		medicalSupplyDispenseService = new MedicalSupplyDispenseServiceImpl();
		medicalSupplyDispenseDao = mock(MedicalSupplyDispenseDao.class);
		medicalSupplyDispenseService.setMedicalSupplyDispenseDao(medicalSupplyDispenseDao);
	}
	
	/**
	 * Test that getMedicalSupplyDispenseByUuid returns the dispense when found
	 */
	@Test
	public void shouldReturnMedicalSupplyDispenseWhenFoundByUuid() {
		// Given
		String uuid = "dispense-uuid-123";
		MedicalSupplyDispense expectedDispense = new MedicalSupplyDispense();
		expectedDispense.setUuid(uuid);
		when(medicalSupplyDispenseDao.getMedicalSupplyDispenseByUuid(uuid)).thenReturn(Optional.of(expectedDispense));
		
		// When
		Optional<MedicalSupplyDispense> result = medicalSupplyDispenseService.getMedicalSupplyDispenseByUuid(uuid);
		
		// Then
		assertThat("Result should be present", result.isPresent(), equalTo(true));
		assertThat("Dispense UUID should match", result.get().getUuid(), equalTo(uuid));
		verify(medicalSupplyDispenseDao).getMedicalSupplyDispenseByUuid(uuid);
	}
	
	/**
	 * Test that getMedicalSupplyDispenseByUuid returns empty when not found
	 */
	@Test
	public void shouldReturnEmptyWhenMedicalSupplyDispenseNotFound() {
		// Given
		String uuid = "non-existent-uuid";
		when(medicalSupplyDispenseDao.getMedicalSupplyDispenseByUuid(uuid)).thenReturn(Optional.empty());
		
		// When
		Optional<MedicalSupplyDispense> result = medicalSupplyDispenseService.getMedicalSupplyDispenseByUuid(uuid);
		
		// Then
		assertThat("Result should be empty", result.isPresent(), equalTo(false));
		verify(medicalSupplyDispenseDao).getMedicalSupplyDispenseByUuid(uuid);
	}
	
	/**
	 * Test that saveOrUpdate saves the dispense through DAO
	 */
	@Test
	public void shouldSaveMedicalSupplyDispenseThroughDao() {
		// Given
		MedicalSupplyDispense dispense = new MedicalSupplyDispense();
		dispense.setUuid("new-dispense-uuid");
		dispense.setStatus(MedicalSupplyStatus.PREPARATION);
		when(medicalSupplyDispenseDao.saveOrUpdate(any(MedicalSupplyDispense.class))).thenReturn(dispense);
		
		// When
		MedicalSupplyDispense result = medicalSupplyDispenseService.saveOrUpdate(dispense);
		
		// Then
		assertThat("Saved dispense should not be null", result, notNullValue());
		assertThat("UUID should be preserved", result.getUuid(), equalTo("new-dispense-uuid"));
		assertThat("Status should be preserved", result.getStatus(), equalTo(MedicalSupplyStatus.PREPARATION));
		verify(medicalSupplyDispenseDao).saveOrUpdate(dispense);
	}
	
	/**
	 * Test that saveOrUpdate updates an existing dispense
	 */
	@Test
	public void shouldUpdateExistingMedicalSupplyDispense() {
		// Given
		MedicalSupplyDispense existingDispense = new MedicalSupplyDispense();
		existingDispense.setId(1);
		existingDispense.setUuid("existing-uuid");
		existingDispense.setStatus(MedicalSupplyStatus.IN_PROGRESS);
		
		when(medicalSupplyDispenseDao.saveOrUpdate(any(MedicalSupplyDispense.class))).thenReturn(existingDispense);
		
		// When
		existingDispense.setStatus(MedicalSupplyStatus.COMPLETED);
		MedicalSupplyDispense result = medicalSupplyDispenseService.saveOrUpdate(existingDispense);
		
		// Then
		assertThat("Updated dispense should not be null", result, notNullValue());
		assertThat("Status should be updated", result.getStatus(), equalTo(MedicalSupplyStatus.COMPLETED));
		verify(medicalSupplyDispenseDao).saveOrUpdate(existingDispense);
	}
	
	/**
	 * Test that saveOrUpdate preserves all fields
	 */
	@Test
	public void shouldPreserveAllFieldsWhenSaving() {
		// Given
		MedicalSupplyDispense dispense = new MedicalSupplyDispense();
		dispense.setUuid("complete-dispense-uuid");
		dispense.setQuantity(10.5);
		dispense.setStatus(MedicalSupplyStatus.ON_HOLD);
		dispense.setDateDispensed(new java.util.Date());
		
		when(medicalSupplyDispenseDao.saveOrUpdate(any(MedicalSupplyDispense.class))).thenReturn(dispense);
		
		// When
		MedicalSupplyDispense result = medicalSupplyDispenseService.saveOrUpdate(dispense);
		
		// Then
		assertThat("UUID should be preserved", result.getUuid(), equalTo("complete-dispense-uuid"));
		assertThat("Quantity should be preserved", result.getQuantity(), equalTo(10.5));
		assertThat("Status should be preserved", result.getStatus(), equalTo(MedicalSupplyStatus.ON_HOLD));
		assertThat("Date dispensed should be preserved", result.getDateDispensed(), notNullValue());
		verify(medicalSupplyDispenseDao).saveOrUpdate(dispense);
	}
}
