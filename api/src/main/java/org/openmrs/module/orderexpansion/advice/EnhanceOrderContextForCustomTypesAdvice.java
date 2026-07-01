/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.advice;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.api.OrderContext;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyOrder;
import org.openmrs.module.orderexpansion.api.model.ProcedureOrder;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * Advice for OrderService that handles custom order types (ProcedureOrder and MedicalSupplyOrder)
 * by setting the appropriate order type before saving. This is needed because the core OpenMRS
 * OrderService.ensureOrderTypeIsSet() method only handles standard order types (DrugOrder,
 * TestOrder, ReferralOrder) and doesn't know about custom order types from this module.
 */
public class EnhanceOrderContextForCustomTypesAdvice implements MethodBeforeAdvice {
	
	private static final Log log = LogFactory.getLog(EnhanceOrderContextForCustomTypesAdvice.class);
	
	// UUIDs for custom order types - these should match the ordertypes.csv configuration
	private static final String PROCEDURE_ORDER_TYPE_UUID = "4237a01f-29c5-4167-9d8e-96d6e590aa33";
	
	private static final String MEDICAL_SUPPLY_ORDER_TYPE_UUID = "dab3ab30-2feb-48ec-b4af-8332a0831b49";
	
	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		try {
			if (method.getName().equals("saveOrder") && args.length > 0 && args[0] instanceof Order) {
				Order order = (Order) args[0];
				OrderContext orderContext = null;
				
				// Extract OrderContext from different method signatures
				if (args.length >= 2 && args[1] instanceof OrderContext) {
					orderContext = (OrderContext) args[1];
				}
				
				// Log entry for debugging
				if (log.isDebugEnabled()) {
					log.debug("EnhanceOrderContextForCustomTypesAdvice invoked for order: " + order.getUuid() + ", class: "
					        + order.getClass().getSimpleName() + ", orderType: "
					        + (order.getOrderType() != null ? order.getOrderType().getName() : "null")
					        + ", context.orderType: "
					        + (orderContext != null && orderContext.getOrderType() != null
					                ? orderContext.getOrderType().getName()
					                : "null"));
				}
				
				// Handle custom order types - only if order type needs to be set
				if (order.getOrderType() == null || (orderContext != null && orderContext.getOrderType() == null)) {
					enhanceOrderContextForCustomTypes(order, orderContext);
				}
			}
		}
		catch (Exception e) {
			log.error("Error in EnhanceOrderContextForCustomTypesAdvice: " + e.getMessage(), e);
			// Re-throw to ensure the operation doesn't proceed with incomplete state
			throw e;
		}
	}
	
	/**
	 * Enhances the OrderContext with the appropriate order type for custom order types
	 *
	 * @param order The order being saved
	 * @param orderContext The order context (may be null)
	 */
	private void enhanceOrderContextForCustomTypes(Order order, OrderContext orderContext) {
		// If both are already set, no need to proceed
		if (order.getOrderType() != null && (orderContext == null || orderContext.getOrderType() != null)) {
			return;
		}
		
		OrderService orderService = Context.getOrderService();
		OrderType orderType = null;
		
		// Try to get order type from concept first
		if (order.getConcept() != null) {
			try {
				orderType = orderService.getOrderTypeByConcept(order.getConcept());
			}
			catch (Exception e) {
				// Context might not be available in tests, fall through to UUID lookup
				if (log.isDebugEnabled()) {
					log.debug("Could not get order type by concept: " + e.getMessage());
				}
			}
		}
		
		// Check for custom order types if still null
		if (orderType == null) {
			if (order instanceof ProcedureOrder) {
				try {
					orderType = orderService.getOrderTypeByUuid(PROCEDURE_ORDER_TYPE_UUID);
					if (log.isDebugEnabled()) {
						log.debug("Setting order type for ProcedureOrder to: "
						        + (orderType != null ? orderType.getName() : "null"));
					}
				}
				catch (Exception e) {
					log.warn("Could not get Procedure order type by UUID: " + e.getMessage());
				}
			} else if (order instanceof MedicalSupplyOrder) {
				try {
					orderType = orderService.getOrderTypeByUuid(MEDICAL_SUPPLY_ORDER_TYPE_UUID);
					if (log.isDebugEnabled()) {
						log.debug("Setting order type for MedicalSupplyOrder to: "
						        + (orderType != null ? orderType.getName() : "null"));
					}
				}
				catch (Exception e) {
					log.warn("Could not get Medical Supply order type by UUID: " + e.getMessage());
				}
			}
		}
		
		// Set the order type on the order and context
		if (orderType != null) {
			if (orderContext != null && orderContext.getOrderType() == null) {
				orderContext.setOrderType(orderType);
			}
			if (order.getOrderType() == null) {
				order.setOrderType(orderType);
			}
			if (log.isDebugEnabled()) {
				log.debug("Successfully set order type '" + orderType.getName() + "' (" + orderType.getUuid()
				        + ") for order: " + order.getUuid());
			}
		} else if (log.isDebugEnabled()) {
			log.debug("Could not determine order type for order: " + order.getUuid() + ", class: "
			        + order.getClass().getSimpleName());
		}
	}
}
