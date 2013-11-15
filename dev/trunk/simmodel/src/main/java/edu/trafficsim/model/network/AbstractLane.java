package edu.trafficsim.model.network;

import java.util.Collection;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.opengis.referencing.operation.TransformException;

import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Segment;
import edu.trafficsim.model.Vehicle;
import edu.trafficsim.model.core.AbstractSubsegment;
import edu.trafficsim.model.core.ModelInputException;

public abstract class AbstractLane<T> extends AbstractSubsegment<T> implements
		Lane {

	private static final long serialVersionUID = 1L;

	private final NavigableSet<Vehicle> vehicles = new TreeSet<Vehicle>();

	public AbstractLane(long id, Segment segment, double start, double end,
			double width, double shift) throws TransformException,
			ModelInputException {
		super(id, null, segment, start, end, width, shift);
	}

	@Override
	public final Vehicle getHeadVehicle() {
		return vehicles.last();
	}

	@Override
	public final Vehicle getTailVehicle() {
		return vehicles.first();
	}

	@Override
	public final Vehicle getLeadingVehicle(Vehicle v) {
		return vehicles.ceiling(v);
	}

	@Override
	public final Vehicle getPrecedingVehicle(Vehicle v) {
		return vehicles.floor(v);
	}

	@Override
	public final void add(Vehicle vehicle) {
		if (vehicle.currentLane() != null)
			vehicle.currentLane().remove(vehicle);
		vehicles.add(vehicle);
	}

	@Override
	public final void remove(Vehicle vehicle) {
		vehicles.remove(vehicle);
	}

	@Override
	public final void update(Vehicle vehicle) {
		vehicles.remove(vehicle);
		vehicles.add(vehicle);
	}

	@Override
	public final Collection<Vehicle> getVehicles() {
		return Collections.unmodifiableCollection(vehicles);
	}

}