package com.sporthub.booking.infrastructure.config;
import com.sporthub.booking.infrastructure.persistence.entity.SportEventJpaEntity;
import com.sporthub.booking.infrastructure.persistence.entity.TeamJpaEntity;
import com.sporthub.booking.infrastructure.persistence.entity.UserJpaEntity;
import com.sporthub.booking.infrastructure.persistence.entity.VenueJpaEntity;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataSportEventRepository;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataTeamRepository;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataUserRepository;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataVenueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(
            SpringDataUserRepository userRepository,
            SpringDataTeamRepository teamRepository,
            SpringDataVenueRepository venueRepository,
            SpringDataSportEventRepository sportEventRepository
    ) {
        return args -> {
            if (sportEventRepository.count() > 0) {
                return;
            }

            UserJpaEntity user = new UserJpaEntity();
            user.setFirstName("Wytse");
            user.setLastName("Van Stichel");
            user.setEmail("wytse@example.com");
            userRepository.save(user);

            TeamJpaEntity lakers = new TeamJpaEntity();
            lakers.setName("Los Angeles Lakers");
            lakers.setCity("Los Angeles");
            lakers.setAbbreviation("LAL");
            lakers = teamRepository.save(lakers);

            TeamJpaEntity warriors = new TeamJpaEntity();
            warriors.setName("Golden State Warriors");
            warriors.setCity("San Francisco");
            warriors.setAbbreviation("GSW");
            warriors = teamRepository.save(warriors);

            TeamJpaEntity bulls = new TeamJpaEntity();
            bulls.setName("Chicago Bulls");
            bulls.setCity("Chicago");
            bulls.setAbbreviation("CHI");
            bulls = teamRepository.save(bulls);

            TeamJpaEntity celtics = new TeamJpaEntity();
            celtics.setName("Boston Celtics");
            celtics.setCity("Boston");
            celtics.setAbbreviation("BOS");
            celtics = teamRepository.save(celtics);

            VenueJpaEntity cryptoArena = new VenueJpaEntity();
            cryptoArena.setName("Crypto.com Arena");
            cryptoArena.setCity("Los Angeles");
            cryptoArena.setCapacity(20000);
            cryptoArena = venueRepository.save(cryptoArena);

            VenueJpaEntity chaseCenter = new VenueJpaEntity();
            chaseCenter.setName("Chase Center");
            chaseCenter.setCity("San Francisco");
            chaseCenter.setCapacity(18000);
            chaseCenter = venueRepository.save(chaseCenter);

            SportEventJpaEntity event1 = new SportEventJpaEntity();
            event1.setHomeTeam(lakers);
            event1.setAwayTeam(warriors);
            event1.setVenue(cryptoArena);
            event1.setStartTime(LocalDateTime.now().plusDays(14));
            event1.setTicketPrice(BigDecimal.valueOf(89.99));
            event1.setCapacity(20000);
            sportEventRepository.save(event1);

            SportEventJpaEntity event2 = new SportEventJpaEntity();
            event2.setHomeTeam(warriors);
            event2.setAwayTeam(bulls);
            event2.setVenue(chaseCenter);
            event2.setStartTime(LocalDateTime.now().plusDays(21));
            event2.setTicketPrice(BigDecimal.valueOf(74.99));
            event2.setCapacity(18000);
            sportEventRepository.save(event2);

            SportEventJpaEntity event3 = new SportEventJpaEntity();
            event3.setHomeTeam(lakers);
            event3.setAwayTeam(celtics);
            event3.setVenue(cryptoArena);
            event3.setStartTime(LocalDateTime.now().plusDays(30));
            event3.setTicketPrice(BigDecimal.valueOf(99.99));
            event3.setCapacity(20000);
            sportEventRepository.save(event3);
        };
    }
}
