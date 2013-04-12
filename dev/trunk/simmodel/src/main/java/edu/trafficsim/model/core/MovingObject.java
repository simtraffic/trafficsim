package edu.trafficsim.model.core;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

public abstract class MovingObject<T> extends BaseEntity<T> implements Movable,
		Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final List<Coordinate> coords = new ArrayList<Coordinate>();

	private final double startTime;

	protected double position = 0;
	protected double lateralOffset = 0;

	protected double speed = 0;
	protected double acceleration = 0;

	protected boolean active = true;

	// public MovingObject() {
	// }

	public MovingObject(double startTime) {
		this.startTime = startTime;
	}

	@Override
	public final double getStartTime() {
		return startTime;
	}

	@Override
	public final double position() {
		return position;
	}

	public final void position(double position) {
		this.position = position;
	}

	@Override
	public final double speed() {
		return speed;
	}

	public final void speed(double speed) {
		this.speed = speed;
	}

	@Override
	public final double acceleration() {
		return acceleration;
	}

	public final void acceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	@Override
	public final Coordinate coord() {
		return coords.size() < 1 ? null : coords.get(coords.size() - 1);
	}

	@Override
	public final Coordinate[] trajectory() {
		return coords.toArray(new Coordinate[0]);
	}

	public void stepForward(double stepSize) {
		before();
		update();
		position = position + stepSize * speed;
		after();
	}

	@Override
	public final boolean isActive() {
		return active;
	}

	protected abstract void before();

	protected abstract void after();

	protected abstract void update();

	public abstract boolean beyondEnd();

	
	@Override
	public void apply(Visitor visitor) {
		visitor.visit(this);
	}
}
