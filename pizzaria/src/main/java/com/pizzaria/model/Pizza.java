package com.pizzaria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Pizza {

    // ID randomizado com o generatedValue
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id private Long id;

    private String name;
    // essa é uma tabela auxiliar que vai suportar a lista nativa do java :p
    @ElementCollection
    private List<String> ingredients;
    private String imageUrl;
    private boolean status;


}
