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
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.Subscription;
import net.fabricmc.fabric.impl.base.toposort.NodeSorting;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

class ArrayBackedEvent<T> extends Event<T> {
	private final Function<T[], T> invokerFactory;
	private final Object lock = new Object();
	private T[] handlers;
	/**
	 * Registered event phases.
	 */
	private final Map<Id, EventPhaseData<T>> phases = new LinkedHashMap<>();
	/**
	 * Phases sorted in the correct dependency order.
	 */
	private final List<EventPhaseData<T>> sortedPhases = new ArrayList<>();

	@SuppressWarnings("unchecked")
	ArrayBackedEvent(Class<? super T> type, Function<T[], T> invokerFactory) {
		this.invokerFactory = invokerFactory;
		this.handlers = (T[]) Array.newInstance(type, 0);
		update();
	}

	void update() {
		this.invoker = invokerFactory.apply(handlers);
	}

	@Override
	public Subscription<T> register(T listener) {
		return register(DEFAULT_PHASE, listener);
	}

	@Override
	public Subscription<T> register(Id phaseId, T listener) {
		Objects.requireNonNull(phaseId, "Tried to register a listener for a null phase!");
		Objects.requireNonNull(listener, "Tried to register a null listener!");

		synchronized (lock) {
			getOrCreatePhase(phaseId, true).addListener(listener);
			rebuildInvoker(handlers.length + 1);
		}

		return new Subscription<>(this, phaseId, listener);
	}

	@Override
	public Event<T> unregister(Id phaseId, T listener) {
		synchronized (lock) {
			getOrCreatePhase(phaseId, true).removeListener(listener);
			rebuildInvoker(handlers.length + 1);
		}

		return this;
	}

	private EventPhaseData<T> getOrCreatePhase(Id id, boolean sortIfCreate) {
		EventPhaseData<T> phase = phases.get(id);

		if (phase == null) {
			phase = new EventPhaseData<>(id, handlers.getClass().getComponentType());
			phases.put(id, phase);
			sortedPhases.add(phase);

			if (sortIfCreate) {
				NodeSorting.sort(sortedPhases, "event phases", Comparator.comparing(data -> data.id));
			}
		}

		return phase;
	}

	private void rebuildInvoker(int newLength) {
		var listenerClass = handlers.getClass().getComponentType();

		// Rebuild handlers.
		if (sortedPhases.size() == 1) {
			// Special case with a single phase: use the array of the phase directly.
			handlers = sortedPhases.get(0).listeners.toArray((T[]) Array.newInstance(listenerClass, 0));
		} else {
			@SuppressWarnings("unchecked")
			T[] newHandlers = (T[]) Array.newInstance(handlers.getClass().getComponentType(), newLength);
			int newHandlersIndex = 0;

			for (EventPhaseData<T> existingPhase : sortedPhases) {
				int length = existingPhase.listeners.size();
				var array = existingPhase.listeners.toArray((T[]) Array.newInstance(listenerClass, 0));
				System.arraycopy(array, 0, newHandlers, newHandlersIndex, length);
				newHandlersIndex += length;
			}

			handlers = newHandlers;
		}

		// Rebuild invoker.
		update();
	}

	@Override
	public void addPhaseOrdering(Id firstPhase, Id secondPhase) {
		Objects.requireNonNull(firstPhase, "Tried to add an ordering for a null phase.");
		Objects.requireNonNull(secondPhase, "Tried to add an ordering for a null phase.");
		if (firstPhase.equals(secondPhase)) throw new IllegalArgumentException("Tried to add a phase that depends on itself.");

		synchronized (lock) {
			EventPhaseData<T> first = getOrCreatePhase(firstPhase, false);
			EventPhaseData<T> second = getOrCreatePhase(secondPhase, false);
			EventPhaseData.link(first, second);
			NodeSorting.sort(this.sortedPhases, "event phases", Comparator.comparing(data -> data.id));
			rebuildInvoker(handlers.length);
		}
	}
}