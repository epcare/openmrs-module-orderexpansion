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
import org.openmrs.module.orderexpansion.api.dao.OrderExpansionDao;
import org.openmrs.module.orderexpansion.api.impl.OrderExpansionServiceImpl;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense.MedicalSupplyStatus;

/**
 * Unit tests for {@link OrderExpansionService}. These tests verify the behavior of the
 * OrderExpansionService implementation using Mockito for dependency injection and mocking.
 * <p>
 * This test class covers MedicalSupplyDispense functionality.
 */
public class OrderExpansionServiceTest {
	
	private OrderExpansionServiceImpl orderExpansionService;
	
	private OrderExpansionDao orderExpansionDao;
	
	@Before
	public void setup() {
		orderExpansionService = new OrderExpansionServiceImpl();
		orderExpansionDao = mock(OrderExpansionDao.class);
		orderExpansionService.setDao(orderExpansionDao);
	}
	
	// ==================== MedicalSupplyDispense Tests ====================
	
	/**
	 * Test that getMedicalSupplyDispenseByUuid returns the dispense when found
	 */
	@Test
	public void shouldReturnMedicalSupplyDispenseWhenFoundByUuid() {
		// Given
		String uuid = "dispense-uuid-123";
		MedicalSupplyDispense expectedDispense = new MedicalSupplyDispense();
		expectedDispense.setUuid(uuid);
		when(orderExpansionDao.getMedicalSupplyDispenseByUuid(uuid)).thenReturn(Optional.of(expectedDispense));
		
		// When
		Optional<MedicalSupplyDispense> result = orderExpansionService.getMedicalSupplyDispenseByUuid(uuid);
		
		// Then
		assertThat("Result should be present", result.isPresent(), equalTo(true));
		assertThat("Dispense UUID should match", result.get().getUuid(), equalTo(uuid));
		verify(orderExpansionDao).getMedicalSupplyDispenseByUuid(uuid);
	}
	
	/**
	 * Test that getMedicalSupplyDispenseByUuid returns empty when not found
	 */
	@Test
	public void shouldReturnEmptyWhenMedicalSupplyDispenseNotFound() {
		// Given
		String uuid = "non-existent-uuid";
		when(orderExpansionDao.getMedicalSupplyDispenseByUuid(uuid)).thenReturn(Optional.empty());
		
		// When
		Optional<MedicalSupplyDispense> result = orderExpansionService.getMedicalSupplyDispenseByUuid(uuid);
		
		// Then
		assertThat("Result should be empty", result.isPresent(), equalTo(false));
		verify(orderExpansionDao).getMedicalSupplyDispenseByUuid(uuid);
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
		when(orderExpansionDao.saveOrUpdate(any(MedicalSupplyDispense.class))).thenReturn(dispense);
		
		// When
		MedicalSupplyDispense result = orderExpansionService.saveOrUpdate(dispense);
		
		// Then
		assertThat("Saved dispense should not be null", result, notNullValue());
		assertThat("UUID should be preserved", result.getUuid(), equalTo("new-dispense-uuid"));
		assertThat("Status should be preserved", result.getStatus(), equalTo(MedicalSupplyStatus.PREPARATION));
		verify(orderExpansionDao).saveOrUpdate(any(MedicalSupplyDispense.class));
	}
	
	/**
	 * Test that saveOrUpdate updates an existing dispense through DAO
	 */
	@Test
	public void shouldUpdateMedicalSupplyDispenseThroughDao() {
		// Given
		MedicalSupplyDispense dispense = new MedicalSupplyDispense();
		dispense.setId(789);
		dispense.setUuid("existing-dispense-uuid");
		dispense.setStatus(MedicalSupplyStatus.COMPLETED);
		when(orderExpansionDao.saveOrUpdate(any(MedicalSupplyDispense.class))).thenReturn(dispense);
		
		// When
		MedicalSupplyDispense result = orderExpansionService.saveOrUpdate(dispense);
		
		// Then
		assertThat("Updated dispense should not be null", result, notNullValue());
		assertThat("ID should be preserved", result.getId(), equalTo(789));
		assertThat("UUID should be preserved", result.getUuid(), equalTo("existing-dispense-uuid"));
		assertThat("Status should be preserved", result.getStatus(), equalTo(MedicalSupplyStatus.COMPLETED));
		verify(orderExpansionDao).saveOrUpdate(any(MedicalSupplyDispense.class));
	}
}
