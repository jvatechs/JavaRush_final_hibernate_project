package org.jvatechs.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "country", schema = "world")
@Getter
@Setter
public class Country {
    @Id
    @Column(name = "id")
    private Integer country_id;

    @Column(name = "code")
    private String code;
    @Column(name = "code_2")
    private String alternativeCode;

    @Column(name = "name")
    private String name;

    @Column(name = "continent")
    @Enumerated(EnumType.ORDINAL)
    private Continent continent;

    @Column(name = "region")
    private String region;

    @Column(name = "surface_area", columnDefinition = "decimal", precision = 10, scale = 2)
    private BigDecimal surfaceArea;

    @Column(name = "indep_year")
    private Short independenceYear;

    @Column(name = "population")
    private Integer population;

    @Column(name = "life_expectancy", columnDefinition = "decimal", precision = 3, scale = 1)
    private BigDecimal lifeExpectancy;

    @Column(name = "gnp", columnDefinition = "decimal", precision = 10, scale = 2)
    private BigDecimal GNP;

    @Column(name = "gnpo_id", columnDefinition = "decimal", precision = 10, scale = 2)
    private BigDecimal GNPOId;

    @Column(name = "local_name")
    private String localName;

    @Column(name = "government_form")
    private String governmentForm;

    @Column(name = "head_of_state")
    private String headOfState;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capital")
    private City city;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Set<CountryLanguage> languages;

}
