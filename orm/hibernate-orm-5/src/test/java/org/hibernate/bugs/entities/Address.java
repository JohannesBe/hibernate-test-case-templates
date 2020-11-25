/*
 *   @Author Johannes Bergé
 *   @Date November 2020
 *
 */
package org.hibernate.bugs.entities;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class Address implements Serializable {
    private String line1;
    private String line2;
    private String state;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Country country;

    public Address() {
    }

    public Address(String line1, String line2, String state, Country country) {
        this.line1 = line1;
        this.line2 = line2;
        this.state = state;
        this.country = country;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
