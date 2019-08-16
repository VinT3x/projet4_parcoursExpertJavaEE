package com.dummy.myerp.model.bean.comptabilite;


import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;


public class CompteComptableTest {

    private CompteComptable compteComptable;
    private List<CompteComptable> compteComptableList;

    @BeforeEach
    void initEach() {
        // creation et alimentation d'une liste de CompteComptable
        compteComptableList = new ArrayList<>();
        //----- creation du compte pour les tests
        compteComptable = new CompteComptable(1,"compte comptable 1");
        compteComptableList.add(compteComptable);
        //----- alimentation de la liste avec d'autres comptes
        compteComptableList.add(new CompteComptable(2, "compte comptable 2"));
        compteComptableList.add(new CompteComptable(3, "compte comptable 3"));

    }

    @AfterEach
    void cleanUpEach() {
        compteComptable = null;
        compteComptableList.clear();
    }

    @Test
    @DisplayName("getByNumero() test")
    public void getByNumero() {
        assertSame(CompteComptable.getByNumero(compteComptableList, 1), compteComptable);
        assertNull(CompteComptable.getByNumero(compteComptableList, 4));

    }
}