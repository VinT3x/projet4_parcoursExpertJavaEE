package com.dummy.myerp.model.bean.comptabilite;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JournalComptableTest {

    @Test
    public void getByCode() {
        List<JournalComptable> journalComptableList = new ArrayList<>();

        JournalComptable journalComptable1 = new JournalComptable();
        journalComptable1.setCode("JC1");
        journalComptable1.setLibelle("Journal comptable1");
        journalComptableList.add(journalComptable1);

        JournalComptable journalComptable2 = new JournalComptable();
        journalComptable2.setCode("JC2");
        journalComptable2.setLibelle("Journal comptable2");
        journalComptableList.add(journalComptable2);

        JournalComptable journalComptable3 = new JournalComptable("JC3", "Journal comptable3");
        journalComptableList.add(journalComptable3);

        org.junit.Assert.assertSame(JournalComptable.getByCode(journalComptableList, "JC1"), journalComptable1);
        Assert.assertNull(JournalComptable.getByCode(journalComptableList, "JC4"));

    }
}