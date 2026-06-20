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

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.orderexpansion.api.dao.ProcedureDao;
import org.openmrs.module.orderexpansion.api.impl.ProcedureServiceImpl;
import org.openmrs.module.orderexpansion.api.model.Procedure;

/**
 * Unit tests for {@link ProcedureService}. These tests verify the behavior of the ProcedureService
 * implementation using Mockito for dependency injection and mocking.
 */
public class ProcedureServiceTest {
	
	private ProcedureServiceImpl procedureService;
	
	private ProcedureDao procedureDao;
	
	@Before
	public void setup() {
		procedureService = new ProcedureServiceImpl();
		procedureDao = mock(ProcedureDao.class);
		procedureService.setProcedureDao(procedureDao);
	}
	
	/**
	 * Test that getProcedureByUuid returns the procedure when found
	 */
	@Test
	public void shouldReturnProcedureWhenFoundByUuid() {
		// Given
		String uuid = "test-uuid-123";
		Procedure expectedProcedure = new Procedure();
		expectedProcedure.setUuid(uuid);
		when(procedureDao.getProcedureByUuid(uuid)).thenReturn(Optional.of(expectedProcedure));
		
		// When
		Optional<Procedure> result = procedureService.getProcedureByUuid(uuid);
		
		// Then
		assertThat("Result should be present", result.isPresent(), equalTo(true));
		assertThat("Procedure UUID should match", result.get().getUuid(), equalTo(uuid));
		verify(procedureDao).getProcedureByUuid(uuid);
	}
	
	/**
	 * Test that getProcedureByUuid returns empty when not found
	 */
	@Test
	public void shouldReturnEmptyWhenProcedureNotFound() {
		// Given
		String uuid = "non-existent-uuid";
		when(procedureDao.getProcedureByUuid(uuid)).thenReturn(Optional.empty());
		
		// When
		Optional<Procedure> result = procedureService.getProcedureByUuid(uuid);
		
		// Then
		assertThat("Result should be empty", result.isPresent(), equalTo(false));
		verify(procedureDao).getProcedureByUuid(uuid);
	}
	
	/**
	 * Test that saveOrUpdate saves the procedure through DAO
	 */
	@Test
	public void shouldSaveProcedureThroughDao() {
		// Given
		Procedure procedure = new Procedure();
		procedure.setUuid("new-procedure-uuid");
		procedure.setEncounters(new ArrayList<>());
		when(procedureDao.saveOrUpdate(any(Procedure.class))).thenReturn(procedure);
		
		// When
		Procedure result = procedureService.saveOrUpdate(procedure);
		
		// Then
		assertThat("Saved procedure should not be null", result, notNullValue());
		assertThat("UUID should be preserved", result.getUuid(), equalTo("new-procedure-uuid"));
		verify(procedureDao).saveOrUpdate(any(Procedure.class));
	}
	
	/**
	 * Test that saveOrUpdate handles empty encounters list
	 */
	@Test
	public void shouldHandleEmptyEncountersList() {
		// Given
		Procedure procedure = new Procedure();
		procedure.setUuid("procedure-no-encounters");
		procedure.setEncounters(new ArrayList<>());
		when(procedureDao.saveOrUpdate(any(Procedure.class))).thenReturn(procedure);
		
		// When
		Procedure result = procedureService.saveOrUpdate(procedure);
		
		// Then
		assertThat("Procedure should be saved", result, notNullValue());
		assertThat("Encounters should be empty", result.getEncounters().isEmpty(), equalTo(true));
		verify(procedureDao).saveOrUpdate(procedure);
	}
	
	/**
	 * Test that saveOrUpdate handles null encounters
	 */
	@Test(expected = NullPointerException.class)
	public void shouldHandleNullEncounters() {
		// Given
		Procedure procedure = new Procedure();
		procedure.setUuid("procedure-null-encounters");
		procedure.setEncounters(null);
		when(procedureDao.saveOrUpdate(any(Procedure.class))).thenReturn(procedure);
		
		// When
		procedureService.saveOrUpdate(procedure);
		
		// Then - should throw NullPointerException
	}
	
	/**
	 * Test that saveOrUpdate calls DAO with procedure
	 */
	@Test
	public void shouldCallDaoWhenSavingProcedure() {
		// Given
		Procedure procedure = new Procedure();
		procedure.setUuid("test-procedure");
		procedure.setEncounters(new ArrayList<>());
		when(procedureDao.saveOrUpdate(any(Procedure.class))).thenReturn(procedure);
		
		// When
		Procedure result = procedureService.saveOrUpdate(procedure);
		
		// Then
		assertThat("Procedure should be saved", result, notNullValue());
		assertThat("UUID should match", result.getUuid(), equalTo("test-procedure"));
		verify(procedureDao).saveOrUpdate(any(Procedure.class));
	}
}
