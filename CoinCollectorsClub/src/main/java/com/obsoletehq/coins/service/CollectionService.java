package com.obsoletehq.coins.service;

import com.obsoletehq.coins.game.PackType;
import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CollectionService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackService packService;
    @Autowired
    private UserService userService;

    @Transactional
    public List<Coin> openPackForUser(String username, PackType packType) {
        List<Coin> coinsFromPack = packService.openPack(packType);
        for (Coin coin : coinsFromPack) {
            userService.addCoinToUserByUsername(username, coin.getId());
        }

        return coinsFromPack;
    }


}
