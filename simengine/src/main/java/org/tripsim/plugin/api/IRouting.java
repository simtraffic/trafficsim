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
package org.tripsim.plugin.api;

import org.tripsim.api.model.Link;
import org.tripsim.api.model.Node;
import org.tripsim.api.model.VehicleClass;
import org.tripsim.engine.simulation.SimulationEnvironment;

/**
 * 
 * 
 * @author Xuan Shi
 */
public interface IRouting extends IPlugin {

	Link searchNextLink(SimulationEnvironment environment, Node current,
			Node destination);

	Link searchNextLink(SimulationEnvironment environment, Link currentLink,
			Node destination, VehicleClass vehicleClass);

}
