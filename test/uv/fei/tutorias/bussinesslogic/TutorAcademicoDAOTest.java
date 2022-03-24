package uv.fei.tutorias.bussinesslogic;

import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uv.fei.tutorias.domain.Persona;

// author @liu
public class TutorAcademicoDAOTest {
    
    // Test of findTutoresAcademicosByName method, of class TutorAcademicoDAO.
    @Test
    public void testFindTutoresAcademicosByName() {
        System.out.println("findTutoresAcademicosByName");
        String searchName = "A";
        TutorAcademicoDAO tutorAcademicoDao = new TutorAcademicoDAO();
        List<Persona> expResult = new ArrayList<>();
        Persona tutorAcademico1 = new Persona(1,"MAX WILLIAM","MILLAN","MARTINEZ","2283407808","mmillan@uv.mx");
        expResult.add(tutorAcademico1);
        Persona tutorAcademico2 = new Persona(2,"ANGEL JUAN","SANCHEZ","GARCIA","2281394728","angesanchez@uv.mx");
        expResult.add(tutorAcademico2);
        List<Persona> result = tutorAcademicoDao.findTutoresAcademicosByName(searchName);
        boolean listasIguales = true;
        for (int i=0; i<result.size(); i++) {
            if (result.get(i).equals(expResult.get(i)) == false) {
                listasIguales = false;
            }
        }
        assertTrue(listasIguales);
    }
    
    // Test of findTutorAcademicoById method, of class TutorAcademicoDAO.
    @Test
    public void testFindTutorAcademicoById() {
        System.out.println("findTutorAcademicoById");
        int idTutorAcademico = 1;
        TutorAcademicoDAO tutorAcademicoDao = new TutorAcademicoDAO();
        Persona tutorAcademicoEsperado = new Persona(1,"ANGEL JUAN","SANCHEZ","GARCIA","2281394728","angesanchez@uv.mx");
        Persona tutorAcademicoObtenido = tutorAcademicoDao.findTutorAcademicoById(idTutorAcademico);
        assertTrue(tutorAcademicoEsperado.equals(tutorAcademicoObtenido));
    }
    
    // Test of addTutorAcademico method, of class TutorAcademicoDAO.
    @Test
    public void testAddTutorAcademico() {
        System.out.println("addTutorAcademico");
        Persona tutorAcademico = new Persona("TUTOR1","DE","PRUEBA","2281818181","tutor1@uv.mx");
        TutorAcademicoDAO tutorAcademicoDao = new TutorAcademicoDAO();
        boolean result = tutorAcademicoDao.addTutorAcademico(tutorAcademico);
        assertTrue(result);
    }

    // Test of deleteTutorAcademicoById method, of class TutorAcademicoDAO.
    @Test
    public void testDeleteTutorAcademicoById() {
        System.out.println("deleteTutorAcademicoById");
        int idTutorAcademico = 7;
        TutorAcademicoDAO tutorAcademicoDao = new TutorAcademicoDAO();
        boolean result = tutorAcademicoDao.deleteTutorAcademicoById(idTutorAcademico);
        assertFalse(result);
    }
}
