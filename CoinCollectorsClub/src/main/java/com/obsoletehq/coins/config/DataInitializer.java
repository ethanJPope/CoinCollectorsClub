package com.obsoletehq.coins.config;

import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.model.User;
import com.obsoletehq.coins.repository.CoinRepo;
import com.obsoletehq.coins.repository.UserRepo;
import com.obsoletehq.coins.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CoinRepo coinRepo;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("DataInitializer is running...");
        logger.info("User count: {}", userRepo.count());
        logger.info("Coin count: {}", coinRepo.count());

        if (userRepo.count() == 0) {
            logger.info("No users found, initializing test users...");
            initializeTestUsers();
        } else {
            logger.info("Users already exist, skipping initialization");
        }
    }

    private void initializeTestUsers() {
        try {
            logger.info("Starting test users initialization...");

            // Force a small delay to ensure coins are initialized
            Thread.sleep(1000);

            long coinCount = coinRepo.count();
            logger.info("Coins available for user initialization: {}", coinCount);

            if (coinCount == 0) {
                logger.warn("No coins found in database. Users will be created without coins.");
            }

            // Create users with coins if available
            try {
                User user1 = new User("CoinCollector123", "hashedPassword123", "collector123@email.com");
                logger.info("Created user1 object with ID: {}", user1.getId());

                // Add a coin if available
                Coin firstCoin = coinRepo.findAll().stream().findFirst().orElse(null);
                if (firstCoin != null) {
                    user1.addCoin(firstCoin);
                    logger.info("Added coin {} to user1", firstCoin.getName());
                }

                User savedUser1 = userRepo.save(user1);
                logger.info("Saved user1 to database: {}", savedUser1.getUsername());

            } catch (Exception e) {
                logger.error("Error creating user1: ", e);
            }

            try {
                User user2 = new User("NumismatistPro", "hashedPassword456", "pro.coins@email.com");
                logger.info("Created user2 object with ID: {}", user2.getId());

                // Add a different coin if available
                Coin secondCoin = coinRepo.findAll().stream().skip(1).findFirst().orElse(null);
                if (secondCoin != null) {
                    user2.addCoin(secondCoin);
                    logger.info("Added coin {} to user2", secondCoin.getName());
                }

                User savedUser2 = userRepo.save(user2);
                logger.info("Saved user2 to database: {}", savedUser2.getUsername());

            } catch (Exception e) {
                logger.error("Error creating user2: ", e);
            }

            logger.info("Test users initialization completed");
            logger.info("Final user count in database: {}", userRepo.count());

        } catch (Exception e) {
            logger.error("Error during test users initialization: ", e);
        }
    }


}
