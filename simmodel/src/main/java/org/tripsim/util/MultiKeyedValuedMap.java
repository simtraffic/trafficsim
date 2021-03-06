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
package org.tripsim.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class MultiKeyedValuedMap<K1, K2, V> implements Serializable {
	private static final long serialVersionUID = 1L;

	private MultiValuedMap<Pair<K1, K2>, V> map;

	public MultiKeyedValuedMap() {
		map = new MultiValuedMap<Pair<K1, K2>, V>();
	}

	public Set<K1> getPrimayKeys() {
		Set<K1> set = new HashSet<K1>();
		for (Pair<K1, K2> key : map.keySet()) {
			set.add(key.primary());
		}
		return set;
	}

	public Set<K2> getSecondaryKeys() {
		Set<K2> set = new HashSet<K2>();
		for (Pair<K1, K2> key : map.keySet()) {
			set.add(key.secondary());
		}
		return set;
	}

	public void put(K1 primaryKey, K2 secondaryKey, V value) {
		map.add(new Pair<K1, K2>(primaryKey, secondaryKey), value);
	}

	public Collection<V> remove(K1 primaryKey, K2 secondaryKey) {
		return map.remove(new Pair<K1, K2>(primaryKey, secondaryKey));
	}

	public Collection<V> get(K1 primaryKey, K2 secondaryKey) {
		return map.get(new Pair<K1, K2>(primaryKey, secondaryKey));
	}

	public MultiValuedMap<K2, V> getByPrimary(K1 primaryKey) {
		MultiValuedMap<K2, V> result = new MultiValuedMap<K2, V>();
		for (Entry<Pair<K1, K2>, Collection<V>> entry : map.entrySet()) {
			Pair<K1, K2> key = entry.getKey();
			if (key.primary().equals(primaryKey)) {
				result.addAll(key.secondary(), entry.getValue());
			}
		}
		return result;
	}

	public MultiValuedMap<K1, V> getBySecondary(K2 secondaryKey) {
		MultiValuedMap<K1, V> result = new MultiValuedMap<K1, V>();
		for (Entry<Pair<K1, K2>, Collection<V>> entry : map.entrySet()) {
			Pair<K1, K2> key = entry.getKey();
			if (key.secondary().equals(secondaryKey)) {
				result.addAll(key.primary(), entry.getValue());
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return String.valueOf(map);
	}

}
