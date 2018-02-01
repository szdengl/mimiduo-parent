package net.mimiduo.boot.web.action;

import net.mimiduo.boot.common.cache.event.EventData;
import org.springframework.beans.factory.annotation.Autowired;


import reactor.core.Reactor;
import reactor.event.Event;

public abstract class EventSupportController extends BaseController {

	@Autowired
	private Reactor reactor;

	public void publishEvent(String topic, EventData eventData) {
		reactor.notify(topic, Event.wrap(eventData));
	}

	public void setReactor(Reactor reactor) {
		this.reactor = reactor;
	}

}
