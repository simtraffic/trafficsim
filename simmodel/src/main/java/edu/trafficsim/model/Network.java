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
package edu.trafficsim.model;

import java.util.Collection;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;

import edu.trafficsim.model.core.ModelInputException;

/**
 * 
 * 
 * @author Xuan Shi
 */
public interface Network extends ObjectContainer, Persistable, GeoReferenced {

	Collection<Node> getSources();

	Collection<Node> getSinks();

	Collection<Node> getNodes();

	Collection<Link> getLinks();

	Set<Long> getNodeIds();

	Set<Long> getLinkIds();

	Node getNode(long id);

	Link getLink(long id);

	boolean contains(Node node);

	boolean contains(Link link);

	boolean containsNode(long id);

	boolean containsLink(long id);

	void add(Link link) throws ModelInputException;

	void add(Link... links) throws ModelInputException;

	Link removeLink(long linkId) throws ModelInputException;

	Coordinate center();

	// TODO implement as needed

	// Set<Route> getRoutes(Node start, Node end);
	//
	// static Set<Route> getRoutes(Node start, Node end);
	//
	// static Set<Link> getDownstreamLinks(Node node);
	//
	// static Set<Link> getDownstreamLinks(Link link);
	//
	// static Set<Node> getDownstreamNodes(Node node);
	//
	// static Set<Node> getDownstreamNodes(Link link);
	//
	// static Set<Link> getUpstreamLinks(Node node);
	//
	// static Set<Link> getUpstreamLinks(Link link);
	//
	// static Set<Node> getUpstreamNodes(Node node);
	//
	// static Set<Node> getUpstreamNodes(Link link);
	//
	// static Network getDownstreamSubnetwork(Node node);
	//
	// static Network getDownstreamSubnetwork(Link link);
	//
	// static Network getUpstreamSubnetwork(Node node);
	//
	// static Network getUpstreamSubnetwork(Link link);
}
