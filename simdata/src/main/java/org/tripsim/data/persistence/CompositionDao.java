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
package org.tripsim.data.persistence;

import java.util.List;

import org.tripsim.data.dom.CompositionDo;
import org.tripsim.data.dom.TypeCategoryDo;

public interface CompositionDao extends GenericDao<CompositionDo> {

	String getDefaultCompositionName(TypeCategoryDo category);

	List<?> getCompositionField(TypeCategoryDo category, String field);

	CompositionDo getByName(TypeCategoryDo category, String name);

	CompositionDo getDefaultByCategory(TypeCategoryDo category);

	List<CompositionDo> getByCategory(TypeCategoryDo category);

	long countByType(TypeCategoryDo category, String typeName);

	String insert(CompositionDo entity);

}
