/*
 *   @Author Johannes Bergé
 *   @Date November 2020
 *
 */
package org.hibernate.bugs.entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NamedEntityGraph(
    name = Customer.EG_WITH_COUNTRY,
    attributeNodes = {
        @NamedAttributeNode(value = "address", subgraph = "address.country")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "address.country",
            attributeNodes = {
                @NamedAttributeNode("country")
            }
        )
    }
)
public class Customer implements Serializable {
    public static final String EG_WITH_COUNTRY = "Customer.WITH_COUNTRY";

    @Id @GeneratedValue
    private Long id;
    private String name;

    @Embedded
    private Address address;

    public Customer() {
    }

    public Customer(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}