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
package org.tripsim.engine.type;

import org.tripsim.api.model.VehicleClass;
import org.tripsim.model.core.CrusingType;

/**
 * 
 * 
 * @author Xuan Shi
 */
public class VehicleType extends Type {

	double width = 2.5;
	double length = 4.5;

	// TODO make it a function
	double maxAccel = 3.5;
	double maxDecel = -7.5;
	double maxSpeed = 40;
	// private double minHeight;
	// private double maxHeight;
	//
	// private double frictions;
	// private double horsePower;
	// private double mpg;
	//
	// private double emission;

	private VehicleClass vehicleClass = VehicleClass.Car;
	private CrusingType crusingType = CrusingType.NONE;

	VehicleType(String name) {
		super(name);
	}

	public VehicleClass getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(VehicleClass vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

	public CrusingType getCrusingType() {
		return crusingType;
	}

	public void setCrusingType(CrusingType crusingType) {
		this.crusingType = crusingType;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public double getMaxAccel() {
		return maxAccel;
	}

	public void setMaxAccel(double maxAccel) {
		this.maxAccel = maxAccel;
	}

	public double getMaxDecel() {
		return maxDecel;
	}

	public void setMaxDecel(double maxDecel) {
		this.maxDecel = maxDecel;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

}
