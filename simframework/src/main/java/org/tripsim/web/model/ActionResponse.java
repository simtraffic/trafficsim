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
package org.tripsim.web.model;

/**
 * 
 * 
 * @author Xuan Shi
 */
public class ActionResponse {

	private final boolean successful;
	private final String message;
	private final String panelUrl;
	private final String redirectUrl;

	public ActionResponse(boolean successful, String message, String panelUrl,
			String redirectUrl) {
		this.successful = successful;
		this.message = message;
		this.panelUrl = panelUrl;
		this.redirectUrl = redirectUrl;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public String getMessage() {
		return message;
	}

	public String getPanelUrl() {
		return panelUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

}
