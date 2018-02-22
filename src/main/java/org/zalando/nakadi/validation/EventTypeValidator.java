package org.zalando.nakadi.validation;

import com.google.common.collect.Lists;
import org.json.JSONObject;
import org.zalando.nakadi.domain.EventType;
import org.zalando.nakadi.domain.ValidationStrategyConfiguration;

import java.util.List;
import java.util.Optional;

public class EventTypeValidator {

    private final EventType eventType;
    private final List<EventValidator> validators = Lists.newArrayList();

    public EventTypeValidator(final EventType eventType) {
        this.eventType = eventType;
    }

    public Optional<ValidationError> validate(final JSONObject event) {
        for (final EventValidator ev : validators) {
            Optional<ValidationError> result = ev.accepts(event);
            if (result.isPresent()) {
                return result;
            }
        }

        return Optional.empty();
    }

    public EventTypeValidator withConfiguration(final ValidationStrategyConfiguration vsc) {
        final ValidationStrategy vs = ValidationStrategy.lookup(vsc.getStrategyName());
        validators.add(vs.materialize(eventType, vsc));

        return this;
    }

}
