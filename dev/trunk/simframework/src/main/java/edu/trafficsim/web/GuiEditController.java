package edu.trafficsim.web;

import java.util.Map;

import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.trafficsim.model.ConnectionLane;
import edu.trafficsim.model.Lane;
import edu.trafficsim.model.Link;
import edu.trafficsim.model.Network;
import edu.trafficsim.model.core.ModelInputException;
import edu.trafficsim.web.service.ActionJsonResponseService;
import edu.trafficsim.web.service.JsonOutputService;
import edu.trafficsim.web.service.NetworkEditService;

@Controller
@RequestMapping(value = "/edit")
public class GuiEditController {

	@Autowired
	SimulationProject project;
	@Autowired
	NetworkEditService networkEdit;
	@Autowired
	JsonOutputService jsonOutputService;
	@Autowired
	ActionJsonResponseService actionJsonResponse;

	@RequestMapping(value = "/addlanetolink", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> addLane(@RequestParam("id") long id) {
		Network network = project.getNetwork();
		if (network == null) {
			return actionJsonResponse.messageOnlyResponse("no network.");
		}
		Link link = network.getLink(id);
		if (link == null) {
			return actionJsonResponse
					.messageOnlyResponse("link doesn't exist.");
		}

		try {
			networkEdit.addLane(link, project.getNetworkFactory());
		} catch (ModelInputException e) {
			e.printStackTrace();
		} catch (TransformException e) {
			e.printStackTrace();
		}

		return laneUpdatedResponse(network, id);
	}

	@RequestMapping(value = "/removelanefromlink", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> removeLane(@RequestParam("laneId") int laneId,
			@RequestParam("linkId") long linkId) {

		Network network = project.getNetwork();
		if (network == null) {
			return actionJsonResponse.messageOnlyResponse("no network.");
		}
		Link link = network.getLink(linkId);
		if (link == null) {
			return actionJsonResponse
					.messageOnlyResponse("link doesn't exist.");
		}

		try {
			networkEdit.removeLane(link, laneId);
		} catch (ModelInputException e) {
			e.printStackTrace();
		} catch (TransformException e) {
			e.printStackTrace();
		}

		return laneUpdatedResponse(network, linkId);
	}

	private Map<String, Object> laneUpdatedResponse(Network network, long linkId) {
		return actionJsonResponse.response("lane updated.", "view/link-edit/"
				+ linkId,
				jsonOutputService.getLanesConnectorsJson(network, linkId));
	}

	@RequestMapping(value = "/connectlanes", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> connectLanes(@RequestParam("link1") long link1Id,
			@RequestParam("lane1") int lane1Id,
			@RequestParam("link2") long link2Id,
			@RequestParam("lane2") int lane2Id) {
		Network network = project.getNetwork();
		if (network == null)
			return actionJsonResponse.messageOnlyResponse("no network.");
		Link link1 = network.getLink(link1Id);
		if (link1 == null)
			return actionJsonResponse.messageOnlyResponse("no link.");
		Lane lane1 = link1.getLane(lane1Id);
		if (lane1 == null)
			return actionJsonResponse.messageOnlyResponse("no lane.");
		Link link2 = network.getLink(link2Id);
		if (link2 == null)
			return actionJsonResponse.messageOnlyResponse("no link.");
		Lane lane2 = link2.getLane(lane2Id);
		if (lane2 == null)
			return actionJsonResponse.messageOnlyResponse("no lane.");

		try {
			if (link1.getEndNode() == link2.getStartNode()) {
				if (link1.getEndNode().isConnected(lane1, lane2)) {
					return actionJsonResponse
							.messageOnlyResponse("already connected");
				}
				ConnectionLane connector = networkEdit.connectLanes(lane1,
						lane2, project.getNetworkFactory());
				return connectSuccessResponse(connector, "Success 1!");
			} else if (link1.getStartNode() == link2.getEndNode()) {
				if (link1.getStartNode().isConnected(lane2, lane1)) {
					return actionJsonResponse
							.messageOnlyResponse("already connected");
				}
				ConnectionLane connector = networkEdit.connectLanes(lane2,
						lane1, project.getNetworkFactory());
				return connectSuccessResponse(connector, "Success 2!");
			} else {
				return actionJsonResponse.messageOnlyResponse("no connection");
			}
		} catch (ModelInputException | TransformException e) {
			e.printStackTrace();
		}

		return actionJsonResponse.messageOnlyResponse("null");
	}

	@RequestMapping(value = "/removeconnector", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> removeConnector(
			@RequestParam("fromLink") long fromLinkId,
			@RequestParam("fromLane") int fromLaneId,
			@RequestParam("toLink") long toLinkId,
			@RequestParam("toLane") int toLaneId) {
		Network network = project.getNetwork();
		if (network == null)
			return actionJsonResponse.messageOnlyResponse("no network.");
		Link fromLink = network.getLink(fromLinkId);
		if (fromLink == null)
			return actionJsonResponse.messageOnlyResponse("no link.");
		Lane fromLane = fromLink.getLane(fromLaneId);
		if (fromLane == null)
			return actionJsonResponse.messageOnlyResponse("no lane.");
		Link toLink = network.getLink(toLinkId);
		if (toLink == null)
			return actionJsonResponse.messageOnlyResponse("no link.");
		Lane toLane = toLink.getLane(toLaneId);
		if (toLane == null)
			return actionJsonResponse.messageOnlyResponse("no lane.");

		ConnectionLane connector = fromLane.getLink().getEndNode()
				.getConnector(fromLane, toLane);
		if (connector == null)
			return actionJsonResponse.messageOnlyResponse("no lane.");

		networkEdit.removeConnector(connector);
		return disconnectSuccessResponse(connector, "Success!");
	}

	private Map<String, Object> connectSuccessResponse(
			ConnectionLane connector, String message) {
		return actionJsonResponse.response(message, null,
				jsonOutputService.getConnectorJson(connector));
	}

	private Map<String, Object> disconnectSuccessResponse(
			ConnectionLane connector, String message) {
		return actionJsonResponse.response(message, null,
				jsonOutputService.getConnectorsIdsJson(connector));
	}
}
