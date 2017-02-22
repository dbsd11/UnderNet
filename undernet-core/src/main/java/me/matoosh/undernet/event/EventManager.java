package me.matoosh.undernet.event;

import java.util.ArrayList;
import java.util.HashMap;

import me.matoosh.undernet.UnderNet;

/**
 * Manages the events generated by the application.
 * Created by Mateusz Rębacz on 21.02.2017.
 */

public class EventManager {
    /**
     * List of all the registered event handlers.
     */
    public static HashMap<Class, ArrayList<EventHandler>> eventHandlers = new HashMap<>();

    /**
     * Registers an event.
     * @param eventType
     */
    public static void registerEvent(Class eventType) {
        if (!eventHandlers.containsKey(eventType)) {
            UnderNet.logger.info("Registered event type " + eventType.toString());
            eventHandlers.put(eventType, new ArrayList<EventHandler>());
        } else {
            UnderNet.logger.warn("Event type " + eventType.toString() + " already registered!");
        }
    }

    /**
     * Registers an event handler.
     * @param handler
     * @param eventType
     */
    public static void registerHandler(EventHandler handler, Class eventType) {
        if(!eventHandlers.containsKey(eventType)) {
            registerEvent(eventType);
        }

        eventHandlers.get(eventType).add(handler);
    }

    /**
     * Calls the specified event.
     * @param event
     */
    public static void callEvent(Event event) {
        event.onCalled();
        for (EventHandler handler :
            eventHandlers.get(event.getClass()))
        {
            handler.onEventCalled(event);
        }
    }
}
