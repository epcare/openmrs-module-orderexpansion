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

import java.util.Optional;

import org.openmrs.api.context.Context;
import org.openmrs.module.orderexpansion.api.OrderExpansionService;
import org.openmrs.module.orderexpansion.api.model.MedicalSupplyDispense;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1
        + "/medicalsupplydispense", supportedClass = MedicalSupplyDispense.class, supportedOpenmrsVersions = {
                "2.8.* - 9.*" })
public class MedicalSupplyDispenseResource extends DataDelegatingCrudResource<MedicalSupplyDispense> {
	
	private OrderExpansionService orderExpansionService;
	
	public MedicalSupplyDispenseResource() {
		this.orderExpansionService = Context.getService(OrderExpansionService.class);
	}
	
	@Override
	public MedicalSupplyDispense getByUniqueId(String uuid) {
		Optional<MedicalSupplyDispense> medicalSupplyDispense = orderExpansionService.getMedicalSupplyDispenseByUuid(uuid);
		if (medicalSupplyDispense.isPresent()) {
			return medicalSupplyDispense.get();
		} else {
			return null;
		}
	}
	
	@Override
	protected void delete(MedicalSupplyDispense medicalSupplyDispense, String s, RequestContext requestContext)
	        throws ResponseException {
		
	}
	
	@Override
	public MedicalSupplyDispense newDelegate() {
		return new MedicalSupplyDispense();
	}
	
	@Override
	public MedicalSupplyDispense save(MedicalSupplyDispense medicalSupplyDispense) {
		return orderExpansionService.saveOrUpdate(medicalSupplyDispense);
	}
	
	@Override
	public void purge(MedicalSupplyDispense medicalSupplyDispense, RequestContext requestContext) throws ResponseException {
		
	}
	
	// @Override
	// protected PageableResult doSearch(RequestContext requestContext) {
	// String orderUuid = requestContext.getParameter("orderUuid");
	// String orderTypeUuid = requestContext.getParameter("orderTypeUuid");
	
	// return new NeedsPaging<>(new ArrayList<>(), requestContext);
	// }
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("patient");
		description.addProperty("encounter");
		description.addProperty("medicalSupplyOrder");
		description.addProperty("concept");
		description.addProperty("quantity");
		description.addProperty("quantityUnits");
		description.addProperty("dateDispensed");
		description.addProperty("status");
		description.addProperty("statusReason");
		description.addProperty("location");
		description.addProperty("encounter");
		description.addProperty("dispenser");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation representation) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (representation instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("patient", Representation.REF);
			description.addProperty("medicalSupplyOrder", Representation.REF);
			description.addProperty("concept", Representation.REF);
			description.addProperty("quantity", Representation.REF);
			description.addProperty("quantityUnits", Representation.REF);
			description.addProperty("dateDispensed");
			description.addProperty("status");
			description.addProperty("statusReason", Representation.REF);
			description.addProperty("location", Representation.REF);
			description.addProperty("encounter", Representation.REF);
			description.addProperty("dispenser", Representation.REF);
		} else if (representation instanceof DefaultRepresentation) {
			description.addProperty("uuid");
			description.addProperty("patient", Representation.DEFAULT);
			description.addProperty("medicalSupplyOrder", Representation.DEFAULT);
			description.addProperty("concept", Representation.DEFAULT);
			description.addProperty("quantity", Representation.DEFAULT);
			description.addProperty("quantityUnits", Representation.DEFAULT);
			description.addProperty("dateDispensed");
			description.addProperty("status");
			description.addProperty("statusReason", Representation.DEFAULT);
			description.addProperty("location", Representation.DEFAULT);
			description.addProperty("encounter", Representation.DEFAULT);
			description.addProperty("dispenser", Representation.DEFAULT);
		} else if (representation instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("patient", Representation.REF);
			description.addProperty("medicalSupplyOrder", Representation.FULL);
			description.addProperty("concept", Representation.FULL);
			description.addProperty("quantity", Representation.REF);
			description.addProperty("quantityUnits", Representation.FULL);
			description.addProperty("dateDispensed");
			description.addProperty("status");
			description.addProperty("statusReason", Representation.FULL);
			description.addProperty("location", Representation.REF);
			description.addProperty("encounter", Representation.REF);
			description.addProperty("dispenser", Representation.FULL);
		} else if (representation instanceof CustomRepresentation) { // custom rep
			description = null;
		}
		return description;
	}
	
}
