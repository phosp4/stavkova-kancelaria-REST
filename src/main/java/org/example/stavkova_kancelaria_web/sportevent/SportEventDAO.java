package org.example.stavkova_kancelaria_web.sportevent;

import org.example.stavkova_kancelaria_web.exceptions.NotFoundException;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record5;
import org.jooq.Result;
import org.jooq.codegen.maven.example.Tables;
import org.jooq.impl.DSL;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.codegen.maven.example.tables.SportEvents.SPORT_EVENTS;

public class SportEventDAO {
    private final DSLContext dslContext;
    public SportEventDAO(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    // zdroj https://www.jooq.org/doc/latest/manual/getting-started/tutorials/jooq-in-7-steps/
    public List<SportEvent> getAllSportEvents() {
        List<SportEvent> events = new ArrayList<>();
        Result<Record5<Integer, String, LocalDateTime, String, String>> result = dslContext
                .select(SPORT_EVENTS.EVENT_ID, SPORT_EVENTS.EVENT_NAME, SPORT_EVENTS.START_TIME, SPORT_EVENTS.SPORT_TYPE, SPORT_EVENTS.STATUS)
                .from(SPORT_EVENTS)
                .where(SPORT_EVENTS.VISIBILITY.eq("visible"))
                .orderBy(SPORT_EVENTS.START_TIME.desc())
                .fetch();

        for (Record record : result) {
            SportEvent event = new SportEvent();
            event.setEventId(record.getValue(SPORT_EVENTS.EVENT_ID));
            event.setEventName(record.getValue(SPORT_EVENTS.EVENT_NAME));
            event.setStartTime(record.getValue(SPORT_EVENTS.START_TIME));
            event.setSportType(record.getValue(SPORT_EVENTS.SPORT_TYPE));
            event.setStatus(StatusForEvent.valueOf(record.getValue(SPORT_EVENTS.STATUS)));
            events.add(event);
        }

        return events;
    }

    /**
    * alternative to deleteEvent method
     */
    public void hideEvent(int eventId) {

        // --- added code by sd ---
        boolean eventExists = dslContext.fetchExists(
                DSL.selectOne().from(Tables.SPORT_EVENTS)
                        .where(Tables.SPORT_EVENTS.EVENT_ID.eq(eventId))
        );

        if (!eventExists) {
            throw new IllegalArgumentException("Event nebol nájdený");
        }
        // --- end of added code by sd ---

        dslContext.update(SPORT_EVENTS)
                .set(SPORT_EVENTS.VISIBILITY, "hidden")
                .where(SPORT_EVENTS.EVENT_ID.eq(eventId))
                .execute();
    }

    /**
    * method was modified to take SportEvent object as parameter instead of individual fields
     */
    public int createEvent(SportEvent event) {

        String eventName = event.getEventName();
        LocalDateTime startTime = event.getStartTime();
        String sportType = event.getSportType();

        if (startTime == null) {
            throw new IllegalArgumentException("Neplatný čas eventu");
        }

        if (eventName == null || eventName.isEmpty()) {
            throw new IllegalArgumentException("Neplatný názov eventu");
        }

        if (sportType == null || sportType.isEmpty()) {
            throw new IllegalArgumentException("Neplatný športový typ");
        }

        return dslContext.insertInto(Tables.SPORT_EVENTS)
                .set(Tables.SPORT_EVENTS.EVENT_NAME, eventName)
                .set(Tables.SPORT_EVENTS.START_TIME, startTime)
                .set(Tables.SPORT_EVENTS.SPORT_TYPE, sportType)
                .set(Tables.SPORT_EVENTS.STATUS, StatusForEvent.upcoming.name())
                .returning(Tables.SPORT_EVENTS.EVENT_ID)
                .fetchOne()
                .getValue(Tables.SPORT_EVENTS.EVENT_ID);
    }

    public void updateEventStatus(int eventID) {
        boolean eventExists = dslContext.fetchExists(
                DSL.selectOne().from(Tables.SPORT_EVENTS)
                        .where(Tables.SPORT_EVENTS.EVENT_ID.eq(eventID))
        );

        if (!eventExists) {
            throw new IllegalArgumentException("Event nebol nájdený");
        }

        dslContext.update(Tables.SPORT_EVENTS)
                .set(Tables.SPORT_EVENTS.STATUS, StatusForEvent.finished.name())
                .where(Tables.SPORT_EVENTS.EVENT_ID.eq(eventID))
                .execute();
    }

    /**
    * method was added to find event by id to create basic crud operations
     */
    public SportEvent findById(int eventId) {

        Record record = dslContext.select(SPORT_EVENTS.EVENT_ID, SPORT_EVENTS.EVENT_NAME, SPORT_EVENTS.START_TIME, SPORT_EVENTS.SPORT_TYPE, SPORT_EVENTS.STATUS)
                .from(SPORT_EVENTS)
                .where(SPORT_EVENTS.EVENT_ID.eq(eventId))
                .fetchOne();

        if (record == null) {
            throw new NotFoundException("Event with id " + eventId + " not found");
        }

        SportEvent event = new SportEvent();
        event.setEventId(record.getValue(SPORT_EVENTS.EVENT_ID));
        event.setEventName(record.getValue(SPORT_EVENTS.EVENT_NAME));
        event.setStartTime(record.getValue(SPORT_EVENTS.START_TIME));
        event.setSportType(record.getValue(SPORT_EVENTS.SPORT_TYPE));
        event.setStatus(StatusForEvent.valueOf(record.getValue(SPORT_EVENTS.STATUS)));

        return event;
    }

    public void updateSportEvent(SportEvent event) {
        boolean eventExists = dslContext.fetchExists(
                DSL.selectOne().from(Tables.SPORT_EVENTS)
                        .where(Tables.SPORT_EVENTS.EVENT_ID.eq(event.getEventId()))
        );

        if (!eventExists) {
            throw new NotFoundException("Event nebol nájdený");
        }

        if (event.getEventName() == null || event.getEventName().isEmpty() || event.getStartTime() == null || event.getSportType() == null || event.getSportType().isEmpty()) {
            throw new IllegalArgumentException("Event musí obsahovať všetky atribúty.");
        }

        dslContext.update(SPORT_EVENTS)
                .set(SPORT_EVENTS.EVENT_NAME, event.getEventName())
                .set(SPORT_EVENTS.START_TIME, event.getStartTime())
                .set(SPORT_EVENTS.SPORT_TYPE, event.getSportType())
                .set(SPORT_EVENTS.STATUS, event.getStatus().name())
                .where(SPORT_EVENTS.EVENT_ID.eq(event.getEventId()))
                .execute();
    }
}
