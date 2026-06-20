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
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openmrs.Concept;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.api.OrderContext;
import org.openmrs.api.OrderService;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyOrder;
import org.openmrs.module.orderexpansion.api.model.ProcedureOrder;

/**
 * Unit tests for {@link OrderServiceInterceptor} using mocking. These tests use Mockito to verify
 * the interceptor behavior without requiring a full OpenMRS context setup.
 */
public class OrderServiceInterceptorTest {
	
	private static final String PROCEDURE_ORDER_TYPE_UUID = "b4a7c280-369e-4d12-9ce8-18e36783fed6";
	
	private static final String MEDICAL_SUPPLY_ORDER_TYPE_UUID = "dab3ab30-2feb-48ec-b4af-8332a0831b49";
	
	private OrderService mockOrderService;
	
	private OrderService wrappedService;
	
	private OrderType procedureOrderType;
	
	private OrderType medicalSupplyOrderType;
	
	@Before
	public void setup() {
		// Create mock OrderService
		mockOrderService = mock(OrderService.class);
		
		// Setup OrderType mocks
		procedureOrderType = mock(OrderType.class);
		when(procedureOrderType.getUuid()).thenReturn(PROCEDURE_ORDER_TYPE_UUID);
		when(procedureOrderType.getName()).thenReturn("Procedure Order");
		
		medicalSupplyOrderType = mock(OrderType.class);
		when(medicalSupplyOrderType.getUuid()).thenReturn(MEDICAL_SUPPLY_ORDER_TYPE_UUID);
		when(medicalSupplyOrderType.getName()).thenReturn("Medical Supplies Order");
		
		// Setup OrderService.getOrderTypeByUuid behavior
		when(mockOrderService.getOrderTypeByUuid(PROCEDURE_ORDER_TYPE_UUID)).thenReturn(procedureOrderType);
		when(mockOrderService.getOrderTypeByUuid(MEDICAL_SUPPLY_ORDER_TYPE_UUID)).thenReturn(medicalSupplyOrderType);
		
		// Setup saveOrder to track invocations and update orders
		doAnswer(new Answer<Order>() {
			
			@Override
			public Order answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				if (args.length > 0 && args[0] instanceof Order) {
					Order order = (Order) args[0];
					// If order type is set by interceptor, preserve it
					if (order.getOrderType() != null) {
						return order;
					}
				}
				return (Order) args[0];
			}
		}).when(mockOrderService).saveOrder(any(Order.class), any(OrderContext.class));
		
		// Create wrapped service with interceptor
		wrappedService = OrderServiceInterceptor.createProxy(mockOrderService);
	}
	
	/**
	 * Test that createProxy returns an OrderService instance
	 */
	@Test
	public void shouldCreateOrderServiceProxy() {
		assertThat("Wrapped service should not be null", wrappedService, notNullValue());
		assertThat("Wrapped service should be instance of OrderService", wrappedService, instanceOf(OrderService.class));
		assertThat("Wrapped service should be a proxy", wrappedService.getClass().getName(),
		    org.hamcrest.CoreMatchers.containsString("Proxy"));
	}
	
	/**
	 * Test that ProcedureOrder gets order type set when saved
	 */
	@Test
	public void shouldSetOrderTypeForProcedureOrder() throws Exception {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		OrderContext orderContext = new OrderContext();
		
		// When
		wrappedService.saveOrder(procedureOrder, orderContext);
		
		// Then
		assertThat("Order type should be set", procedureOrder.getOrderType(), notNullValue());
		assertThat("Order type UUID should match", procedureOrder.getOrderType().getUuid(),
		    equalTo(PROCEDURE_ORDER_TYPE_UUID));
		assertThat("Context order type should also be set", orderContext.getOrderType(), notNullValue());
		assertThat("Context order type UUID should match", orderContext.getOrderType().getUuid(),
		    equalTo(PROCEDURE_ORDER_TYPE_UUID));
	}
	
	/**
	 * Test that MedicalSupplyOrder gets order type set when saved
	 */
	@Test
	public void shouldSetOrderTypeForMedicalSupplyOrder() throws Exception {
		// Given
		MedicalSupplyOrder medicalSupplyOrder = new MedicalSupplyOrder();
		OrderContext orderContext = new OrderContext();
		
		// When
		wrappedService.saveOrder(medicalSupplyOrder, orderContext);
		
		// Then
		assertThat("Order type should be set", medicalSupplyOrder.getOrderType(), notNullValue());
		assertThat("Order type UUID should match", medicalSupplyOrder.getOrderType().getUuid(),
		    equalTo(MEDICAL_SUPPLY_ORDER_TYPE_UUID));
		assertThat("Context order type should also be set", orderContext.getOrderType(), notNullValue());
		assertThat("Context order type UUID should match", orderContext.getOrderType().getUuid(),
		    equalTo(MEDICAL_SUPPLY_ORDER_TYPE_UUID));
	}
	
	/**
	 * Test that ProcedureOrder without context still gets order type set
	 */
	@Test
	public void shouldSetOrderTypeForProcedureOrderWithoutContext() throws Exception {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		
		// When
		wrappedService.saveOrder(procedureOrder, (OrderContext) null);
		
		// Then
		assertThat("Order type should be set", procedureOrder.getOrderType(), notNullValue());
		assertThat("Order type UUID should match", procedureOrder.getOrderType().getUuid(),
		    equalTo(PROCEDURE_ORDER_TYPE_UUID));
	}
	
	/**
	 * Test that order with order type already set is not modified
	 */
	@Test
	public void shouldNotModifyOrderWhenOrderTypeAlreadySet() throws Exception {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		OrderType existingType = mock(OrderType.class);
		when(existingType.getUuid()).thenReturn("some-other-uuid");
		procedureOrder.setOrderType(existingType);
		OrderContext orderContext = new OrderContext();
		
		// When
		wrappedService.saveOrder(procedureOrder, orderContext);
		
		// Then
		assertThat("Order type should not be changed", procedureOrder.getOrderType().getUuid(), equalTo("some-other-uuid"));
	}
	
	/**
	 * Test that order with context order type set is not modified
	 */
	@Test
	public void shouldUseContextOrderTypeWhenSet() throws Exception {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		OrderContext orderContext = new OrderContext();
		OrderType contextType = mock(OrderType.class);
		when(contextType.getUuid()).thenReturn("context-type-uuid");
		orderContext.setOrderType(contextType);
		
		// When
		wrappedService.saveOrder(procedureOrder, orderContext);
		
		// Then
		assertThat("Order type should match context type", procedureOrder.getOrderType().getUuid(),
		    equalTo("context-type-uuid"));
	}
	
	/**
	 * Test that standard Order (not custom type) is handled gracefully
	 */
	@Test
	public void shouldHandleStandardOrderGracefully() throws Exception {
		// Given
		Order standardOrder = new Order();
		OrderContext orderContext = new OrderContext();
		
		// When
		wrappedService.saveOrder(standardOrder, orderContext);
		
		// Then - order type should not be set ( interceptor doesn't know this type)
		assertThat("Standard order type should not be modified", standardOrder.getOrderType(), nullValue());
	}
	
	/**
	 * Test that the wrapped service delegates to the original service
	 */
	@Test
	public void shouldDelegateToOriginalService() throws Exception {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		OrderContext orderContext = new OrderContext();
		
		// When
		wrappedService.saveOrder(procedureOrder, orderContext);
		
		// Then - verify the original service was called
		verify(mockOrderService).saveOrder(any(Order.class), any(OrderContext.class));
	}
	
	/**
	 * Test that the interceptor handles saveOrder with null context
	 */
	@Test
	public void shouldHandleSaveOrderWithNullContext() throws Exception {
		// Given
		ProcedureOrder procedureOrder = new ProcedureOrder();
		
		// When
		wrappedService.saveOrder(procedureOrder, (OrderContext) null);
		
		// Then
		assertThat("Order type should be set even with null context", procedureOrder.getOrderType(), notNullValue());
		assertThat("Order type UUID should match", procedureOrder.getOrderType().getUuid(),
		    equalTo(PROCEDURE_ORDER_TYPE_UUID));
	}
	
	/**
	 * Test that concept-based order type lookup is attempted before fallback
	 */
	@Test
	public void shouldAttemptConceptBasedOrderTypeLookup() throws Exception {
		// Given
		Concept concept = mock(Concept.class);
		OrderType conceptOrderType = mock(OrderType.class);
		when(conceptOrderType.getUuid()).thenReturn("concept-based-type-uuid");
		when(mockOrderService.getOrderTypeByConcept(concept)).thenReturn(conceptOrderType);
		
		ProcedureOrder procedureOrder = new ProcedureOrder();
		procedureOrder.setConcept(concept);
		OrderContext orderContext = new OrderContext();
		
		// When
		wrappedService.saveOrder(procedureOrder, orderContext);
		
		// Then - should use concept-based type if available
		assertThat("Order type should be set", procedureOrder.getOrderType(), notNullValue());
		verify(mockOrderService).getOrderTypeByConcept(concept);
	}
}
