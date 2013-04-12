package edu.trafficsim.model.core;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import edu.trafficsim.model.DataContainer;

public interface Segment extends DataContainer {

	public Coordinate getStartCoordinate();

	public Coordinate getEndCoordinate();

	public LineString getLinearGeom();

	// transfer local coordinate to global coordinate
	public Coordinate getCoordinate(double x, double y);
	
	public double getWidth();

	public double getLength();
	
	public List<SegmentElement> getElements();
}
