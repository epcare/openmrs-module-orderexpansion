/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orderexpansion.api.model;

import org.openmrs.Concept;
import org.openmrs.OrderFrequency;
import org.openmrs.ServiceOrder;

public class ProcedureOrder extends ServiceOrder {
	
	public static final long serialVersionUID = 1L;
	
	private Concept specimenType;
	
	private Concept bodySite;
	
	private Concept specimenSource;
	
	private OrderFrequency frequency;
	
	private Concept location;
	
	private ServiceOrder.Laterality laterality;
	
	private String clinicalHistory;
	
	private Integer numberOfRepeats;
	
	private ProcedureOrder relatedProcedure;
	
	public ProcedureOrder() {
	}
	
	@Override
	public ProcedureOrder copy() {
		ProcedureOrder newOrder = new ProcedureOrder();
		super.copyHelper(newOrder);
		return newOrder;
	}
	
	/**
	 * Creates a discontinuation order for this.
	 *
	 * @return the newly created order
	 * @see org.openmrs.ServiceOrder#cloneForDiscontinuing()
	 */
	@Override
	public ProcedureOrder cloneForDiscontinuing() {
		ProcedureOrder newOrder = new ProcedureOrder();
		super.cloneForDiscontinuingHelper(newOrder);
		return newOrder;
	}
	
	/**
	 * Creates a ReferralOrder for revision from this order, sets the previousOrder, action field and
	 * other test order fields.
	 *
	 * @return the newly created order
	 */
	@Override
	public ProcedureOrder cloneForRevision() {
		ProcedureOrder newOrder = new ProcedureOrder();
		super.cloneForRevisionHelper(newOrder);
		return newOrder;
	}
	
	public Concept getSpecimenType() {
		return specimenType;
	}
	
	public void setSpecimenType(Concept specimenType) {
		this.specimenType = specimenType;
	}
	
	public Concept getBodySite() {
		return bodySite;
	}
	
	public void setBodySite(Concept bodySite) {
		this.bodySite = bodySite;
	}
	
	public Concept getSpecimenSource() {
		return specimenSource;
	}
	
	public void setSpecimenSource(Concept specimenSource) {
		this.specimenSource = specimenSource;
	}
	
	public OrderFrequency getFrequency() {
		return frequency;
	}
	
	public void setFrequency(OrderFrequency frequency) {
		this.frequency = frequency;
	}
	
	public Concept getLocation() {
		return location;
	}
	
	public void setLocation(Concept location) {
		this.location = location;
	}
	
	public ServiceOrder.Laterality getLaterality() {
		return laterality;
	}
	
	public void setLaterality(ServiceOrder.Laterality laterality) {
		this.laterality = laterality;
	}
	
	public String getClinicalHistory() {
		return clinicalHistory;
	}
	
	public void setClinicalHistory(String clinicalHistory) {
		this.clinicalHistory = clinicalHistory;
	}
	
	public Integer getNumberOfRepeats() {
		return numberOfRepeats;
	}
	
	public void setNumberOfRepeats(Integer numberOfRepeats) {
		this.numberOfRepeats = numberOfRepeats;
	}
	
	public ProcedureOrder getRelatedProcedure() {
		return relatedProcedure;
	}
	
	public void setRelatedProcedure(ProcedureOrder relatedProcedure) {
		this.relatedProcedure = relatedProcedure;
	}
}
