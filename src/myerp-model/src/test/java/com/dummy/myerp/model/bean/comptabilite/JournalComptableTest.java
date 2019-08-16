package com.dummy.myerp.model.bean.comptabilite;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class JournalComptableTest {

    private JournalComptable journalComptable;
    private List<JournalComptable> journalComptableList;

    @BeforeEach
    void initEach() {
        journalComptableList = new ArrayList<>();

        journalComptable = new JournalComptable("JC1","Journal comptable1");
        journalComptableList.add(journalComptable);

        journalComptableList.add(new JournalComptable("JC2","Journal comptable2"));
        journalComptableList.add(new JournalComptable("JC3","Journal comptable3"));
    }

    @AfterEach
    void cleanUpEach() {
        journalComptable = null;
        journalComptableList.clear();
    }

    @Test
    @DisplayName("getByCode() test")
    public void getByCode() {
        assertEquals(JournalComptable.getByCode(journalComptableList, "JC1"), journalComptable);
        assertNull(JournalComptable.getByCode(journalComptableList, "JC4"));
    }
}