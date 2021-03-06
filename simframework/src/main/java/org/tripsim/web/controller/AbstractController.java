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
package org.tripsim.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tripsim.web.model.ActionResponse;
import org.tripsim.web.session.UserContextConveniences;

/**
 * 
 * 
 * @author Xuan Shi
 */
public abstract class AbstractController {

	private static final String ACTION_RESPONSE_STATUS_KEY = "status";
	private static final String ACTION_RESPONSE_DATA_KEY = "data";

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractController.class);

	@Autowired
	UserContextConveniences context;

	@ExceptionHandler(RuntimeException.class)
	public @ResponseBody Map<String, Object> handleError(
			HttpServletRequest req, Exception exception) {
		logger.warn("Failure request due to {}", exception.getMessage());
		logger.warn("Error -- ", exception);
		return failureResponse(exception);
	}

	public static Map<String, Object> failureResponse(String message) {
		return response(false, message, null, null);
	}

	public static Map<String, Object> failureResponse(Exception e) {
		return failureResponse(e.getMessage());
	}

	public static Map<String, Object> successResponse(String message) {
		return response(true, message, null, null);
	}

	public static Map<String, Object> successResponseWithRedirect(
			String message, String url) {
		return response(true, message, null, url, null);
	}

	public static Map<String, Object> successResponse(String message,
			String panelUrl) {
		return response(true, message, panelUrl, null);
	}

	public static Map<String, Object> successResponse(String message,
			String panelUrl, Object data) {
		return response(true, message, panelUrl, data);
	}

	public static Map<String, Object> response(boolean successful,
			String message, String panelUrl, Object data) {
		return response(successful, message, panelUrl, null, data);
	}

	public static Map<String, Object> response(boolean successful,
			String message, String panelUrl, String redirectUrl, Object data) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ACTION_RESPONSE_STATUS_KEY, new ActionResponse(successful,
				message, panelUrl, redirectUrl));
		if (data != null)
			map.put(ACTION_RESPONSE_DATA_KEY, data);
		return map;
	}
}
