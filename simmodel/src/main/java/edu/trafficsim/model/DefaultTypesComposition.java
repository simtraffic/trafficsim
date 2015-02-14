/*
 * Copyright (C) 2014 Xuan Shi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package edu.trafficsim.model;

import java.util.Set;

import edu.trafficsim.model.core.AbstractComposition;

/**
 * 
 * 
 * @author Xuan Shi
 */
public class DefaultTypesComposition extends AbstractComposition<String>
		implements TypesComposition {

	private static final long serialVersionUID = 1L;

	private boolean defaultComposition = false;

	public DefaultTypesComposition(String name, String[] types,
			Double[] probabilities) {
		super(name, types, probabilities);
	}

	@Override
	public final Set<String> getTypes() {
		return keys();
	}

	@Override
	public boolean isDefault() {
		return defaultComposition;
	}

	@Override
	public void setDefault(boolean defaultComposition) {
		this.defaultComposition = defaultComposition;
	}

}
