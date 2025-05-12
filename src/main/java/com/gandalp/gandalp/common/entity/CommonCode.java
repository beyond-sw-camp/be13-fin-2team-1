package com.gandalp.gandalp.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CommonCode {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "common-code-id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String codeGroup;

    @Column(nullable = false, length = 50)
    private String codeValue;

    @Column(nullable = false, length = 50, unique = true)
    private String codeLabel;


    private char useYn;

    private int sortOrder;

}
