package com.obsoletehq.coins.repository;

import com.obsoletehq.coins.model.UserCoin;
import com.obsoletehq.coins.model.User;
import com.obsoletehq.coins.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserCoinRepository extends JpaRepository<UserCoin, Long> {

    List<UserCoin> findByUser(User user);

    Optional<UserCoin> findByUserAndCoin(User user, Coin coin);
}
