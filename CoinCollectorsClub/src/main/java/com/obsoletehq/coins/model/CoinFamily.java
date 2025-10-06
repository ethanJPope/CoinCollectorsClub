// File: `src/main/java/com/obsoletehq/coins/model/CoinFamily.java`
package com.obsoletehq.coins.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
public class CoinFamily {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "display_name")
    private String displayName;

    public CoinFamily() {

    }
    public CoinFamily(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
