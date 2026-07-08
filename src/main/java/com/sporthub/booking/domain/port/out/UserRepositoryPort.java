package com.sporthub.booking.domain.port.out;

import com.sporthub.booking.domain.model.User;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findById(Long userId);

    User save(User user);
}
