package com.sporthub.booking.infrastructure.persistence.adapter;
import com.sporthub.booking.domain.model.Booking;
import com.sporthub.booking.domain.model.BookingStatus;
import com.sporthub.booking.domain.port.out.BookingRepositoryPort;
import com.sporthub.booking.infrastructure.persistence.mapper.BookingPersistenceMapper;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataBookingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookingPersistenceAdapter implements BookingRepositoryPort {

    private final SpringDataBookingRepository springDataBookingRepository;

    public BookingPersistenceAdapter(SpringDataBookingRepository springDataBookingRepository) {
        this.springDataBookingRepository = springDataBookingRepository;
    }

    @Override
    public Booking save(Booking booking) {
        return BookingPersistenceMapper.toDomain(
                springDataBookingRepository.save(BookingPersistenceMapper.toEntity(booking))
        );
    }

    @Override
    public Optional<Booking> findById(Long bookingId) {
        return springDataBookingRepository.findById(bookingId)
                .map(BookingPersistenceMapper::toDomain);
    }

    @Override
    public List<Booking> findByUserId(Long userId) {
        return springDataBookingRepository.findByUserId(userId)
                .stream()
                .map(BookingPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Booking> findActiveBookingsBySportEventId(Long sportEventId) {
        return springDataBookingRepository.findBySportEventIdAndStatus(sportEventId, BookingStatus.ACTIVE)
                .stream()
                .map(BookingPersistenceMapper::toDomain)
                .toList();
    }
}
