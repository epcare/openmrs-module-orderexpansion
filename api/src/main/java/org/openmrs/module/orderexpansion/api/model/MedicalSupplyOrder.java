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
import org.openmrs.ServiceOrder;

public class MedicalSupplyOrder extends ServiceOrder {
	
	public static final long serialVersionUID = 1L;
	
	private Double quantity;
	
	private String brandName;
	
	private Concept quantityUnits;
	
	private Integer medicalSuppliesInventoryId;
	
	public MedicalSupplyOrder() {
	}
	
	@Override
	public MedicalSupplyOrder copy() {
		MedicalSupplyOrder newOrder = new MedicalSupplyOrder();
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
	public MedicalSupplyOrder cloneForDiscontinuing() {
		MedicalSupplyOrder newOrder = new MedicalSupplyOrder();
		super.cloneForDiscontinuingHelper(newOrder);
		return newOrder;
	}
	
	/**
	 * Creates a revision from this order, sets the previousOrder, action field and other test order
	 * fields.
	 * 
	 * @return the newly created order
	 */
	@Override
	public MedicalSupplyOrder cloneForRevision() {
		MedicalSupplyOrder newOrder = new MedicalSupplyOrder();
		super.cloneForRevisionHelper(newOrder);
		return newOrder;
	}
	
	public Double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public Concept getQuantityUnits() {
		return quantityUnits;
	}
	
	public void setQuantityUnits(Concept quantityUnits) {
		this.quantityUnits = quantityUnits;
	}
	
	public void setMedicalSuppliesInventoryId(Integer medicalSuppliesInventoryId) {
		this.medicalSuppliesInventoryId = medicalSuppliesInventoryId;
	}
	
	public Integer getMedicalSuppliesInventoryId() {
		return medicalSuppliesInventoryId;
	}
	
}
