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

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.api.OrderContext;
import org.openmrs.api.OrderService;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyOrder;
import org.openmrs.module.orderexpansion.api.model.ProcedureOrder;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Interceptor for OrderService that handles custom order types (ProcedureOrder and
 * MedicalSupplyOrder) by setting the appropriate order type before saving. This is needed because
 * the core OpenMRS OrderService.ensureOrderTypeIsSet() method only handles standard order types
 * (DrugOrder, TestOrder, ReferralOrder) and doesn't know about custom order types from this module.
 */
public class OrderServiceInterceptor {
	
	private static final Log log = LogFactory.getLog(OrderServiceInterceptor.class);
	
	// UUIDs for custom order types - these should match the ordertypes.csv configuration
	private static final String PROCEDURE_ORDER_TYPE_UUID = "b4a7c280-369e-4d12-9ce8-18e36783fed6";
	
	private static final String MEDICAL_SUPPLY_ORDER_TYPE_UUID = "dab3ab30-2feb-48ec-b4af-8332a0831b49";
	
	/**
	 * Creates a proxy for the OrderService that intercepts saveOrder calls
	 *
	 * @param originalService The original OrderService
	 * @return A proxy that intercepts saveOrder calls
	 */
	public static OrderService createProxy(OrderService originalService) {
		ProxyFactory proxyFactory = new ProxyFactory(originalService);
		proxyFactory.addInterface(OrderService.class);
		proxyFactory.addAdvice(new OrderServiceMethodInterceptor(originalService));
		return (OrderService) proxyFactory.getProxy();
	}
	
	/**
	 * Method interceptor that processes saveOrder calls to ensure custom order types have their order
	 * type set correctly.
	 */
	static class OrderServiceMethodInterceptor implements org.aopalliance.intercept.MethodInterceptor {
		
		private final OrderService orderService;
		
		OrderServiceMethodInterceptor(OrderService orderService) {
			this.orderService = orderService;
		}
		
		@Override
		public Object invoke(org.aopalliance.intercept.MethodInvocation methodInvocation) throws Throwable {
			Method method = methodInvocation.getMethod();
			Object[] args = methodInvocation.getArguments();
			
			// Intercept saveOrder methods with Order parameter
			if (method.getName().equals("saveOrder") && args.length > 0 && args[0] instanceof Order) {
				Order order = (Order) args[0];
				OrderContext orderContext = null;
				
				// Extract OrderContext from different method signatures
				if (args.length >= 2 && args[1] instanceof OrderContext) {
					orderContext = (OrderContext) args[1];
				}
				
				// Handle custom order types
				if (order.getOrderType() == null || (orderContext != null && orderContext.getOrderType() == null)) {
					enhanceOrderContextForCustomTypes(order, orderContext);
				}
			}
			
			return methodInvocation.proceed();
		}
		
		/**
		 * Enhances the OrderContext with the appropriate order type for custom order types
		 *
		 * @param order The order being saved
		 * @param orderContext The order context (may be null)
		 */
		private void enhanceOrderContextForCustomTypes(Order order, OrderContext orderContext) {
			OrderType orderType = null;
			
			// Try to get order type from context first
			if (orderContext != null && orderContext.getOrderType() != null) {
				orderType = orderContext.getOrderType();
			}
			
			// If still null, try to determine from concept
			if (orderType == null && order.getConcept() != null) {
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
			}
		}
	}
}
