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
package org.energy_home.jemma.ah.cluster.zigbee.general;

import org.energy_home.jemma.ah.hac.ApplianceException;
import org.energy_home.jemma.ah.hac.IEndPointRequestContext;
import org.energy_home.jemma.ah.hac.ServiceClusterException;

public interface AlarmsServer {

	final static String ATTR_AlarmCount_NAME = "AlarmCount";
	final static String CMD_ResetAlarm_NAME = "ResetAlarm";
	final static String CMD_ResetAllAlarms_NAME = "ResetAllAlarms";
	final static String CMD_GetAlarm_NAME = "GetAlarm";
	final static String CMD_ResetAlarmLog_NAME = "ResetAlarmLog";

	public int getAlarmCount(IEndPointRequestContext context) throws ApplianceException, ServiceClusterException;

	public void execResetAlarm(short AlarmCode, int ClusterIdentifier, IEndPointRequestContext context) throws ApplianceException,
			ServiceClusterException;

	public void execResetAllAlarms(IEndPointRequestContext context) throws ApplianceException, ServiceClusterException;

	public GetAlarmResponse execGetAlarm(IEndPointRequestContext context) throws ApplianceException, ServiceClusterException;

	public void execResetAlarmLog(IEndPointRequestContext context) throws ApplianceException, ServiceClusterException;

}
