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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vividsolutions.jts.geom.Point;

import edu.trafficsim.api.model.Connector;
import edu.trafficsim.api.model.Lane;
import edu.trafficsim.api.model.Link;
import edu.trafficsim.api.model.Node;
import edu.trafficsim.model.core.AbstractLocation;
import edu.trafficsim.util.MultiValuedMap;

/**
 * 
 * 
 * @author Xuan Shi
 * @param <T>
 *            the generic type
 */
public abstract class AbstractNode extends AbstractLocation implements Node {

	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_INITIAL_CONNECTOR_MAP_CAPACITY = 4;

	private final Set<Link> downstreams = new HashSet<Link>();
	private final Set<Link> upstreams = new HashSet<Link>();

	// TODO may not need to maps
	private final MultiValuedMap<Lane, Connector> inConnectors = new MultiValuedMap<Lane, Connector>(
			DEFAULT_INITIAL_CONNECTOR_MAP_CAPACITY);
	private final MultiValuedMap<Lane, Connector> outConnectors = new MultiValuedMap<Lane, Connector>(
			DEFAULT_INITIAL_CONNECTOR_MAP_CAPACITY);

	public AbstractNode(long id, Point point, double radius) {
		super(id, point, radius);
	}

	@Override
	public final void add(Link link) {
		if (link.getEndNode() == this)
			upstreams.add(link);
		else if (link.getStartNode() == this)
			downstreams.add(link);
		else
			throw new IllegalStateException(
					"The link doesn't start or end from the node.");
	}

	@Override
	public final void removeUpstream(Link link) {
		upstreams.remove(link);
		for (Connector connector : getConnectorsFromLink(link)) {
			remove(connector);
		}
	}

	@Override
	public final void removeDownstream(Link link) {
		downstreams.remove(link);
		for (Connector connector : getConnectorsToLink(link)) {
			remove(connector);
		}
	}

	@Override
	public final boolean isUpstream(Link link) {
		return upstreams.contains(link);
	}

	@Override
	public final boolean isDownstream(Link link) {
		return downstreams.contains(link);
	}

	@Override
	public Link getLinkFromNode(Node node) {
		for (Link link : upstreams)
			if (link.getStartNode() == node)
				return link;
		return null;
	}

	@Override
	public Link getLinkToNode(Node node) {
		for (Link link : downstreams)
			if (link.getEndNode() == node)
				return link;
		return null;
	}

	@Override
	public final Collection<Link> getUpstreams() {
		return Collections.unmodifiableCollection(upstreams);
	}

	@Override
	public final Collection<Link> getDownstreams() {
		return Collections.unmodifiableCollection(downstreams);
	}

	@Override
	public Collection<Connector> getConnectors() {
		List<Connector> allConnectors = new ArrayList<Connector>();
		allConnectors.addAll(inConnectors.values());
		allConnectors.addAll(outConnectors.values());
		return Collections.unmodifiableCollection(allConnectors);
	}

	@Override
	public Collection<Connector> getConnectorsFromLane(Lane fromLane) {
		return inConnectors.get(fromLane);
	}

	@Override
	public Collection<Connector> getConnectorsToLane(Lane toLane) {
		return outConnectors.get(toLane);
	}

	@Override
	public Collection<Connector> getConnectorsFromLink(Link fromLink) {
		List<Connector> allConnectors = new ArrayList<Connector>();
		for (Lane lane : fromLink.getLanes()) {
			allConnectors.addAll(inConnectors.get(lane));
		}
		return allConnectors;
	}

	@Override
	public Collection<Connector> getConnectorsToLink(Link toLink) {
		List<Connector> allConnectors = new ArrayList<Connector>();
		for (Lane lane : toLink.getLanes()) {
			allConnectors.addAll(outConnectors.get(lane));
		}
		return allConnectors;
	}

	@Override
	public Connector getConnector(Lane fromLane, Lane toLane) {
		for (Connector connector : inConnectors.get(fromLane)) {
			if (connector.getToLane() == toLane)
				return connector;
		}
		return null;
	}

	@Override
	public Collection<Connector> getConnectors(Lane fromLane, Link toLink) {
		List<Connector> newConnectors = new ArrayList<Connector>();
		for (Connector connector : inConnectors.get(fromLane)) {
			if (connector.getToLane().getLink() == toLink)
				newConnectors.add(connector);
		}
		return Collections.unmodifiableCollection(newConnectors);
	}

	@Override
	public void add(Connector connector) {
		inConnectors.add(connector.getFromLane(), connector);
		outConnectors.add(connector.getToLane(), connector);
	}

	@Override
	public void remove(Connector connector) {
		inConnectors.remove(connector.getFromLane(), connector);
		outConnectors.remove(connector.getToLane(), connector);
	}

	@Override
	public boolean isConnected(Lane fromLane, Lane toLane) {
		return getConnector(fromLane, toLane) != null;
	}

	@Override
	public boolean isEmpty() {
		return upstreams.isEmpty() ? downstreams.isEmpty() ? true : false
				: false;
	}

	@Override
	public boolean isSource() {
		return upstreams.isEmpty() || isEndPoint();
	}

	@Override
	public boolean isSink() {
		return downstreams.isEmpty() || isEndPoint();
	}

	private boolean isEndPoint() {
		return getUpstreams().size() == 1
				&& getDownstreams().size() == 1
				&& getUpstreams().iterator().next() == getDownstreams()
						.iterator().next().getReverseLink();
	}

	@Override
	public void onLaneAdded(Lane lane) {

	}

	@Override
	public void onLaneRemoved(int lanePosition, Lane lane) {
		inConnectors.remove(lane);
		outConnectors.remove(lane);
	}
}