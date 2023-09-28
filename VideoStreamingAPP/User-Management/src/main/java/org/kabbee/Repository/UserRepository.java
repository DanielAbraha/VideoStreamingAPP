package org.kabbee.Repository;

import org.kabbee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByPassword(String password);

    Optional<User> findUserByUsernameAndPassword(String username, String password);

    Optional<User> findUserByEmail(String email);
}
