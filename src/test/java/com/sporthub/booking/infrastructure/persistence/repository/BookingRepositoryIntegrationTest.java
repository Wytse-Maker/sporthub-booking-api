package com.sporthub.booking.infrastructure.persistence.repository;

import com.sporthub.booking.domain.model.BookingStatus;
import com.sporthub.booking.infrastructure.persistence.entity.BookingJpaEntity;
import com.sporthub.booking.infrastructure.persistence.entity.SportEventJpaEntity;
import com.sporthub.booking.infrastructure.persistence.entity.UserJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.postgresql.PostgreSQLContainer;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(BookingRepositoryIntegrationTest.PostgresConfiguration.class)
class BookingRepositoryIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private SpringDataSportEventRepository sportEventRepository;

    @Autowired
    private SpringDataBookingRepository bookingRepository;

    @TestConfiguration(proxyBeanMethods = false)
    static class PostgresConfiguration {

        @Bean
        @ServiceConnection
        PostgreSQLContainer postgres() {
            return new PostgreSQLContainer("postgres:17");
        }
    }

    @Test
    void shouldConnectToPostgres(){
        Integer result=jdbcTemplate.queryForObject(
                "SELECT 1",
                Integer.class
        );
        assertEquals(1,result);
    }

    @Test
    void shouldReturnOnlyActiveBookingsForSportEvent() {
        UserJpaEntity user=new UserJpaEntity();
        user .setFirstName("Peter");
        user.setLastName("Pan");
        user.setEmail("peter@test.com");

        userRepository.save(user);

        SportEventJpaEntity sportEvent1=new SportEventJpaEntity();

        sportEvent1.setCapacity(20);

        SportEventJpaEntity sportEvent2=new SportEventJpaEntity();

        sportEvent2.setCapacity(30);

        sportEventRepository.save(sportEvent1);
        sportEventRepository.save(sportEvent2);

        BookingJpaEntity booking1=new BookingJpaEntity();
        booking1.setUser(user);
        booking1.setSportEvent(sportEvent1);
        booking1.setNumberOfTickets(2);
        booking1.setStatus(BookingStatus.ACTIVE);
        booking1.setBookingDate(LocalDateTime.now());

        BookingJpaEntity booking2=new BookingJpaEntity();
        booking2.setUser(user);
        booking2.setSportEvent(sportEvent1);
        booking2.setNumberOfTickets(3);
        booking2.setStatus(BookingStatus.ACTIVE);
        booking2.setBookingDate(LocalDateTime.now());


        BookingJpaEntity booking3=new BookingJpaEntity();
        booking3.setUser(user);
        booking3.setSportEvent(sportEvent1);
        booking3.setNumberOfTickets(4);
        booking3.setStatus(BookingStatus.CANCELLED);
        booking3.setBookingDate(LocalDateTime.now());

        BookingJpaEntity booking4=new BookingJpaEntity();
        booking4.setUser(user);
        booking4.setSportEvent(sportEvent2);
        booking4.setNumberOfTickets(1);
        booking4.setStatus(BookingStatus.ACTIVE);
        booking4.setBookingDate(LocalDateTime.now());

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);

        List<BookingJpaEntity>activeBookings=bookingRepository
                .findBySportEventIdAndStatus(sportEvent1.getId(), BookingStatus.ACTIVE);

        assertEquals(2,activeBookings.size());
        assertTrue(
                activeBookings.stream()
                        .allMatch(booking->booking.getStatus()==BookingStatus.ACTIVE)
        );
        assertTrue(
                activeBookings.stream().allMatch(
                        booking->booking.getSportEvent().getId().equals(sportEvent1.getId()))
        );
    }



    }