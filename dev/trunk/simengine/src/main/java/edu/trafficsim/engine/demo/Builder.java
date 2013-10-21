package edu.trafficsim.engine.demo;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import edu.trafficsim.engine.VehicleFactory;
import edu.trafficsim.engine.VehicleGenerator;
import edu.trafficsim.engine.factory.DefaultNetworkFactory;
import edu.trafficsim.engine.factory.DefaultVehicleFactory;
import edu.trafficsim.engine.generator.DefaultVehicleGenerator;
import edu.trafficsim.model.Connector;
import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Link;
import edu.trafficsim.model.Network;
import edu.trafficsim.model.Node;
import edu.trafficsim.model.Od;
import edu.trafficsim.model.Router;
import edu.trafficsim.model.VehicleTypeComposition;
import edu.trafficsim.model.core.Coordinates;
import edu.trafficsim.model.core.Coordinates.TransformCoordinateFilter;
import edu.trafficsim.model.core.ModelInputException;
import edu.trafficsim.model.demand.DefaultDriverTypeComposition;
import edu.trafficsim.model.demand.DefaultOd;
import edu.trafficsim.model.demand.DefaultVehicleTypeComposition;
import edu.trafficsim.model.network.DefaultNetwork;
import edu.trafficsim.model.network.TurnPercentageRouter;
import edu.trafficsim.model.roadusers.DriverType;
import edu.trafficsim.model.roadusers.VehicleType;
import edu.trafficsim.model.roadusers.VehicleType.VehicleClass;

// Hack to create nodes links...
public class Builder {

	private DefaultNetwork network;
	private DefaultNetworkFactory networkFactory;
	private DefaultVehicleFactory vehicleFactory;
	private DefaultVehicleGenerator vehicleGenerator;

	public Builder() throws ModelInputException {
		network = new DefaultNetwork();
		networkFactory = DefaultNetworkFactory.getInstance();
		vehicleFactory = DefaultVehicleFactory.getInstance();
		vehicleGenerator = DefaultVehicleGenerator.getInstance();

		build();
	}

	private void build() throws ModelInputException {

		// TODO using WTKReader, or other well known format reader if viable

		// Links and Nodes need to have there own coordinate object to avoid
		// transform errors
		// Coonectors, on the other hands, shared the end coordinate object with
		// connecting Links

		// Johnson@Randall
		Coordinate coord53596818 = new Coordinate(43.0728056, -89.409022);
		Coordinate coord53596819 = new Coordinate(43.072726, -89.408787);
		Coordinate coord1345424868 = new Coordinate(43.0726121, -89.4084588);
		Coordinate coord53596820 = new Coordinate(43.072565, -89.408323);
		Coordinate coord53596821 = new Coordinate(43.07241, -89.407859);
		Coordinate coord53596824 = new Coordinate(43.072325, -89.407584);
		Coordinate coord53596826 = new Coordinate(43.0722933, -89.4074786);
		// Johnson@Orchard
		Coordinate coord1345424866 = new Coordinate(43.0722666, -89.4073831);
		Coordinate coord1533633321 = new Coordinate(43.072231, -89.407203);
		Coordinate coord53596827 = new Coordinate(43.0722033, -89.4069557);
		Coordinate coord1345416864 = new Coordinate(43.0722028, -89.4069465);
		Coordinate coord1859358846 = new Coordinate(43.0721947, -89.406805);
		Coordinate coord53720210 = new Coordinate(43.0721884, -89.4066958);
		Coordinate coord1859358892 = new Coordinate(43.072183, -89.4065084);
		Coordinate coord53720208 = new Coordinate(43.072171, -89.40609);
		// Johnson@Charter
		Coordinate coord53607075 = new Coordinate(43.072159, -89.405751);

		// Johson from Randall to Orchard
		Coordinate[] coords1 = new Coordinate[] { coord53596818, coord53596819,
				coord1345424868, coord53596820, coord53596821, coord53596824,
				coord53596826, coord1345424866 };
		// Johson from Orchard to Charter
		Coordinate[] coords2 = new Coordinate[] {
				new Coordinate(coord1345424866), coord1533633321,
				coord53596827, coord1345416864, coord1859358846, coord53720210,
				coord1859358892, coord53720208, coord53607075 };

		// Nodes
		Node node1 = networkFactory.createNode("Johnson at Randall",
				new Coordinate(coord53596818));
		Node node2 = networkFactory.createNode("Johnson at Orchardl",
				new Coordinate(coord1345424866));
		Node node3 = networkFactory.createNode("Johnson at Charter",
				new Coordinate(coord53607075));
		// Node node4 = networkFactory.createNode("Johnson at Mill");
		// Node node5 = networkFactory.createNode("Johnson at Park");
		// Links
		Link link1 = networkFactory
				.createLink("Johson1", node1, node2, coords1);
		Link link2 = networkFactory
				.createLink("Johson2", node2, node3, coords2);
		// Lanes
		List<Lane> lanes1 = networkFactory.createLanes(link1, 3);
		List<Lane> lanes2 = networkFactory.createLanes(link2, 3);
		// Connectors
		Connector connector200 = networkFactory.createConnector(lanes1.get(0),
				lanes2.get(0));
		Connector connector211 = networkFactory.createConnector(lanes1.get(1),
				lanes2.get(1));
		Connector connector222 = networkFactory.createConnector(lanes1.get(2),
				lanes2.get(2));

		// Network
		network.add(node1, node2, node3);
		network.add(link1, link2);
		network.discover();

		// Transform
		TransformCoordinateFilter filter = Coordinates
				.getDefaultTransformFilter();
		network.transform(filter);

		System.out.println(link1.getLength());
		System.out.println(link2.getLength());

		// Vehicle Type
		VehicleType vehicleTypeCar = new VehicleType("TestCar",
				VehicleClass.Car);
		VehicleType vehicleTypeTruck = new VehicleType("TestTruck",
				VehicleClass.Truck);
		VehicleType[] vehicleTypes = new VehicleType[] { vehicleTypeCar,
				vehicleTypeTruck };
		double[] vehPossibilities = new double[] { 0.8, 0.2 };
		VehicleTypeComposition vehicleTypeComposition = new DefaultVehicleTypeComposition(
				vehicleTypes, vehPossibilities);

		// Driver Type
		DriverType driverType = new DriverType("TestDriver");
		DriverType[] driverTypes = new DriverType[] { driverType };
		double[] drvPossibilities = new double[] { 1.0 };
		DefaultDriverTypeComposition driverTypeComposition = new DefaultDriverTypeComposition(
				driverTypes, drvPossibilities);

		// Origin Destination
		// no destination 0s ~ 100s 1000vph
		// no destination 100s~200s 800vph
		double[] times = new double[] { 300, 500 };
		Integer[] vphs = new Integer[] { 1600, 1000 };
		Od od = new DefaultOd(node1, null, vehicleTypeComposition,
				driverTypeComposition, times, vphs);
		network.add(od);

		// Router
		Router router = new TurnPercentageRouter();
		node2.setRouter(router);
	}

	public Network getNetwork() {
		return network;
	}

	public VehicleFactory getVehicleFactory() {
		return vehicleFactory;
	}

	public VehicleGenerator getVehicleGenerator() {
		return vehicleGenerator;
	}

}