/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.orderexpansion.interceptor.OrderServiceInterceptor;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory bean that creates a wrapped OrderService with custom order type support. This factory
 * intercepts OrderService calls to ensure custom order types (ProcedureOrder, MedicalSupplyOrder)
 * have their order types properly set before being saved.
 */
public class OrderServiceFactoryBean implements FactoryBean<OrderService> {
	
	private static final Log log = LogFactory.getLog(OrderServiceFactoryBean.class);
	
	private OrderService originalService;
	
	/**
	 * Sets the original OrderService to wrap
	 *
	 * @param originalService The original service
	 */
	public void setOriginalService(OrderService originalService) {
		this.originalService = originalService;
	}
	
	@Override
	public OrderService getObject() throws Exception {
		if (originalService == null) {
			log.warn("Original OrderService is null, attempting to get from context");
			originalService = Context.getOrderService();
		}
		return OrderServiceInterceptor.createProxy(originalService);
	}
	
	@Override
	public Class<?> getObjectType() {
		return OrderService.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
}
