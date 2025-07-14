package org.abc_berkut_test_task.repository;

import org.abc_berkut_test_task.entity.TelegramToken;
import org.abc_berkut_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TelegramTokenRepository extends JpaRepository<TelegramToken, Long> {
    Optional<TelegramToken> findByToken(String token);

    Optional<TelegramToken> findByUser(User user);
}
