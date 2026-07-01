/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.web.resources;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.Order;
import org.openmrs.api.context.Context;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyOrder;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.SubClassHandler;
import org.openmrs.module.webservices.rest.web.api.RestService;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.BaseDelegatingSubclassHandler;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingSubclassHandler;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;

@SubClassHandler(supportedClass = MedicalSupplyOrder.class, supportedOpenmrsVersions = { "2.8.* - 9.*" })
public class MedicalSupplyOrderSubclassHandler extends BaseDelegatingSubclassHandler<Order, MedicalSupplyOrder> implements DelegatingSubclassHandler<Order, MedicalSupplyOrder> {
	
	private OrderResource2_3 getCachedOrderResource() {
		return (OrderResource2_3) Context.getService(RestService.class).getResourceBySupportedClass(Order.class);
	}
	
	@Override
	public String getTypeName() {
		return "medicalsupplyorder";
	}
	
	@Override
	public PageableResult getAllByType(RequestContext requestContext) throws ResourceDoesNotSupportOperationException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public MedicalSupplyOrder newDelegate() {
		return new MedicalSupplyOrder();
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() throws ResourceDoesNotSupportOperationException {
		OrderResource2_3 orderResource = (OrderResource2_3) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		return orderResource.getUpdatableProperties();
	}
	
	@PropertyGetter("display")
	public static String getDisplay(MedicalSupplyOrder delegate) {
		OrderResource2_3 orderResource = (OrderResource2_3) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		return orderResource.getDisplayString(delegate);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		if (rep instanceof DefaultRepresentation) {
			OrderResource2_3 orderResource = getCachedOrderResource();
			DelegatingResourceDescription d = orderResource.getRepresentationDescription(rep);
			d.addProperty("quantity", Representation.REF);
			d.addProperty("medicalSuppliesInventoryId");
			d.addProperty("brandName");
			d.addProperty("quantityUnits", Representation.REF);
			return d;
		} else if (rep instanceof FullRepresentation) {
			OrderResource2_3 orderResource = getCachedOrderResource();
			DelegatingResourceDescription d = orderResource.getRepresentationDescription(rep);
			d.addProperty("quantity", Representation.FULL);
			d.addProperty("medicalSuppliesInventoryId");
			d.addProperty("brandName");
			d.addProperty("quantityUnits", Representation.DEFAULT);
			return d;
		} else if (rep instanceof CustomRepresentation) { // custom rep
			return null;
		}
		return null;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		OrderResource2_3 orderResource = (OrderResource2_3) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		DelegatingResourceDescription d = orderResource.getCreatableProperties();
		d.addProperty("quantity");
		d.addProperty("medicalSuppliesInventoryId");
		d.addProperty("brandName");
		d.addProperty("quantityUnits");
		return d;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		OrderResource2_3 orderResource = (OrderResource2_3) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		ModelImpl orderModel = (ModelImpl) orderResource.getGETModel(rep);
		orderModel.property("medicalSuppliesInventoryId", new StringProperty()).property("quantity", new DoubleProperty())
		        .property("brandName", new StringProperty()).property("quantityUnits", new StringProperty());
		
		return orderModel;
	}
	
	@Override
	public Model getCREATEModel(Representation rep) {
		OrderResource2_3 orderResource = (OrderResource2_3) Context.getService(RestService.class)
		        .getResourceBySupportedClass(Order.class);
		ModelImpl orderModel = (ModelImpl) orderResource.getCREATEModel(rep);
		return orderModel.property("medicalSuppliesInventoryId", new StringProperty().example("uuid"))
		        .property("quantityUnits", new StringProperty()).property("brandName", new StringProperty())
		        .property("quantity", new DoubleProperty());
	}
	
}
