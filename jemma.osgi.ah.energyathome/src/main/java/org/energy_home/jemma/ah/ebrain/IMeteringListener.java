/**
 * This file is part of JEMMA - http://jemma.energy-home.org
 * (C) Copyright 2010 Telecom Italia (http://www.telecomitalia.it)
 *
 * JEMMA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) version 3
 * or later as published by the Free Software Foundation, which accompanies
 * this distribution and is available at http://www.gnu.org/licenses/lgpl.html
 *
 * JEMMA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License (LGPL) for more details.
 *
 */
package org.energy_home.jemma.ah.ebrain;

import org.energy_home.jemma.ah.ebrain.algo.DailyTariff;
import org.energy_home.jemma.ah.hap.client.M2MHapException;

public interface IMeteringListener {
	Class<? extends DailyTariff> getDailyTariff();
	void setDailyTariff(Class<? extends DailyTariff> clazz) throws InstantiationException, IllegalAccessException;
	
	float getUpperPowerThreshold();
	void setUpperPowerThreshold(float upper);
	
	
	void notifyIstantaneousDemandPower(String applianceId, long time, float power) throws M2MHapException;
	void notifyCurrentSummationDelivered(String applianceId, long time, double totalEnergy) throws M2MHapException;
	void notifyCurrentSummationReceived(String applianceId, long time, double totalEnergy) throws M2MHapException;
	
	//EnergyCostInfo calculateEnergyCost(String applianceId);
	//MinMaxPowerInfo getInstantaneosPowerInfo(String applianceId);
	
	float getIstantaneousDemandPower(String applianceId);
	double getCurrentSummationDelivered(String applianceId);
	double getCurrentSummationReceived(String applianceId);
	EnergyCostInfo getAccumulatedEnergyCost(String applianceId);
	//double getApplianceAccumulatedEnergy(String applianceId);
	//long getApplianceAccumulatedEnergyTime(String applianceId);
}