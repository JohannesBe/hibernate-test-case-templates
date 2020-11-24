package org.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Customer implements Serializable {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "isoCode")
    private Country country;

    public Customer() {
    }

    public Customer(String cName, Country cCountry) {
        if (cName == null) throw new IllegalArgumentException("CustomerWithoutJC: Name cannot be null");
        if (cCountry == null) throw new IllegalArgumentException("CustomerWithoutJC: Country cannot be null");

        this.name = cName;
        this.country = cCountry;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) throw new IllegalArgumentException("CustomerWithoutJC#setName: Name cannot be null");
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        if (country == null) throw new IllegalArgumentException("CustomerWithoutJC#setCountry: Country cannot be null");

        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer that = (Customer) o;
        return getId().equals(that.getId()) &&
                getName().equals(that.getName());
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
