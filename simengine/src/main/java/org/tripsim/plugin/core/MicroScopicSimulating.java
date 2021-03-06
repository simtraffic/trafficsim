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
package org.tripsim.plugin.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tripsim.api.model.Od;
import org.tripsim.api.model.Vehicle;
import org.tripsim.api.model.VehicleStream;
import org.tripsim.api.model.VehicleWeb;
import org.tripsim.engine.simulation.SimulationEnvironment;
import org.tripsim.engine.statistics.StatisticsCollector;
import org.tripsim.engine.vehicle.VehicleFactory;
import org.tripsim.plugin.api.ISimulating;
import org.tripsim.util.Timer;

/**
 * 
 * 
 * @author Xuan Shi
 */
@Component("Micro Scopic Simulating")
class MicroScopicSimulating extends AbstractMicroScopicSimulating implements
		ISimulating {

	private static final long serialVersionUID = 1L;

	@Autowired
	VehicleFactory vehicleFactory;
	@Autowired
	StatisticsCollector statisticsCollector;

	@Override
	protected void beforeSimulate(Timer timer, SimulationEnvironment environ) {
		String name = environ.getSimulationName();
		logger.info("Simulation--{}--------Micro Scopic Simulation--------",
				name);
		logger.info("Simulation--{}--------Random Seed: {} ", name,
				environ.getSeed());
		logger.info("Simulation--{}--------Step Size: {}", name,
				environ.getStepSize());
		logger.info("Simulation--{}--------Duration: {}", name,
				environ.getDuration());
		logger.info("Simulation--{}--------Start Simulation--------", name);
	}

	@Override
	protected void beforeEachStep(SimulationEnvironment environment) {
		logger.debug("Simulation--{}----Time--{}----start step forward!",
				environment.getSimulationName(), environment.getForwardedTime());
	}

	@Override
	protected void moveVehicle(SimulationEnvironment environment,
			Vehicle vehicle, VehicleStream stream, VehicleWeb web) {
		stream = environment.applyLaneChanging(vehicle, stream, web);
		environment.applyCarFollowing(vehicle, stream, web);
		environment.applyMoving(vehicle, stream, web);
	}

	@Override
	protected void collectStatistics(SimulationEnvironment environment,
			Vehicle vehicle) {
		logger.debug("Simulation--{}----Time--{}----{}",
				environment.getSimulationName(),
				environment.getForwardedTime(), vehicle);
		statisticsCollector.visit(environment, vehicle);
	}

	@Override
	protected void afterGenerateVehicles(SimulationEnvironment environment,
			Od od, List<Vehicle> newVehicles) {
		logger.debug(
				"Simulation--{}----Time--{}----{} new vehicles generated at node {} to node {}",
				environment.getSimulationName(),
				environment.getForwardedTime(), newVehicles.size(),
				od.getOriginNodeId(), od.getDestinationNodeId());
	}

	@Override
	protected void afterSimulate(Timer timer, SimulationEnvironment environment) {
		logger.info(
				"Simulation--{}--------Finished Micro Scopic Simulation--------",
				environment.getSimulationName());
	}

	@Override
	public String getName() {
		return "Microscopic Simulating";
	}

}
