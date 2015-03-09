/*
 * Copyright (C) 2014 Xuan Shi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.trafficsim.model.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.vividsolutions.jts.geom.LineString;

import edu.trafficsim.api.model.Connector;
import edu.trafficsim.api.model.Lane;
import edu.trafficsim.api.model.Link;
import edu.trafficsim.api.model.Node;
import edu.trafficsim.api.model.Path;
import edu.trafficsim.api.model.RoadInfo;

/**
 * 
 * 
 * @author Xuan Shi
 */
public class DefaultLink extends AbstractLink {

	private static final long serialVersionUID = 1L;

	private String linkType;
	private RoadInfo roadInfo;

	public DefaultLink(long id, String linkType, Node startNode, Node endNode,
			LineString linearGeom, RoadInfo roadInfo) {
		super(id, startNode, endNode, linearGeom);
		this.linkType = linkType;
		this.roadInfo = roadInfo;
	}

	@Override
	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	@Override
	public RoadInfo getRoadInfo() {
		return roadInfo;
	}

	@Override
	public void setRoadInfo(RoadInfo roadInfo) {
		this.roadInfo = roadInfo;
	}

	@Override
	public Collection<Connector> getConnectors(Link destLink) {
		List<Connector> connectors = new ArrayList<Connector>();
		for (Lane fromLane : getLanes()) {
			connectors.addAll(getEndNode().getConnectors(fromLane, destLink));
		}
		return Collections.unmodifiableCollection(connectors);
	}

	@Override
	public Collection<Connector> getOutConnectors() {
		return getEndNode().getConnectorsFromLink(this);
	}

	@Override
	public Collection<Connector> getInConnectors() {
		return getStartNode().getConnectorsToLink(this);
	}

	@Override
	public List<Lane> getAuxiliaryLanes() {
		return getLanes(true);
	}

	@Override
	public List<Lane> getMainLanes() {
		return getLanes(false);
	}

	protected List<Lane> getLanes(boolean auxiliary) {
		List<Lane> list = new ArrayList<Lane>();
		for (Lane lane : getLanes()) {
			if (lane.isAuxiliary() == auxiliary) {
				list.add(lane);
			}
		}
		return list;
	}

	// --------------------------------------------------
	// Path Implementation
	// --------------------------------------------------

	@Override
	public Collection<? extends Path> getExits() {
		return Collections
				.unmodifiableCollection(getEndNode().getDownstreams());
	}

}