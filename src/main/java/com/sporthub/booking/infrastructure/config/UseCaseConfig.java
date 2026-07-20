package com.sporthub.booking.infrastructure.config;
import com.sporthub.booking.application.usecase.BookingUseCaseService;
import com.sporthub.booking.application.usecase.SportEventUseCaseService;
import com.sporthub.booking.domain.port.out.BookingRepositoryPort;
import com.sporthub.booking.domain.port.out.SportEventRepositoryPort;
import com.sporthub.booking.domain.port.out.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class UseCaseConfig {

    @Bean
    public BookingUseCaseService bookingUseCaseService(
            BookingRepositoryPort bookingRepositoryPort,
            SportEventRepositoryPort sportEventRepositoryPort,
            UserRepositoryPort userRepositoryPort
    ) {
        return new BookingUseCaseService(
                bookingRepositoryPort,
                sportEventRepositoryPort,
                userRepositoryPort
        );
    }

    @Bean
    public SportEventUseCaseService sportEventUseCaseService(
            SportEventRepositoryPort sportEventRepositoryPort
    ) {
        return new SportEventUseCaseService(sportEventRepositoryPort);
    }
}
