package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Date;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.ConstraintViolationException;


@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest extends BusinessTestCase {


    @Mock
    private ComptabiliteManagerImpl managerMock;

    private EcritureComptable vEcritureComptable;

    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @BeforeEach
    void init() {

        // création d'une ecriture correcte
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setReference("AC-2019/11111");
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        LigneEcritureComptable ligneEcritureComptable = new LigneEcritureComptable(
                new CompteComptable(1, "compte1"),"ligne ecriture compte Comptable1", BigDecimal.valueOf(12l), BigDecimal.ZERO);
        LigneEcritureComptable ligneEcritureComptable2 = new LigneEcritureComptable(
                new CompteComptable(1, "compte1"),"ligne ecriture compte Comptable2", BigDecimal.ZERO,  BigDecimal.valueOf(12l));
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureComptable);
        vEcritureComptable.getListLigneEcriture().add(ligneEcritureComptable2);
    }


    @Test
    public void checkEcritureComptableUnit() throws Exception {
        // EcritureComptable correcte pas d'exeception remontée
        assertDoesNotThrow(() -> manager.checkEcritureComptableUnit(vEcritureComptable));
    }


    @Test
    public void checkEcritureComptableUnitViolation() {
        // EcritureComptable null
        assertThrows(ConstraintViolationException.class, () -> manager.checkEcritureComptableUnit(new EcritureComptable()));

        // EcritureComptable avec un mauvais format de référence
        vEcritureComptable.setReference("mauvaise ref format");
        assertThrows(ConstraintViolationException.class, () -> manager.checkEcritureComptableUnit(vEcritureComptable));

    }

    @Test
    public void checkEcritureComptableUnitRG2() {
        // les débits != crédits
        assertThrows(FunctionalException.class, () -> {
            vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                     null, new BigDecimal(123),
                                                                                     null));
            vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                     null, null,
                                                                                     new BigDecimal(1234)));
            manager.checkEcritureComptableUnit(vEcritureComptable);
        });

    }

    @Test
    public void checkEcritureComptableUnitRG3() {
        assertThrows(FunctionalException.class, () -> {
            // pas de crédit
            vEcritureComptable.getListLigneEcriture().clear();
            vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                    null, new BigDecimal(123),
                    null));
            vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                    null, new BigDecimal(123),
                    null));
            manager.checkEcritureComptableUnit(vEcritureComptable);
        });
        // pas de débit
        assertThrows(FunctionalException.class, () -> {
            vEcritureComptable.getListLigneEcriture().clear();
            vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                    null, null, new BigDecimal(123)));
            vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                    null, null, new BigDecimal(123)));
            manager.checkEcritureComptableUnit(vEcritureComptable);
        });

        // une seule ligne d'écriture
        assertThrows(ConstraintViolationException.class, () -> {
            vEcritureComptable.getListLigneEcriture().clear();
            vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                    null, new BigDecimal(123),
                    null));
            manager.checkEcritureComptableUnit(vEcritureComptable);
        });
    }

    @Test
    public void checkEcritureComptableUnitRG5() {
        // l'année de référence != à la date de l'écriture
        assertThrows(FunctionalException.class, () -> {
            vEcritureComptable.setReference("AC-2012/11111");
            manager.checkEcritureComptableUnit(vEcritureComptable);
        });

        // le code journal est incorrect
        assertThrows(FunctionalException.class, () -> {
            vEcritureComptable.setReference("BC-2019/11111");
            manager.checkEcritureComptableUnit(vEcritureComptable);
        });

    }

    @Test
    public void checkEcritureComptableContext() throws NotFoundException, FunctionalException {

        // une ecriture comptable existe déjà avec cette référence
        assertThrows(FunctionalException.class, () -> {
            EcritureComptable vEcritureComptableRefAlreadyExist = new EcritureComptable();
            vEcritureComptableRefAlreadyExist.setReference("AC-2019/11111");

            when(managerMock.getEcritureComptableByRef(anyString()))
                    .thenReturn(vEcritureComptableRefAlreadyExist);

            managerMock.checkEcritureComptableContext(vEcritureComptable);
        });

    }


    @Test
    public void insertEcritureComptable() {
    }

    @Test
    public void updateEcritureComptable() {
    }

    @Test
    public void deleteEcritureComptable() {
    }

    @Test
    public void addReference() {
    }
}
