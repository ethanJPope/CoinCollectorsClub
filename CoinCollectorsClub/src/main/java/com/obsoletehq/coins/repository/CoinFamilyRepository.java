package com.obsoletehq.coins.repository;

import com.obsoletehq.coins.model.CoinFamily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CoinFamilyRepository extends JpaRepository<CoinFamily, UUID> {
    CoinFamily findByName(String name);
}
