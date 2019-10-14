package com.code.eventgenerator.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "eventdetails")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String eventId;

    private long eventDuration;

    private String type;

    private String host;

    private boolean alert;

    @Transient
    private Long timestamp;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public long getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(long eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventDuration == event.eventDuration &&
                alert == event.alert &&
                eventId.equals(event.eventId) &&
                Objects.equals(type, event.type) &&
                Objects.equals(host, event.host) &&
                timestamp.equals(event.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, eventDuration, type, host, alert, timestamp);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventDuration='" + eventDuration + '\'' +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", alert=" + alert +
                ", timestamp=" + timestamp +
                '}';
    }
}
