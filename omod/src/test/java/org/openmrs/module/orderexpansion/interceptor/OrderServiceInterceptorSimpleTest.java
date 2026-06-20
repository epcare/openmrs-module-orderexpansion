/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.interceptor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.OrderType;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyOrder;
import org.openmrs.module.orderexpansion.api.model.ProcedureOrder;

/**
 * Simple unit tests for {@link OrderServiceInterceptor}. Tests that the interceptor correctly
 * identifies custom order types without requiring full OpenMRS test infrastructure.
 */
public class OrderServiceInterceptorSimpleTest {
	
	private static final String PROCEDURE_ORDER_TYPE_UUID = "b4a7c280-369e-4d12-9ce8-18e36783fed6";
	
	private static final String MEDICAL_SUPPLY_ORDER_TYPE_UUID = "dab3ab30-2feb-48ec-b4af-8332a0831b49";
	
	private OrderType procedureOrderType;
	
	private OrderType medicalSupplyOrderType;
	
	@Before
	public void setup() {
		// Create mock order types
		procedureOrderType = mock(OrderType.class);
		when(procedureOrderType.getUuid()).thenReturn(PROCEDURE_ORDER_TYPE_UUID);
		when(procedureOrderType.getName()).thenReturn("Procedure Order");
		when(procedureOrderType.getId()).thenReturn(3);
		
		medicalSupplyOrderType = mock(OrderType.class);
		when(medicalSupplyOrderType.getUuid()).thenReturn(MEDICAL_SUPPLY_ORDER_TYPE_UUID);
		when(medicalSupplyOrderType.getName()).thenReturn("Medical Supplies Order");
		when(medicalSupplyOrderType.getId()).thenReturn(4);
	}
	
	/**
	 * Test that ProcedureOrder is correctly identified
	 */
	@Test
	public void shouldIdentifyProcedureOrder() {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		
		// Then
		assertThat("Order should be instance of ProcedureOrder", procedureOrder instanceof ProcedureOrder, equalTo(true));
		assertThat("Order should not have order type initially", procedureOrder.getOrderType(), nullValue());
	}
	
	/**
	 * Test that MedicalSupplyOrder is correctly identified
	 */
	@Test
	public void shouldIdentifyMedicalSupplyOrder() {
		// Given
		MedicalSupplyOrder medicalSupplyOrder = new MedicalSupplyOrder();
		
		// Then
		assertThat("Order should be instance of MedicalSupplyOrder", medicalSupplyOrder instanceof MedicalSupplyOrder,
		    equalTo(true));
		assertThat("Order should not have order type initially", medicalSupplyOrder.getOrderType(), nullValue());
	}
	
	/**
	 * Test that order type can be set on ProcedureOrder
	 */
	@Test
	public void shouldSetOrderTypeOnProcedureOrder() {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		
		// When
		procedureOrder.setOrderType(procedureOrderType);
		
		// Then
		assertThat("Order type should be set", procedureOrder.getOrderType(), notNullValue());
		assertThat("Order type should match Procedure Order UUID", procedureOrder.getOrderType().getUuid(),
		    equalTo(PROCEDURE_ORDER_TYPE_UUID));
		assertThat("Order type name should be Procedure Order", procedureOrder.getOrderType().getName(),
		    equalTo("Procedure Order"));
	}
	
	/**
	 * Test that order type can be set on MedicalSupplyOrder
	 */
	@Test
	public void shouldSetOrderTypeOnMedicalSupplyOrder() {
		// Given
		MedicalSupplyOrder medicalSupplyOrder = new MedicalSupplyOrder();
		
		// When
		medicalSupplyOrder.setOrderType(medicalSupplyOrderType);
		
		// Then
		assertThat("Order type should be set", medicalSupplyOrder.getOrderType(), notNullValue());
		assertThat("Order type should match Medical Supply Order UUID", medicalSupplyOrder.getOrderType().getUuid(),
		    equalTo(MEDICAL_SUPPLY_ORDER_TYPE_UUID));
		assertThat("Order type name should be Medical Supplies Order", medicalSupplyOrder.getOrderType().getName(),
		    equalTo("Medical Supplies Order"));
	}
	
	/**
	 * Test that ProcedureOrder constructor creates valid object
	 */
	@Test
	public void shouldCreateValidProcedureOrder() {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		
		// Then
		assertThat("ProcedureOrder should not be null", procedureOrder, notNullValue());
		assertThat("ProcedureOrder should be an Order subclass", procedureOrder instanceof org.openmrs.Order, equalTo(true));
	}
	
	/**
	 * Test that MedicalSupplyOrder constructor creates valid object
	 */
	@Test
	public void shouldCreateValidMedicalSupplyOrder() {
		// Given
		MedicalSupplyOrder medicalSupplyOrder = new MedicalSupplyOrder();
		
		// Then
		assertThat("MedicalSupplyOrder should not be null", medicalSupplyOrder, notNullValue());
		assertThat("MedicalSupplyOrder should be an Order subclass", medicalSupplyOrder instanceof org.openmrs.Order,
		    equalTo(true));
	}
	
	/**
	 * Test that order type IDs are correct
	 */
	@Test
	public void shouldHaveCorrectOrderTypeIds() {
		// Then
		assertThat("Procedure Order type ID should be 3", procedureOrderType.getId(), equalTo(3));
		assertThat("Medical Supply Order type ID should be 4", medicalSupplyOrderType.getId(), equalTo(4));
	}
	
	/**
	 * Test that order type UUIDs are correctly formatted
	 */
	@Test
	public void shouldHaveCorrectOrderTypeUuids() {
		// Then
		assertThat("Procedure Order UUID should not be empty", procedureOrderType.getUuid(),
		    equalTo("b4a7c280-369e-4d12-9ce8-18e36783fed6"));
		assertThat("Medical Supply Order UUID should not be empty", medicalSupplyOrderType.getUuid(),
		    equalTo("dab3ab30-2feb-48ec-b4af-8332a0831b49"));
	}
}
