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
package org.tripsim.api.model;

/**
 * @author Xuan
 * 
 */
public interface ArcSection extends Arc {

	Arc getParent();

	double getStart();

	double getEnd();

	double getShift();

	double getWidth();

	double getLength();

	void setStartOffset(double startOffset);

	void setEndOffset(double endOffset);

	void setShift(double shift, boolean update);

	void setWidth(double width, boolean update);

}
