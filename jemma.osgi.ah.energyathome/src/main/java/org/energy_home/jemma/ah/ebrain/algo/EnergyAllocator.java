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
package org.energy_home.jemma.ah.ebrain.algo;


import java.util.*;

import javax.crypto.SealedObject;

import org.energy_home.jemma.ah.ebrain.CalendarUtil;

public final class EnergyAllocator {
	public static float[] newEnergyAllocation() {
		return newEnergyAllocation(3);
	}
	public static float[] newEnergyAllocation(int numDays) {
		return new float[numDays * CalendarUtil.SLOTS_IN_ONE_DAY];
	}
	
	private float[] energyForecast = newEnergyAllocation();
	private float[] energyRealAllocation = newEnergyAllocation();
	private float[] energyAllocation = newEnergyAllocation(); // three days are necessary in scheduling to avoid the chance of overflow
	private float powerThreshold;
	private float oneSlotEnergyThreshold;
	private DailyTariff dailyTariff;
	private Calendar calendar;

	
	public float[] getEnergyForecast() {
		return energyForecast;
	}

	public void setEnergyForecast(float[] ef) {
		energyForecast = ef;
	}

	public float getPowerThreshold() {
		return powerThreshold;
	}

	public void setPowerThreshold(float pt) {
		powerThreshold = pt;
		oneSlotEnergyThreshold = pt * CalendarUtil.HOURS_IN_ONE_SLOT;
	}

	public DailyTariff getDailyTariff() {
		return dailyTariff;
	}

	public void setDailyTariff(DailyTariff dt) {
		dailyTariff = dt;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar c) {
		calendar = c;
	}
	
	public EnergyAllocator() throws InstantiationException, IllegalAccessException {
		this(Calendar.getInstance(), DailyTariff.getInstance());
	}
	public EnergyAllocator(DailyTariff dt) throws InstantiationException, IllegalAccessException {
		this(Calendar.getInstance(), dt);
	}
	public EnergyAllocator(Calendar c) throws InstantiationException, IllegalAccessException {
		this(c, DailyTariff.getInstance());
	}
	public EnergyAllocator(Calendar c, DailyTariff dt) {
		calendar = c;
		dailyTariff = dt;
		setPowerThreshold(2100);
	}

	float computeOverload(ProfileScheduleParticle swarm) {
		// compute the overload amount (if any)
		clearEnergyAllocation();
		swarm.allocateBiasedPeakEnergy(energyAllocation);
		float overload = 0;
		for (int i = energyAllocation.length; --i >=0;) {
			if (energyAllocation[i] > oneSlotEnergyThreshold - energyForecast[i])
				overload += energyAllocation[i] - oneSlotEnergyThreshold + energyForecast[i];
		}
		return overload;
	}

	float computeEnergyCost(ProfileScheduleParticle particle) {
		clearEnergyAllocation();
		particle.allocateMeanEnergy(energyAllocation);
		return dailyTariff.computeCost(calendar, energyAllocation);
	}

	private void clearEnergyAllocation() {
		for (int i = energyAllocation.length; --i >=0; energyAllocation[i] = 0);
	}
	
	public void interpolateEnergyForecast(double[] forecast, int offset, int slotSize) {
		if (CalendarUtil.SLOTS_IN_ONE_DAY % slotSize != 0)
			throw new IllegalArgumentException("Inconsistent interpolation: incongruent daily time slots.");
		
		int steps = CalendarUtil.SLOTS_IN_ONE_DAY / slotSize;
		for (int i = offset; i < forecast.length -1; ++i) {
			double delta = (forecast[i+1] - forecast[i]) / steps;
			for (int j = 0; j < steps; ++j) {
				float val = (float)(forecast[i] + (j * delta));
				energyForecast[j + (i-offset)*steps] = val * 0.8f; // biased conservative allocation;
			}
		}
		for (int i = 0; i < energyForecast.length; ++i) {
			System.out.println(i + " " + energyForecast[i]);
		}
	}
}
