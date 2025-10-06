package com.obsoletehq.coins.repository;

import com.obsoletehq.coins.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u ORDER BY u.collectionWorth DESC")
    List<User> findTopByCollectionWorthOrderByCollectionWorthDesc(Pageable pageable);
    default List<User> findTopByCollectionWorthOrderByCollectionWorthDesc(int limit) {
        return findTopByCollectionWorthOrderByCollectionWorthDesc(PageRequest.of(0, limit));
    }
}
