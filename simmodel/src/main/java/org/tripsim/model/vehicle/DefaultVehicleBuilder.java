/*
 * Copyright (c) 2015 Xuan Shi
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.a
 * 
 * @author Xuan Shi
 */
package org.tripsim.model.vehicle;

import org.tripsim.api.model.Node;
import org.tripsim.api.model.VehicleClass;
import org.tripsim.model.core.CrusingType;

public final class DefaultVehicleBuilder {

	final long id;
	final VehicleClass vehicleClass;
	CrusingType crusingType = CrusingType.NONE;
	final String vehicleType;
	final String driverType;

	// Vehicle Type Properties
	long startFrame = 0;
	double width = 2.5;
	double length = 4.5;

	Node origin = null;
	Node destination = null;

	// Driver Type Properties
	// in seconds
	double perceptionTime = 2;
	double reactionTime = 2;
	double desiredHeadway = 2;
	double desiredRearway = 2;

	double desiredSpeed = 25;
	double maxSpeed = 40;

	double lookAheadDistance = 50;
	double lookBehindDistance = 20;

	public DefaultVehicleBuilder(long id, VehicleClass vehicleClass,
			String vehicleType, String driverType) {
		this.id = id;
		this.vehicleClass = vehicleClass;
		this.vehicleType = vehicleType;
		this.driverType = driverType;
	}

	public DefaultVehicleBuilder withCrusingType(CrusingType crusingType) {
		this.crusingType = crusingType;
		return this;
	}

	public DefaultVehicleBuilder withStartFrame(long startFrame) {
		this.startFrame = startFrame;
		return this;
	}

	public DefaultVehicleBuilder withWidth(double width) {
		this.width = width;
		return this;
	}

	public DefaultVehicleBuilder withLength(double length) {
		this.length = length;
		return this;
	}

	public DefaultVehicleBuilder withPerceptionTime(double perceptionTime) {
		this.perceptionTime = perceptionTime;
		return this;
	}

	public DefaultVehicleBuilder withReactionTime(double reactionTime) {
		this.reactionTime = reactionTime;
		return this;
	}

	public DefaultVehicleBuilder withDesiredHeadway(double headway) {
		this.desiredHeadway = headway;
		return this;
	}

	public DefaultVehicleBuilder withDesiredRearway(double rearway) {
		this.desiredHeadway = rearway;
		return this;
	}

	public DefaultVehicleBuilder withDesiredSpeed(double speed) {
		this.desiredSpeed = speed;
		return this;
	}

	public DefaultVehicleBuilder withMaxSpeed(double speed) {
		this.maxSpeed = speed;
		return this;
	}

	public DefaultVehicleBuilder withLookAheadDistance(double distance) {
		this.lookAheadDistance = distance;
		return this;
	}

	public DefaultVehicleBuilder withLookBehindDistance(double distance) {
		this.lookBehindDistance = distance;
		return this;
	}

	public DefaultVehicleBuilder withOrigin(Node origin) {
		this.origin = origin;
		return this;
	}

	public DefaultVehicleBuilder withDestination(Node destination) {
		this.destination = destination;
		return this;
	}

	public DefaultVehicle build() {
		DefaultVehicle vehicle = new DefaultVehicle(id, startFrame,
				vehicleClass, driverType, driverType);
		vehicle.setWidth(width);
		vehicle.setLength(length);
		vehicle.setMaxSpeed(maxSpeed);
		vehicle.setCrusingType(crusingType);
		vehicle.setDesiredSpeed(desiredSpeed);
		vehicle.setDesiredHeadway(desiredHeadway);
		vehicle.setDesiredRearway(desiredRearway);
		vehicle.setPerceptionTime(perceptionTime);
		vehicle.setReactionTime(reactionTime);
		vehicle.setLookAheadDistance(lookAheadDistance);
		vehicle.setLookBehindDistance(lookBehindDistance);
		vehicle.setOrigin(origin);
		vehicle.setDestination(destination);
		return vehicle;
	}
}
