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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tripsim.api.model.Path;
import org.tripsim.api.model.Vehicle;
import org.tripsim.api.model.VehicleStream;
import org.tripsim.api.model.VehicleWeb;
import org.tripsim.engine.simulation.SimulationEnvironment;
import org.tripsim.plugin.AbstractPlugin;
import org.tripsim.plugin.api.ILaneChanging;
import org.tripsim.util.Pair;

/**
 * 
 * 
 * @author Xuan Shi
 */
@Component("Simple Lane-changing")
abstract class AbstractLaneChanging extends AbstractPlugin implements
		ILaneChanging {
	private static final long serialVersionUID = 1L;

	protected static final Logger logger = LoggerFactory
			.getLogger(ILaneChanging.class);

	@Override
	public String getName() {
		return "Default Lane Changing";
	}

	@Override
	public final VehicleStream update(SimulationEnvironment environment,
			Vehicle vehicle, VehicleStream stream, VehicleWeb web) {
		LaneChangingEnvironment laneChangingEnvironment = new LaneChangingEnvironment(
				environment, vehicle, stream, web);
		return laneChangingEnvironment.update();
	}

	protected class LaneChangingEnvironment extends AbstractVehicleEnvironment {
		private final VehicleStream leftStream;
		private final VehicleStream rightStream;

		LaneChangingEnvironment(SimulationEnvironment environment,
				Vehicle vehicle, VehicleStream stream, VehicleWeb web) {
			super(environment, vehicle, stream, web);
			leftStream = web.getStream(stream.getMergeLeftPath(vehicle));
			rightStream = web.getStream(stream.getMergeRightPath(vehicle));
		}

		public VehicleStream update() {
			if (!vehicle.isActive()) {
				return stream;
			}

			int changeDirection = getChangeDirection();
			if (changeDirection < 0) {
				return merge(leftStream);
			}
			if (changeDirection > 0) {
				return merge(rightStream);
			}
			return stream;
		}

		private int getChangeDirection() {
			boolean currentViable = stream.isViable(vehicle);
			double frontGap = getGap(stream, true);
			double rearGap = getGap(stream, false);
			boolean leftViable = isViable(stream.getLeftDestPaths());
			double leftFrontGap = getGap(leftStream, true);
			double leftRearGap = getGap(leftStream, false);
			boolean rightViable = isViable(stream.getRightDestPaths());
			double rightFrontGap = getGap(rightStream, true);
			double rightRearGap = getGap(rightStream, false);

			double desiredFrontGap = vehicle.getDesiredHeadway()
					* vehicle.getSpeed();
			double desiredRearGap = vehicle.getDesiredRearway()
					* vehicle.getSpeed();

			return currentViable ? AbstractLaneChanging.this
					.calDirectionOnNoChangeNeeded(desiredFrontGap,
							desiredRearGap, frontGap, rearGap, leftViable,
							leftFrontGap, leftRearGap, rightViable,
							rightFrontGap, rightRearGap)
					: AbstractLaneChanging.this.calDirectionOnChangeNeeded(
							desiredFrontGap, desiredRearGap, desiredFrontGap,
							desiredRearGap, leftViable, leftFrontGap,
							leftRearGap, rightViable, rightFrontGap,
							rightRearGap);
		}

		private double getGap(VehicleStream stream, boolean ahead) {
			if (stream == null) {
				return 0;
			}
			double maxSpacing = ahead ? vehicle.getLookAheadDistance()
					: vehicle.getLookBehindDistance();
			Pair<Double, Vehicle> pair = calculateSpacing(stream.getPath(),
					maxSpacing, ahead);
			return pair.primary() - (ahead ? 0 : vehicle.getLength());
		}

		private boolean isViable(Collection<Path> paths) {
			for (Path parallelPath : paths) {
				VehicleStream parallelStream = web.getStream(parallelPath);
				if (parallelStream != null
						&& parallelStream.isViableNext(vehicle)) {
					return true;
				}
			}
			return false;
		}

		private VehicleStream merge(VehicleStream toStream) {
			if (toStream != null && toStream.mergeIn(vehicle)) {
				logger.debug(
						"Simulation--{}----Time--{}----{} moved to {} from {}",
						environment.getSimulationName(),
						environment.getForwardedTime(), vehicle, toStream,
						stream);
				stream.moveOrMergeOut(vehicle);
				return toStream;
			}
			return stream;
		}

	}

	/**
	 * @return 0 for not moving, positive for moving right, and negative for
	 *         moving left
	 */
	abstract protected int calDirectionOnChangeNeeded(double desiredFrontGap,
			double desiredRearGap, double frontGap, double rearGap,
			boolean leftViable, double leftFrontGap, double leftRearGap,
			boolean rightViable, double rightFrontGap, double rightRearGap);

	/**
	 * @return 0 for not moving, positive for moving right, and negative for
	 *         moving left
	 */
	abstract protected int calDirectionOnNoChangeNeeded(
			double desiredFrontGap, double desiredRearGap, double frontGap,
			double rearGap, boolean leftViable, double leftFrontGap,
			double leftRearGap, boolean rightViable, double rightFrontGap,
			double rightRearGap);

}
