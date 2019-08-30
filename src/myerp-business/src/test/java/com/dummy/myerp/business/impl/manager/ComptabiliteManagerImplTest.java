package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.*;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import com.dummy.myerp.testbusiness.business.BusinessTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ComptabiliteManagerImplTest extends BusinessTestCase {

    @InjectMocks
    @Spy
    ComptabiliteManagerImpl manager;

    @Mock
    ComptabiliteManagerImpl test;

    @Mock
    private DaoProxy daoProxyMock;

    @Mock
    private ComptabiliteDao comptabiliteDaoMock;



    private EcritureComptable vEcritureComptable;

//    private ComptabiliteManagerImpl manager = new ComptabiliteManagerImpl();

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);

        // création d'une ecriture correcte
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(null);
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
            EcritureComptable vEcritureComptableRefAlreadyExist = new EcritureComptable();
            vEcritureComptableRefAlreadyExist.setReference("AC-2019/11111");


            when(comptabiliteDaoMock.getEcritureComptableByRef(anyString()))
                    .thenReturn(vEcritureComptableRefAlreadyExist);

            assertThrows(FunctionalException.class, () -> {
                manager.checkEcritureComptableContext(vEcritureComptable);
            });

            // si elles ont le même id, donc même objet alors pas d'erreur
            vEcritureComptable.setId(2);
            vEcritureComptableRefAlreadyExist.setId(2);
            assertDoesNotThrow(() -> manager.checkEcritureComptableContext(vEcritureComptable));

            // si aucune référence identique, pas d'    erreur
            when(comptabiliteDaoMock.getEcritureComptableByRef(anyString()))
                .thenThrow(NotFoundException.class);
            assertDoesNotThrow(() -> manager.checkEcritureComptableContext(vEcritureComptable));

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
    public void addReference() throws NotFoundException, FunctionalException, ParseException {
        vEcritureComptable.setId(-1);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new SimpleDateFormat("yyyy/MM/dd").parse("2016/12/31"));
        vEcritureComptable.setLibelle("Cartouches d’imprimante");

        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(606),
                "Cartouches d’imprimante", new BigDecimal(43),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(4456),
                "TVA 20%", new BigDecimal(8),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(401),
                "Facture F110001", null,
                new BigDecimal(51)));

        SequenceEcritureComptable vExistingSequence = new SequenceEcritureComptable(2019,3);
        Mockito.doReturn(vExistingSequence).when(manager).getSequenceByCodeJournalAndByAnneeCourante(any(SequenceEcritureComptable.class));
        Mockito.doReturn(vEcritureComptable).when(manager).updateEcritureComptable(any());
        Mockito.doNothing().when(manager).insertOrUpdateSequenceEcritureComptable(any());
        manager.addReference(vEcritureComptable);

        Mockito.verify(manager, times(1)).getSequenceByCodeJournalAndByAnneeCourante(any());
        Mockito.verify(manager,times(1)).updateEcritureComptable(any());
        Mockito.verify(manager,times(1)).insertOrUpdateSequenceEcritureComptable(any());


        Mockito.doThrow(new NotFoundException()).when(manager).getSequenceByCodeJournalAndByAnneeCourante(any(SequenceEcritureComptable.class));

        assertThrows(NotFoundException.class, () -> {
            manager.addReference(vEcritureComptable);
        });

        Mockito.doReturn(vExistingSequence).when(manager).getSequenceByCodeJournalAndByAnneeCourante(any(SequenceEcritureComptable.class));
        Mockito.doThrow(new FunctionalException("erreur mise à jour")).when(manager).updateEcritureComptable(any());

        assertThrows(FunctionalException.class, () -> {
            manager.addReference(vEcritureComptable);
        });

        // verification de la référence
        Assertions.assertEquals("AC-2016/00004",vEcritureComptable.getReference());

    }
}
