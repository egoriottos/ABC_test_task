package org.abc_berkut_test_task.repository;

import org.abc_berkut_test_task.entity.Message;
import org.abc_berkut_test_task.entity.User;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUserOrderByCreatedAtDesc(User user);
}
