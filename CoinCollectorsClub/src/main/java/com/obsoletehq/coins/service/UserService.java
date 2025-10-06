package com.obsoletehq.coins.service;

import com.obsoletehq.coins.model.CoinFamily;
import com.obsoletehq.coins.model.User;
import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.repository.CoinFamilyRepository;
import com.obsoletehq.coins.repository.CoinRepository;
import com.obsoletehq.coins.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CoinRepository coinRepo;

    @Autowired
    private CoinFamilyRepository coinFamilyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new user with encrypted password
     */
    public User registerUser(String username, String email, String password) {
        // Check if username already exists
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user with encrypted password
        String encryptedPassword = passwordEncoder.encode(password);
        User user = new User(username, encryptedPassword, email);

        User savedUser = userRepo.save(user);
        logger.info("New user registered: {}", username);

        return savedUser;
    }

    /**
     * Find user by username (for authentication)
     */
    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void addCoinToUserById(UUID userId, UUID coinId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Coin coin = coinRepo.findById(coinId)
                .orElseThrow(() -> new RuntimeException("Coin not found with id: " + coinId));

        user.addCointoCollection(coin);
        updateUserCollectionWorth(user); // Add this line
        userRepo.save(user);
        logger.info("Added coin {} to user {}", coin.getName(), user.getUsername());
    }

    public void addCoinToUserByUsername(String username, UUID coinId) {
        System.out.println("Looking for user with username: '" + username + "'");

        Optional<User> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            // Try to list all usernames to see what's in the database
            List<User> allUsers = userRepo.findAll();
            System.out.println("Available usernames in database:");
            allUsers.forEach(user -> System.out.println("  '" + user.getUsername() + "'"));

            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        user.addCointoCollection(coinId);
        updateUserCollectionWorth(user); // Add this line
        userRepo.save(user);
    }

    @PostConstruct
    public void initializeTestUsers() {
        if (userRepo.count() == 0) {
            try {
                logger.info("Starting test users initialization...");

                // Use passwordEncoder.encode() for test users too
                User user1 = new User("CoinCollector123",
                        passwordEncoder.encode("hashedPassword123"),
                        "collector123@email.com");
                logger.info("Created user1 object with ID: {}", user1.getId());
                User savedUser1 = userRepo.saveAndFlush(user1);
                logger.info("Saved user1 to database: {}", savedUser1.getUsername());

                User user2 = new User("NumismatistPro",
                        passwordEncoder.encode("hashedPassword456"),
                        "pro.coins@email.com");
                logger.info("Created user2 object with ID: {}", user2.getId());

                User savedUser2 = userRepo.saveAndFlush(user2);
                logger.info("Saved user2 to database: {}", savedUser2.getUsername());

                ensureDefaultAdmin();

                logger.info("Test users initialization completed");
                logger.info("Final user count in database: {}", userRepo.count());

            } catch (Exception e) {
                logger.error("Error during test users initialization: ", e);
            }
        } else {
            logger.info("Test users already exist, skipping initialization");
        }
    }


    public User findById(UUID id) {
        return userRepo.findById(id).orElse(null);
    }

    public void deleteUser(UUID id) {
        userRepo.deleteById(id);
    }

    public User authenticate(String username, String password) {
        Optional<User> userOpt = userRepo.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }



    @PostConstruct
    public void ensureDefaultAdmin() {
        try {
            final String adminUsername = "admin";
            final String adminEmail = "admin@gmail.com";
            final String rawPassword = "Admin!1236$";
            final int tokens = 1_000_000;

            userRepo.findByUsername(adminUsername).ifPresentOrElse(existing -> {
                boolean changed = false;

                if (!"ROLE_ADMIN".equals(existing.getRole())) {
                    existing.setRole("ROLE_ADMIN");
                    changed = true;
                }
                if (existing.getTokens() != tokens) {
                    existing.setTokens(tokens);
                    changed = true;
                }
                if (existing.getEmail() == null || !adminEmail.equalsIgnoreCase(existing.getEmail())) {
                    // only set email if empty or different
                    existing.setEmail(adminEmail);
                    changed = true;
                }
                // Reset password only if it looks not encoded (BCrypt hashes start with $2)
                if (existing.getPassword() == null || !existing.getPassword().startsWith("$2")) {
                    existing.setPassword(passwordEncoder.encode(rawPassword));
                    changed = true;
                }

                if (changed) {
                    userRepo.save(existing);
                }
            }, () -> {
                com.obsoletehq.coins.model.User admin = new com.obsoletehq.coins.model.User(
                        adminUsername,
                        passwordEncoder.encode(rawPassword),
                        adminEmail
                );
                admin.setRole("ROLE_ADMIN");
                admin.setTokens(tokens);
                userRepo.save(admin);
            });
        } catch (Exception e) {
            // Log and continue so the app still starts
            LoggerFactory.getLogger(UserService.class).error("Failed to ensure default admin", e);
        }
    }

    public void updateUserCollectionWorth(User user) {
        BigDecimal totalWorth = BigDecimal.ZERO;

        Map<UUID, Integer> coinCollection = user.getCoinCollection();
        if (coinCollection == null || coinCollection.isEmpty()) {
            user.setCollectionWorth(BigDecimal.ZERO);
            return;
        }

        // Get all coin families
        List<CoinFamily> allFamilies = coinFamilyRepository.findAll();

        // Track which families are completed and their values
        Map<CoinFamily, BigDecimal> familyValues = new HashMap<>();
        Map<CoinFamily, Boolean> familyCompleted = new HashMap<>();

        // Initialize tracking for each family
        for (CoinFamily family : allFamilies) {
            familyValues.put(family, BigDecimal.ZERO);

            // Check if user has all coins in this family
            List<Coin> coinsInFamily = coinRepo.findByCoinFamily(family);
            boolean hasAllCoins = true;

            for (Coin coin : coinsInFamily) {
                if (!coinCollection.containsKey(coin.getId()) || coinCollection.get(coin.getId()) == 0) {
                    hasAllCoins = false;
                    break;
                }
            }

            familyCompleted.put(family, hasAllCoins);
        }

        // Calculate value for each coin in the collection
        for (Map.Entry<UUID, Integer> entry : coinCollection.entrySet()) {
            UUID coinId = entry.getKey();
            Integer quantity = entry.getValue();

            coinRepo.findById(coinId).ifPresent(coin -> {
                BigDecimal coinValue = BigDecimal.valueOf(coin.getDenomination());
                BigDecimal totalValue = coinValue.multiply(BigDecimal.valueOf(quantity));

                // Add to family value tracker
                CoinFamily family = coin.getCoinFamily();
                familyValues.put(family, familyValues.get(family).add(totalValue));
            });
        }

        // Calculate total worth, applying 2x multiplier for completed families
        for (Map.Entry<CoinFamily, BigDecimal> entry : familyValues.entrySet()) {
            CoinFamily family = entry.getKey();
            BigDecimal familyValue = entry.getValue();

            if (familyCompleted.get(family)) {
                // Double the value for completed families
                totalWorth = totalWorth.add(familyValue.multiply(BigDecimal.valueOf(2)));
                logger.info("User {} completed family: {} (value doubled)", user.getUsername(), family.getName());
            } else {
                // Normal value for incomplete families
                totalWorth = totalWorth.add(familyValue);
            }
        }

        user.setCollectionWorth(totalWorth);
        logger.info("Updated collection worth for user {}: {}", user.getUsername(), totalWorth);
    }
    public User updateUser(User user) {
        return userRepo.save(user);
    }
    public List<User> getTopUsersByCollectionWorth(int limit) {
        return userRepo.findTopByCollectionWorthOrderByCollectionWorthDesc(limit);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

}