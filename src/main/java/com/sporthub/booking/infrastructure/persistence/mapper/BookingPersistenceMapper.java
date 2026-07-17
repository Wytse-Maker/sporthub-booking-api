package com.sporthub.booking.infrastructure.persistence.mapper;
import com.sporthub.booking.domain.model.Booking;
import com.sporthub.booking.infrastructure.persistence.entity.BookingJpaEntity;

public final class BookingPersistenceMapper {

    private BookingPersistenceMapper() {
    }

    public static Booking toDomain(BookingJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setId(entity.getId());
        booking.setUser(UserPersistenceMapper.toDomain(entity.getUser()));
        booking.setSportEvent(SportEventPersistenceMapper.toDomain(entity.getSportEvent()));
        booking.setNumberOfTickets(entity.getNumberOfTickets());
        booking.setBookingDate(entity.getBookingDate());
        booking.setStatus(entity.getStatus());

        return booking;
    }

    public static BookingJpaEntity toEntity(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingJpaEntity entity = new BookingJpaEntity();
        entity.setId(booking.getId());
        entity.setUser(UserPersistenceMapper.toEntity(booking.getUser()));
        entity.setSportEvent(SportEventPersistenceMapper.toEntity(booking.getSportEvent()));
        entity.setNumberOfTickets(booking.getNumberOfTickets());
        entity.setBookingDate(booking.getBookingDate());
        entity.setStatus(booking.getStatus());

        return entity;
    }
}
