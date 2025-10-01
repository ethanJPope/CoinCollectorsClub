package com.obsoletehq.coins.service;

import com.obsoletehq.coins.model.User;
import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.repository.CoinRepo;
import com.obsoletehq.coins.repository.UserRepo;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CoinRepo coinRepo;

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public void addCoinToUser(User user, Coin coin) {
        user.addCoin(coin);
    }

    @PostConstruct
    @Transactional
    public void initializeTestUsers() {
        if (userRepo.count() == 0) {
            try {
                User user1 = new User("CoinCollector123", "hashedPassword123", "collector123@email.com");

                // Save user first without coins
                user1 = userRepo.save(user1);

                // Then add coin to the saved entity
                Coin firstCoin = coinRepo.findAll().stream().findFirst().orElse(null);
                if (firstCoin != null) {
                    user1.addCoin(firstCoin);
                    userRepo.save(user1);
                }

                User user2 = new User("NumismatistPro", "hashedPassword456", "pro.coins@email.com");

                // Save user first without coins
                user2 = userRepo.save(user2);

                // Then add coin to the saved entity
                Coin secondCoin = coinRepo.findAll().stream().skip(1).findFirst().orElse(null);
                if (secondCoin != null) {
                    user2.addCoin(secondCoin);
                    userRepo.save(user2);
                }

            } catch (Exception e) {
                // Handle error silently
            }
        }
    }

}
