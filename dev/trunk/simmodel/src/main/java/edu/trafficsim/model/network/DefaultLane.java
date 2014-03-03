package edu.trafficsim.model.network;

import java.util.Collection;

import org.opengis.referencing.operation.TransformException;

import edu.trafficsim.model.ConnectionLane;
import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Link;

public class DefaultLane extends AbstractLane<DefaultLane> {

	private static final long serialVersionUID = 1L;

	private int laneId;

	public DefaultLane(long id, Link link, double start, double end,
			double width) throws TransformException {
		super(id, link, start, end, width, 0);
		this.laneId = -2;
		link.add(this);
		onGeomUpdated();
	}

	@Override
	public final String getName() {
		return segment.getName() + " " + laneId;
	}

	@Override
	public int getLaneId() {
		return laneId;
	}

	@Override
	public void setLaneId(int laneId) {
		this.laneId = laneId;
	}

	@Override
	public int compareTo(DefaultLane lane) {
		if (!segment.equals(lane.getSegment()))
			return super.compareTo(lane);
		return laneId > lane.laneId ? 1 : (laneId > lane.laneId ? -1 : 0);
	}

	@Override
	public void onGeomUpdated() throws TransformException {
		super.onGeomUpdated();
		for (ConnectionLane connectionLane : getToConnectors()) {
			connectionLane.onGeomUpdated();
		}
		for (ConnectionLane connectionLane : getFromConnectors()) {
			connectionLane.onGeomUpdated();
		}
	}

	@Override
	public Collection<ConnectionLane> getToConnectors() {
		return getLink().getEndNode().getInConnectors(this);
	}

	@Override
	public Collection<ConnectionLane> getFromConnectors() {
		return getLink().getStartNode().getOutConnectors(this);
	}

	@Override
	protected void onShiftUpdate(double offset) throws TransformException {

	}

	@Override
	protected void onWidthUpdate(double offset) throws TransformException {
		this.setShift(shift + offset / 2, false);
		Lane[] lanes = getLink().getLanes();
		if (getLink().getReverseLink() == null) {
			for (int i = laneId + 1; i < lanes.length; i++) {
				lanes[i].setShift(lanes[i].getShift() + offset, false);
			}
		} else {
			for (int i = laneId + 1; i < lanes.length; i++) {
				lanes[i].setShift(lanes[i].getShift() + offset / 2, false);
			}
		}
	}
}
