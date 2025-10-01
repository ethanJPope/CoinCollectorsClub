package com.obsoletehq.coins.repository;

import com.obsoletehq.coins.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoinRepo extends JpaRepository<Coin, UUID> {
    // Change from findByIsSpecial to findBySpecial to match the entity property name
    List<Coin> findBySpecial(boolean special);
}
