package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComptabiliteDaoImplTest{

    private static ComptabiliteDaoImpl dao;


    @BeforeAll
    static void initAll() {
        dao = new ComptabiliteDaoImpl();
    }

    @Test
    void insertOrUpdateSequenceEcritureComptable() {
    }

    @Test
    void getListCompteComptable() {
    }

    @Test
    void getListEcritureComptable() {
        List<EcritureComptable> vList = dao.getListEcritureComptable();
        assertEquals(5, vList.size());
    }

    @Test
    void getEcritureComptable() {
    }

    @Test
    void getEcritureComptableByRef() {
    }

    @Test
    void loadListLigneEcriture() {
    }

    @Test
    void insertEcritureComptable() {
    }

    @Test
    void updateEcritureComptable() {
    }

    @Test
    void deleteEcritureComptable() {
    }

    @Test
    void getSequenceByCodeJournalAndByAnneeCourante() {
    }

    @Test
    void insertOrUpdateSequenceEcritureComptable1() {
    }
}