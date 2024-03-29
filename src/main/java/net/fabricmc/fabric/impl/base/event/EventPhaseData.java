/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.base.event;

import com.chyzman.util.Id;
import net.fabricmc.fabric.impl.base.toposort.SortableNode;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Data of an {@link ArrayBackedEvent} phase.
 */
class EventPhaseData<T> extends SortableNode<EventPhaseData<T>> {
	final Id id;
	List<T> listeners;

	@SuppressWarnings("unchecked")
	EventPhaseData(Id id, Class<?> listenerClass) {
		this.id = id;
		this.listeners = List.of((T[]) Array.newInstance(listenerClass, 0));
	}

	void addListener(T listener) {
		listeners.add(listener);
	}

	void removeListener(T listener) {
		listeners.remove(listener);
	}

	@Override
	protected String getDescription() {
		return id.toString();
	}
}