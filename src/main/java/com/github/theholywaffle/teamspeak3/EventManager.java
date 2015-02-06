package com.github.theholywaffle.teamspeak3;

/*
 * #%L
 * TeamSpeak 3 Java API
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.exception.TS3UnknownEventException;
import com.github.theholywaffle.teamspeak3.commands.response.DefaultArrayResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

	private final Map<String, TS3EventEmitter> map = new HashMap<>();

	private final List<TS3Listener> listeners = new ArrayList<>();

	public EventManager() {
		map.put("notifytextmessage", new TextMessageEvent());
		map.put("notifycliententerview", new ClientJoinEvent());
		map.put("notifyclientleftview", new ClientLeaveEvent());
		map.put("notifyserveredited", new ServerEditedEvent());
		map.put("notifychanneledited", new ChannelEditedEvent());
		map.put("notifychanneldescriptionchanged", new ChannelDescriptionEditedEvent());
		map.put("notifyclientmoved", new ClientMovedEvent());
		map.put("notifychannelcreated", new ChannelCreateEvent());
		map.put("notifychanneldeleted", new ChannelDeletedEvent());
		map.put("notifychannelmoved", new ChannelMovedEvent());
		map.put("notifychannelpasswordchanged", new ChannelPasswordChangedEvent());
	}

	public void addListeners(TS3Listener... listeners) {
		for (final TS3Listener l : listeners) {
			this.listeners.add(l);
		}
	}

	public void removeListeners(TS3Listener... listeners) {
		for (final TS3Listener l : listeners) {
			this.listeners.remove(l);
		}
	}

	public void fireEvent(String notifyName, String notifyBody) {
		final TS3EventEmitter emitter = map.get(notifyName);
		if (emitter == null) {
			throw new TS3UnknownEventException(notifyName + " " + notifyBody);
		}

		for (final TS3Listener l : listeners) {
			emitter.fire(l, new DefaultArrayResponse(notifyBody).getArray().get(0));
		}
	}
}