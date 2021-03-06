package com.coelho.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_SALE")
@Cacheable
@ToString
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private UUID id;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "PRICE")
    private BigDecimal price;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "FK_USER_ID")
    @ToString.Exclude
    private User user;

    @PrePersist
    private void setDate(){
        this.date = new Date();
    }
}