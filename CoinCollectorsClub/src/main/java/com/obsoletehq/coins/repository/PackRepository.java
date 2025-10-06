package com.obsoletehq.coins.repository;

import com.obsoletehq.coins.game.PackType;
import com.obsoletehq.coins.model.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PackRepository extends JpaRepository<Pack, UUID> {
    List<Pack> findByPackType(PackType packType);
}
