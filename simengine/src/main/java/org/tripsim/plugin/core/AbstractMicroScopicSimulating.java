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

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tripsim.api.model.Connector;
import org.tripsim.api.model.Lane;
import org.tripsim.api.model.Link;
import org.tripsim.api.model.Network;
import org.tripsim.api.model.Node;
import org.tripsim.api.model.Od;
import org.tripsim.api.model.OdMatrix;
import org.tripsim.api.model.Vehicle;
import org.tripsim.api.model.VehicleStream;
import org.tripsim.api.model.VehicleWeb;
import org.tripsim.engine.simulation.SimulationEnvironment;
import org.tripsim.model.vehicle.MicroScopicLaneVehicleStream;
import org.tripsim.model.vehicle.SimpleVehicleWeb;
import org.tripsim.plugin.AbstractPlugin;
import org.tripsim.plugin.api.ISimulating;
import org.tripsim.util.Randoms;
import org.tripsim.util.Timer;

abstract class AbstractMicroScopicSimulating extends AbstractPlugin implements
		ISimulating {

	private static final long serialVersionUID = 1L;

	protected static final Logger logger = LoggerFactory
			.getLogger(ISimulating.class);

	@Override
	public final void simulate(Timer timer, SimulationEnvironment environment) {
		beforeSimulate(timer, environment);
		new SimulatingEnvironment(timer, environment).simulateLoop();
		afterSimulate(timer, environment);
	}

	protected class SimulatingEnvironment {

		final SimulationEnvironment environment;
		final Timer timer;
		final SimpleVehicleWeb web = new SimpleVehicleWeb();

		SimulatingEnvironment(Timer timer, SimulationEnvironment environment) {
			this.timer = timer;
			this.environment = environment;
		}

		void simulateLoop() {
			while (!timer.isFinished()) {
				beforeEachStep(environment);
				moveVehicles();
				afterMoveVehicles(environment);
				generateVehicles();
				flush();
				timer.stepForward();
			}
		}

		private void flush() {
			for (VehicleStream stream : web.getStreams()) {
				stream.flush();
			}
		}

		// TODO work on multi-threading
		// use executor for each link or lane or node
		void moveVehicles() {
			Network network = environment.getNetwork();
			for (Node node : network.getSources()) {
				for (Link link : node.getDownstreams()) {
					for (Lane lane : link.getLanes()) {
						moveVehiclesFromSource(lane);
						moveVehiclesDownstreams(lane);
					}
				}
			}
		}

		private void moveVehiclesFromSource(Lane lane) {
			VehicleStream stream = web.getStream(lane);
			if (stream == null) {
				web.putStream(lane, stream = new MicroScopicLaneVehicleStream(
						lane));
			}
			moveVehicles(stream);
		}

		private void moveVehiclesDownstreams(Lane lane) {
			for (Connector connector : lane.getOutConnectors()) {
				Lane nextLane = connector.getToLane();
				VehicleStream stream = web.getStream(nextLane);
				if (stream == null) {
					web.putStream(nextLane,
							stream = new MicroScopicLaneVehicleStream(nextLane));
				}
				moveVehicles(stream);
				moveVehiclesDownstreams(nextLane);
			}
		}

		private void moveVehicles(VehicleStream stream) {
			Collection<Vehicle> vehicles = stream.getVehicles();
			for (Vehicle vehicle : vehicles) {
				moveVehicle(environment, vehicle, stream, web);
				collectStatistics(environment, vehicle);
				if (!vehicle.isActive()) {
					stream.removeInactive(vehicle);
				}
			}
		}

		protected void moveVehicle(SimulationEnvironment environment,
				Vehicle vehicle, VehicleStream stream, VehicleWeb web) {
			try {
				AbstractMicroScopicSimulating.this.moveVehicle(environment,
						vehicle, stream, web);
			} catch (IllegalStateException e) {
				logger.warn(
						"EXCEPTION========Simulation--{}----Time--{}------{}",
						environment.getSimulationName(),
						environment.getForwardedTime(), e.getMessage());
				vehicle.deactivate();
			}
		}

		protected void generateVehicles() {
			OdMatrix odMatrix = environment.getOdMatrix();
			for (Od od : odMatrix.getOds()) {
				List<Vehicle> newVehicles = environment.generateVehicles(od);
				addTostream(newVehicles);
				afterGenerateVehicles(environment, od, newVehicles);
			}
		}

		private void addTostream(List<Vehicle> newVehicles) {
			for (Vehicle newVehicle : newVehicles) {
				Link link = environment.getNextLinkInRoute(newVehicle);
				newVehicle.goToNextLinkAndSetNew(link);
				Lane lane = Randoms.randomElement(link.getMainLanes(),
						environment.getRandom());
				link = environment.getNextLinkInRoute(newVehicle);
				// move on to the link, and set the target exit link
				newVehicle.goToNextLinkAndSetNew(link);
				VehicleStream stream = web.getStream(lane);
				stream.moveIn(newVehicle, null);
				collectStatistics(environment, newVehicle);
			}
		}
	}

	protected void beforeSimulate(Timer timer, SimulationEnvironment environment) {
	}

	protected void beforeEachStep(SimulationEnvironment environment) {
	}

	protected abstract void moveVehicle(SimulationEnvironment environment,
			Vehicle vehicle, VehicleStream stream, VehicleWeb web);

	protected abstract void collectStatistics(
			SimulationEnvironment environment, Vehicle vehicle);

	protected void afterMoveVehicles(SimulationEnvironment environment) {
	}

	protected void afterGenerateVehicles(SimulationEnvironment environment,
			Od od, List<Vehicle> newVehicles) {
	}

	protected void afterSimulate(Timer timer, SimulationEnvironment environment) {
	}

}