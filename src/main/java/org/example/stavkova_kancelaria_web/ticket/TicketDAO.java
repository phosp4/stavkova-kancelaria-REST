//package org.example.stavkova_kancelaria_web.ticket;
//
//import org.example.stavkova_kancelaria_web.exceptions.NotFoundException;
//import org.example.stavkova_kancelaria_web.user.User;
//import org.jooq.DSLContext;
//import org.jooq.Record11;
//import org.jooq.Result;
//import org.jooq.codegen.maven.example.Tables;
//import org.jooq.codegen.maven.example.tables.records.TicketsRecord;
//import org.jooq.impl.DSL;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.jooq.codegen.maven.example.Tables.*;
//
//public class TicketDAO {
//    private final DSLContext dslContext;
//    public TicketDAO(DSLContext dslContext) {
//        this.dslContext = dslContext;
//    }
//
//    //zdroj https://www.jooq.org/doc/latest/manual/getting-started/tutorials/jooq-in-7-steps/
//
//    public List<Ticket> findAllTickets() {
//        List<Ticket> tickets = new ArrayList<>();
//        Result<Record> result = dslContext.select(
//                        TICKETS.TICKET_ID,
//                        USERS.USER_ID,
//                        USERS.USERNAME,
//                        POSSIBLE_OUTCOMES.OUTCOME_ID,
//                        POSSIBLE_OUTCOMES.RESULT_NAME.as("outcome_name"),
//                        TICKETS.STAKE,
//                        TICKETS.STATUS.as("ticket_status"),
//                        SPORT_EVENTS.START_TIME.as("start_time")
//                )
//                .from(TICKETS)
//                .join(USERS).on(TICKETS.USER_ID.eq(USERS.USER_ID))
//                .join(POSSIBLE_OUTCOMES).on(TICKETS.OUTCOME_ID.eq(POSSIBLE_OUTCOMES.OUTCOME_ID))
//                .join(SPORT_EVENTS).on(POSSIBLE_OUTCOMES.EVENT_ID.eq(SPORT_EVENTS.EVENT_ID))
//                .fetch();
//
//        for (Record record : result) {
//            Ticket ticket = new Ticket();
//            ticket.setTicketId(record.get(TICKETS.TICKET_ID));
//
//            User user = new User();
//            user.setUserId(record.get(USERS.USER_ID));
//            user.setUserName(record.get(USERS.USER_NAME));
//            ticket.setUser(user);
//
//            Outcome outcome = new Outcome();
//            outcome.setOutcomeId(record.get(POSSIBLE_OUTCOMES.OUTCOME_ID));
//            outcome.setResultName(record.get("outcome_name", String.class));
//            ticket.setOutcome(outcome);
//
//            ticket.setStake(record.get(TICKETS.STAKE));
//            ticket.setStatus(StatusForTicket.valueOf(record.get("ticket_status", String.class)));
//            ticket.setEventStartTime(record.get("start_time", LocalDateTime.class));
//            tickets.add(ticket);
//        }
//        return tickets;
//    }
//
//    public Ticket findTicketById(int ticketID) {
//        TicketsRecord record = dslContext.selectFrom(TICKETS)
//                .where(TICKETS.TICKET_ID.eq(ticketID))
//                .fetchOne();
//        if (record == null) {
//            throw new NotFoundException("Ticket nebol nájdený");
//        }
//        Ticket ticket = new Ticket();
//        ticket.setTicketId(record.getTicketId());
//        ticket.setUserId(record.getUserId());
//        ticket.setOutcomeId(record.getOutcomeId());
//        ticket.setStake(record.getStake());
//        ticket.setStatus(StatusForTicket.valueOf(record.getStatus()));
//        return ticket;
//    }
//
//    public void updateTicket(Ticket ticket) {
//        boolean ticketExists = dslContext.fetchExists(
//                DSL.selectOne().from(Tables.TICKETS)
//                        .where(Tables.TICKETS.TICKET_ID.eq(ticket.getTicketId()))
//        );
//
//        if (!ticketExists) {
//            throw new NotFoundException("Ticket nebol nájdený");
//        }
//
//        if (ticket.getUserId() == null || ticket.getOutcomeId() == null || ticket.getStake() == null) {
//            throw new IllegalArgumentException("Ticket musí obsahovať všetky atribúty.");
//        }
//
//        dslContext.update(TICKETS)
//                .set(TICKETS.USER_ID, ticket.getUserId())
//                .set(TICKETS.OUTCOME_ID, ticket.getOutcomeId())
//                .set(TICKETS.STAKE, ticket.getStake())
//                .set(TICKETS.STATUS, ticket.getStatus().name())
//                .where(TICKETS.TICKET_ID.eq(ticket.getTicketId()))
//                .execute();
//    }
//
//    public void deleteTicket(int ticketID) {
//        dslContext.deleteFrom(TICKETS)
//                .where(TICKETS.TICKET_ID.eq(ticketID))
//                .execute();
//    }
//
//    public void insertTicket(Ticket ticket) {
//        if (ticket == null) {
//            throw new NotFoundException("Neplatný tiket");
//        }
//
//        int userID = ticket.getUserId();
//        int outcomeID = ticket.getOutcomeId();
//        Double betAmount = ticket.getStake();
//
//        if (betAmount == null) {
//            throw new IllegalArgumentException("Neplatná suma");
//        }
//
//        boolean userExists = dslContext.fetchExists(
//                DSL.selectOne()
//                        .from(USERS)
//                        .where(USERS.USER_ID.eq(userID))
//        );
//
//        boolean outcomeExists = dslContext.fetchExists(
//                DSL.selectOne()
//                        .from(POSSIBLE_OUTCOMES)
//                        .where(POSSIBLE_OUTCOMES.OUTCOME_ID.eq(outcomeID))
//        );
//
//        if (userExists && outcomeExists) {
//            dslContext.insertInto(TICKETS)
//                    .set(TICKETS.USER_ID, userID)
//                    .set(TICKETS.OUTCOME_ID, outcomeID)
//                    .set(TICKETS.STAKE, betAmount)
//                    .set(TICKETS.STATUS, StatusForTicket.pending.name())
//                    .execute();
//        } else {
//            if (!userExists) {
//                throw new IllegalArgumentException("User s týmto ID neexistuje.");
//            }
//            throw new IllegalArgumentException("Outcome s týmto ID neexistuje.");
//        }
//    }
//
//    // other
//
//    public void updateTicketStatusToWon(int ticketID) {
//        dslContext.update(TICKETS)
//                .set(TICKETS.STATUS, StatusForTicket.won.name())
//                .where(TICKETS.TICKET_ID.eq(ticketID))
//                .execute();
//    }
//
//    public void updateTicketStatusToLost(int ticketID) {
//        dslContext.update(TICKETS)
//                .set(TICKETS.STATUS, StatusForTicket.lost.name())
//                .where(TICKETS.TICKET_ID.eq(ticketID))
//                .execute();
//    }
//
//    public List<Ticket> getUsersTickets(Integer userId) {
//        List<Ticket> tickets = new ArrayList<>();
//        Result<Record11<Integer, Integer, Integer, String, Double, String, Integer, String, LocalDateTime, String, String>> result = dslContext.select(
//                        TICKETS.TICKET_ID,
//                        TICKETS.USER_ID,
//                        TICKETS.OUTCOME_ID,
//                        TICKETS.STATUS.as("ticket_status"),
//                        TICKETS.STAKE,
//                        POSSIBLE_OUTCOMES.RESULT_NAME.as("outcome_name"),
//                        POSSIBLE_OUTCOMES.EVENT_ID,
//                        SPORT_EVENTS.EVENT_NAME.as("event_name"),
//                        SPORT_EVENTS.START_TIME.as("start_time"),
//                        SPORT_EVENTS.SPORT_TYPE,
//                        SPORT_EVENTS.STATUS.as("event_status")
//                )
//                .from(TICKETS)
//                .join(POSSIBLE_OUTCOMES).on(TICKETS.OUTCOME_ID.eq(POSSIBLE_OUTCOMES.OUTCOME_ID))
//                .join(SPORT_EVENTS).on(POSSIBLE_OUTCOMES.EVENT_ID.eq(SPORT_EVENTS.EVENT_ID))
//                .where(TICKETS.USER_ID.eq(userId))
//                .fetch();
//
//        for (Record11<Integer, Integer, Integer, String, Double, String, Integer, String, LocalDateTime, String, String> record : result) {
//            Ticket ticket = new Ticket();
//
//            ticket.setTicketId(record.get(TICKETS.TICKET_ID));
//            ticket.setUserId(record.get(TICKETS.USER_ID));
//            ticket.setOutcomeId(record.get(TICKETS.OUTCOME_ID));
//            ticket.setStatus(StatusForTicket.valueOf(String.valueOf(record.get("ticket_status"))));
//            ticket.setStake(record.get(TICKETS.STAKE));
//
//            ticket.setResultName(String.valueOf(record.get("outcome_name")));
//            ticket.setEventName(String.valueOf(record.get("event_name")));
//            ticket.setEventStartTime((LocalDateTime) record.get("start_time"));
//            tickets.add(ticket);
//        }
//        return tickets;
//    }
//
//    public Result<TicketsRecord> fetchTicketsRelatedToEvent(int evnetID) {
//        return dslContext.select(TICKETS.fields())
//                .from(TICKETS)
//                .join(POSSIBLE_OUTCOMES)
//                .on(TICKETS.OUTCOME_ID.eq(POSSIBLE_OUTCOMES.OUTCOME_ID))
//                .where(POSSIBLE_OUTCOMES.EVENT_ID.eq(evnetID))
//                .fetchInto(TICKETS);
//    }
//
//    public Result<TicketsRecord> fetchTicketsForUser(int userID) {
//        return dslContext.selectFrom(TICKETS)
//                .where(TICKETS.USER_ID.eq(userID))
//                .fetch();
//    }
//}
