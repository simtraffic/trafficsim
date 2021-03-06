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
package org.tripsim.engine.network;

import java.util.List;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.springframework.stereotype.Component;
import org.tripsim.api.model.Connector;
import org.tripsim.api.model.Lane;
import org.tripsim.api.model.Link;
import org.tripsim.api.model.Network;
import org.tripsim.api.model.Node;
import org.tripsim.api.model.RoadInfo;
import org.tripsim.model.network.DefaultConnector;
import org.tripsim.model.network.DefaultLane;
import org.tripsim.model.network.DefaultLink;
import org.tripsim.model.network.DefaultNetwork;
import org.tripsim.model.network.DefaultNode;
import org.tripsim.model.network.DefaultRoadInfo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * A factory for creating DefaultNetwork objects.
 * 
 * @author Xuan Shi
 */
@Component("default-network-factory")
class DefaultNetworkFactory implements NetworkFactory {

	private GeometryFactory geometryFactory;

	DefaultNetworkFactory() {
		geometryFactory = JTSFactoryFinder.getGeometryFactory();
	}

	@Override
	public Point createPoint(double x, double y) {
		return geometryFactory.createPoint(new Coordinate(x, y));
	}

	@Override
	public Point createPoint(Coordinate coord) {
		return geometryFactory.createPoint(coord);
	}

	@Override
	public LineString createLineString(Coordinate[] coords) {
		return geometryFactory.createLineString(coords);
	}

	@Override
	public LineString createLineString(CoordinateSequence points) {
		return geometryFactory.createLineString(points);
	}

	@Override
	public Network createNetwork(String name) {
		return new DefaultNetwork(name);
	}

	@Override
	public Node createNode(Long id, String nodeType, double x, double y) {
		return createNode(id, nodeType, createPoint(x, y));
	}

	@Override
	public Node createNode(Long id, String nodeType, Coordinate coord) {
		return createNode(id, nodeType, createPoint(coord));
	}

	@Override
	public Node createNode(Long id, String nodeType, Point point) {
		return new DefaultNode(id, nodeType, point);
	}

	@Override
	public Link createLink(Long id, String linkType, Node startNode,
			Node endNode, Coordinate[] coords, RoadInfo roadInfo) {
		return createLink(id, linkType, startNode, endNode,
				createLineString(coords), roadInfo);
	}

	@Override
	public Link createLink(Long id, String linkType, Node startNode,
			Node endNode, CoordinateSequence points, RoadInfo roadInfo) {
		return createLink(id, linkType, startNode, endNode,
				createLineString(points), roadInfo);
	}

	@Override
	public Link createLink(Long id, String linkType, Node startNode,
			Node endNode, LineString lineString, RoadInfo roadInfo) {
		if (roadInfo == null) {
			roadInfo = new DefaultRoadInfo();
		}
		return new DefaultLink(id, linkType, startNode, endNode, lineString,
				roadInfo);
	}

	@Override
	public Link createReverseLink(Long id, Link link) {
		Link newLink = createLink(id, link.getLinkType(), link.getEndNode(),
				link.getStartNode(), (LineString) link.getLinearGeom()
						.reverse(), link.getRoadInfo());
		link.setReverseLink(newLink);
		return newLink;
	}

	@Override
	public Lane createLane(Long id, Link link, double start, double end,
			double width) {
		return new DefaultLane(id, link, start, end, width);
	}

	@Override
	public List<Lane> createLanes(Long[] ids, Link link, double start,
			double end, double width) {
		for (Long id : ids) {
			createLane(id, link, start, end, width);
		}
		return link.getLanes();
	}

	@Override
	public Connector connect(Long id, Lane laneFrom, Lane laneTo) {
		return new DefaultConnector(id, laneFrom, laneTo);
	}

	@Override
	public RoadInfo createRoadInfo(Long id, String roadName) {
		return new DefaultRoadInfo(id, roadName);
	}

	@Override
	public RoadInfo createRoadInfo(Long id, long osmId, String name,
			String highway) {
		return new DefaultRoadInfo(id, name, osmId, highway);
	}

}
