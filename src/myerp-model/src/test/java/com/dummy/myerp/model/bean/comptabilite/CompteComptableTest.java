package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CompteComptableTest {

    @Test
    public void getByNumero() {
        List<CompteComptable> compteComptableList = new ArrayList<>();

        CompteComptable compteComptable1 = new CompteComptable();
        compteComptable1.setLibelle("compte comptable 1");
        compteComptable1.setNumero(1);
        compteComptableList.add(compteComptable1);

        CompteComptable compteComptable2 = new CompteComptable();
        compteComptable2.setLibelle("compte comptable 2");
        compteComptable2.setNumero(2);
        compteComptableList.add(compteComptable2);

        CompteComptable compteComptable3 = new CompteComptable();
        compteComptable3.setLibelle("compte comptable 3");
        compteComptable3.setNumero(3);
        compteComptableList.add(compteComptable3);

        Assert.assertSame(CompteComptable.getByNumero(compteComptableList, 1), compteComptable1);
        Assert.assertNull(CompteComptable.getByNumero(compteComptableList, 4));

    }
}