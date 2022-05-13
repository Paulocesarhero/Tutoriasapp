package uv.fei.tutorias.bussinesslogic;

import org.apache.log4j.Logger;
import uv.fei.tutorias.dataaccess.DataBaseConnection;
import uv.fei.tutorias.domain.EstudiantesProblematicasAcademicas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudiantesProblematicasAcademicasDAO implements IEstudianteProblematicaAcademicaDAO{
    private final Logger LOG = Logger.getLogger(EstudiantesProblematicasAcademicasDAO.class);

    //este metodo se hizo con la finalidad de saber todos los estudiantes que tienen asignado una misma problematica academica
    @Override
    public List<EstudiantesProblematicasAcademicas> findEstudiantesProblematicasAcademicasById(int searchIdproblematicaAcademica) {
        ArrayList<EstudiantesProblematicasAcademicas> estudiantesproblematicaAcademica = new ArrayList<>();
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        try (Connection connection = dataBaseConnection.getConnection()) {
            String query = "select nombre, apellidoPaterno, apellidoMaterno, pa.descripcion from estudiantes_problematicasacademicas ep\n" +
                    "\tinner join estudiante e on e.id = ep.idEstudiante\n" +
                    "    inner join persona p on e.idPersona = p.id\n" +
                    "    inner join problematica_academica pa on pa.id = ep.idProblematicaAcademica\n" +
                    "    where ep.idProblematicaAcademica = (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, searchIdproblematicaAcademica);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new SQLException("No se ha encontrado la relacion estudiantes_problematicasacademicas con el id "  + searchIdproblematicaAcademica);
            } else {
                String nombre;
                String apellidoPaterno;
                String apellidoMaterno;
                String descripcion;
                do {
                    nombre = resultSet.getString("nombre");
                    apellidoPaterno = resultSet.getString("apellidoPaterno");
                    apellidoMaterno = resultSet.getString("apellidoMaterno");
                    descripcion = resultSet.getString("descripcion");

                    EstudiantesProblematicasAcademicas estudianteProblematicaAcademica = new EstudiantesProblematicasAcademicas();
                    estudianteProblematicaAcademica.getEstudiante().setNombre(nombre);
                    estudianteProblematicaAcademica.getEstudiante().setApellidoPaterno(apellidoPaterno);
                    estudianteProblematicaAcademica.getEstudiante().setApellidoMaterno(apellidoMaterno);
                    estudianteProblematicaAcademica.getProblematicaAcademica().setDescripcion(descripcion);

                    estudiantesproblematicaAcademica.add(estudianteProblematicaAcademica);
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            LOG.warn(EstudiantesProblematicasAcademicasDAO.class.getName(), ex);
        } finally {
            dataBaseConnection.cerrarConexion();
        }
        return estudiantesproblematicaAcademica;

    }

    @Override
    public boolean addEstudiantesProblematicasAcademicas(EstudiantesProblematicasAcademicas estudiantesProblematicasAcademicas) {
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        boolean bandera = false;
        try (Connection connection = dataBaseConnection.getConnection()) {
            String query = "INSERT INTO estudiantes_problematicasacademicas(idEstudiante,idProblematicaAcademica) VALUES(?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, estudiantesProblematicasAcademicas.getIdEstudiante());
            statement.setInt(2, estudiantesProblematicasAcademicas.getIdProblematicaAcademica());
            int executeUpdate = statement.executeUpdate();
            if (executeUpdate == 0){
                throw new SQLException("ERROR: No se ha agregado ningun estudiantes_problematicasAcademicas");
            }else {
                bandera = true;
            }
        } catch (SQLException ex) {
            LOG.warn(PersonaDAO.class.getName(), ex);
        } finally {
            dataBaseConnection.cerrarConexion();
        }
        return bandera;
    }

    @Override
    public boolean deleteEstudinatesProblematicasAcademicasById(int searchId) {
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        boolean bandera = false;
        try (Connection connection = dataBaseConnection.getConnection()) {
            String query = "DELETE FROM estudiantes_problematicasacademicas WHERE (id = ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, searchId);
            int executeUpdate = statement.executeUpdate();
            if (executeUpdate == 0) {
                throw new SQLException("ERROR: No se ha eliminado ninguna relacion con estudiantes-problematicasAcademicas con el id " + searchId);
            }else {
                bandera = true;
            }
        } catch (SQLException ex) {
            LOG.warn(PersonaDAO.class.getName(), ex);
        } finally {
            dataBaseConnection.cerrarConexion();
        }
        return bandera;
    }
}