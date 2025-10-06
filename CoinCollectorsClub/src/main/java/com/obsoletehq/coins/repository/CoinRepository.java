package com.obsoletehq.coins.repository;

import com.obsoletehq.coins.game.PackType;
import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.model.CoinFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoinRepository extends JpaRepository<Coin, UUID> {
    List<Coin> findByPackType(PackType packType);
    List<Coin> findByCoinFamily(CoinFamily coinFamily);
}
