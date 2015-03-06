package edu.trafficsim.engine.type;

import org.springframework.stereotype.Service;

import edu.trafficsim.api.model.TypesComposition;
import edu.trafficsim.util.WeakReferenceCache;

@Service("cached-types-manager")
public class CachedTypesManager extends AbstractTypesManager implements
		TypesManager {

	// WeakReferenceCache should be Thread-safe
	WeakReferenceCache<String, LinkType> linkTypes = new WeakReferenceCache<String, LinkType>();
	WeakReferenceCache<String, NodeType> nodeTypes = new WeakReferenceCache<String, NodeType>();
	WeakReferenceCache<String, VehicleType> vehicleTypes = new WeakReferenceCache<String, VehicleType>();
	WeakReferenceCache<String, DriverType> driverTypes = new WeakReferenceCache<String, DriverType>();
	WeakReferenceCache<String, TypesComposition> vehicleCompositions = new WeakReferenceCache<String, TypesComposition>();
	WeakReferenceCache<String, TypesComposition> driverCompositions = new WeakReferenceCache<String, TypesComposition>();

	@Override
	public LinkType getLinkType(String name) {
		LinkType type = linkTypes.get(name);
		if (type == null) {
			linkTypes.put(name, type = fetchLinkType(name));
		}
		return type;
	}

	@Override
	public NodeType getNodeType(String name) {
		NodeType type = nodeTypes.get(name);
		if (type == null) {
			nodeTypes.put(name, type = fetchNodeType(name));
		}
		return type;
	}

	@Override
	public VehicleType getVehicleType(String name) {
		VehicleType type = vehicleTypes.get(name);
		if (type == null) {
			vehicleTypes.put(name, type = fetchVehicleType(name));
		}
		return type;
	}

	@Override
	public void deleteVehicleType(String name) {
		super.deleteVehicleType(name);
		vehicleTypes.remove(name);
	}

	@Override
	public DriverType getDriverType(String name) {
		DriverType type = driverTypes.get(name);
		if (type == null) {
			driverTypes.put(name, type = fetchDriverType(name));
		}
		return type;
	}

	@Override
	public void deleteDriverType(String name) {
		super.deleteDriverType(name);
		driverTypes.remove(name);
	}

	@Override
	public TypesComposition getVehicleTypeComposition(String name) {
		TypesComposition compo = vehicleCompositions.get(name);
		if (compo == null) {
			vehicleCompositions.put(name,
					compo = fetchVehicleTypeComposition(name));
		}
		return compo;
	}

	@Override
	public void deleteVehicleTypesComposition(String name) {
		super.deleteVehicleTypesComposition(name);
		vehicleCompositions.remove(name);
	}

	@Override
	public TypesComposition getDriverTypeComposition(String name) {
		TypesComposition compo = driverCompositions.get(name);
		if (compo == null) {
			driverCompositions.put(name,
					compo = fetchDriverTypeComposition(name));
		}
		return compo;
	}

	@Override
	public void deleteDriverTypesComposition(String name) {
		super.deleteDriverTypesComposition(name);
		driverCompositions.remove(name);
	}

}
