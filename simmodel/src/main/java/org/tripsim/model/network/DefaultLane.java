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
package org.tripsim.model.network;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.tripsim.api.model.Connector;
import org.tripsim.api.model.Lane;
import org.tripsim.api.model.Link;
import org.tripsim.api.model.Path;
import org.tripsim.model.core.AbstractArcSection;

/**
 * 
 * 
 * @author Xuan Shi
 */
public class DefaultLane extends AbstractArcSection implements Lane {

	private static final long serialVersionUID = 1L;

	private boolean auxiliary;
	private int lanePosition;

	public DefaultLane(long id, Link link, double start, double end,
			double width) {
		super(id, link, start, end, width, 0);
		this.lanePosition = -2;
		link.add(this);
	}

	@Override
	public Link getLink() {
		return (Link) parent;
	}

	@Override
	public boolean isAuxiliary() {
		return auxiliary;
	}

	public void setAuxiliary(boolean auxiliary) {
		this.auxiliary = auxiliary;
	}

	@Override
	public int getLanePosition() {
		return lanePosition;
	}

	@Override
	public void setLanePosition(int lanePosition) {
		this.lanePosition = lanePosition;
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof Lane)) {
			throw new ClassCastException("cannot compare with non Lane Object!");
		}
		Lane lane = (Lane) o;

		if (!getLink().equals(lane.getLink())) {
			return super.compareTo(lane);
		}
		return lanePosition > lane.getLanePosition() ? 1 : (lanePosition > lane
				.getLanePosition() ? -1 : 0);
	}

	@Override
	public Collection<Connector> getOutConnectors() {
		return getLink().getEndNode().getConnectorsFromLane(this);
	}

	@Override
	public Collection<Connector> getInConnectors() {
		return getLink().getStartNode().getConnectorsToLane(this);
	}

	@Override
	protected void onShiftUpdate(double offset) {

	}

	@Override
	protected void onWidthUpdate(double offset) {
		this.setShift(shift + offset / 2, false);
		List<Lane> lanes = getLink().getLanes();
		if (getLink().getReverseLink() == null) {
			for (int i = lanePosition + 1; i < lanes.size(); i++) {
				Lane lane = lanes.get(i);
				lane.setShift(lane.getShift() + offset, false);
			}
		} else {
			for (int i = lanePosition + 1; i < lanes.size(); i++) {
				Lane lane = lanes.get(i);
				lane.setShift(lane.getShift() + offset / 2, false);
			}
		}
	}

	@Override
	public void onGeomUpdated() {
		super.onGeomUpdated();
		for (Connector connector : getOutConnectors()) {
			connector.onGeomUpdated();
		}
		for (Connector connector : getInConnectors()) {
			connector.onGeomUpdated();
		}
	}

	@Override
	public Collection<Lane> getUpstreamLanes() {
		Set<Lane> entrances = new HashSet<Lane>();
		for (Connector connector : getInConnectors()) {
			entrances.add(connector.getFromLane());
		}
		return entrances;
	}

	@Override
	public Collection<Lane> getDownstreamLanes() {
		Set<Lane> exits = new HashSet<Lane>();
		for (Connector connector : getOutConnectors()) {
			exits.add(connector.getToLane());
		}
		return exits;
	}

	@Override
	public Collection<? extends Path> getEntrances() {
		return getUpstreamLanes();
	}

	@Override
	public Collection<? extends Path> getExits() {
		return getDownstreamLanes();
	}

	@Override
	public boolean inLane(double position) {
		return position > startOffset && position < startOffset + length;
	}

}
