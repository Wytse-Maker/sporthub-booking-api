package com.sporthub.booking.infrastructure.persistence.adapter;
import com.sporthub.booking.domain.model.User;
import com.sporthub.booking.domain.port.out.UserRepositoryPort;
import com.sporthub.booking.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.sporthub.booking.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository springDataUserRepository;

    public UserPersistenceAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return springDataUserRepository.findById(userId)
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return UserPersistenceMapper.toDomain(
                springDataUserRepository.save(UserPersistenceMapper.toEntity(user))
        );
    }
}
