package edu.trafficsim.model.facilities;

import com.vividsolutions.jts.geom.Point;

public abstract class TCD<T> extends Device<T> {

	private static final long serialVersionUID = 1L;

	public TCD(long id, String name, Point point) {
		super(id, name, point);
	}

}
