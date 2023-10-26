package org.jvatechs.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.ToOne;

@Entity
@Table(name = "city", schema = "world")
@Getter
@Setter
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer cityId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    private String district;

    @Column(name = "population")
    private Integer population;
}
