package uv.fei.tutorias.bussinesslogic;

import dataaccess.ConexionBD;
import domain.Persona;
import domain.TutorAcademico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// author @liu
public class TutorAcademicoDAO implements ITutorAcademicoDAO {
    private final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(PersonaDAO.class);

    @Override
    public ArrayList<TutorAcademico> obtenerTutoresAcademicos() throws SQLException {
        ArrayList<TutorAcademico> tutores = new ArrayList<>();
        String consulta = 
        "SELECT TA.idTutorAcademico, P.* " +
        "FROM TutorAcademico TA INNER JOIN Persona P ON P.idPersona = TA.idPersona";
        ConexionBD baseDeDatos = new ConexionBD();
        try (Connection conexion = baseDeDatos.abrirConexion()) {
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            if (!resultado.next()) {
                throw new SQLException("No se han encontrado tutores academicos");
            } else {
                do {
                    tutores.add(getTutorAcademico(resultado));
                } while (resultado.next());
            }
        } finally {
            baseDeDatos.cerrarConexion();
        }
        return tutores;
    }

    @Override
    public TutorAcademico obtenerTutorAcademicoPorId(int idTutorAcademico) throws SQLException {
        TutorAcademico tutorAcademico = new TutorAcademico();
        String consulta = 
        "SELECT TA.idTutorAcademico, P.* " +
        "FROM TutorAcademico TA LEFT JOIN Persona P ON P.idPersona = TA.idPersona " +
        "WHERE idTutorAcademico = ?";
        ConexionBD baseDeDatos = new ConexionBD();
        try (Connection conexion = baseDeDatos.abrirConexion()) {
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idTutorAcademico);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next() == false) {
                throw new SQLException("No se ha encontrado el tutor academico con el id " + idTutorAcademico);
            } else {
                tutorAcademico = getTutorAcademico(resultado);
            }
        } finally {
            baseDeDatos.cerrarConexion();
        }
        return tutorAcademico;
    }

    private TutorAcademico getTutorAcademico(ResultSet resultado) throws SQLException {
        int idTutorAcademico;
        String nombre;
        String apellidoPaterno;
        String apellidoMaterno;
        
        idTutorAcademico = resultado.getInt("idTutorAcademico");
        nombre = resultado.getString("nombre");
        apellidoPaterno = resultado.getString("apellidoPaterno");
        apellidoMaterno = resultado.getString("apellidoMaterno");
        
        Persona personaCoordinador = new Persona(nombre,apellidoPaterno,apellidoMaterno);
        TutorAcademico tutorAcademico = new TutorAcademico(idTutorAcademico,personaCoordinador);
        return tutorAcademico;
    }

    @Override
    public boolean agregarTutorAcademico(TutorAcademico tutorAcademico) throws SQLException {
        boolean validacion = false;
        String consulta = "INSERT INTO tutor_academico(idPersona,idUsuario) VALUES(?,?)";
        ConexionBD baseDeDatos = new ConexionBD();
        try (Connection conexion = baseDeDatos.abrirConexion()) {
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, tutorAcademico.getIdPersona());
            sentencia.setInt(2, tutorAcademico.getUsuario().getId());
            int columnasAfectadas = sentencia.executeUpdate();
            if(columnasAfectadas == 0) {
                throw new SQLException("ERROR: El coordinador no se ha agregado");
            } else {
                validacion = true;
            }
        } finally {
            baseDeDatos.cerrarConexion();
        }
        return validacion;
    }

    @Override
    public boolean eliminarTutorAcademicoPorId(int idTutorAcademico) throws SQLException {
        boolean validacion = false;
        String consulta = "DELETE FROM tutor_academico WHERE id = ?";
        ConexionBD baseDeDatos = new ConexionBD();
        try (Connection conexion = baseDeDatos.abrirConexion()) {
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idTutorAcademico);
            int columnasAfectadas = sentencia.executeUpdate();
            if(columnasAfectadas == 0) {
                throw new SQLException("ERROR: No se ha eliminado el tutor académico con el id " + idTutorAcademico);
            } else {
                validacion = true;
            }
        } finally {
            baseDeDatos.cerrarConexion();
        }
        return validacion;
    }
    
    @Override
    public boolean modificarTutorAcademico(TutorAcademico tutorAcademico) throws SQLException {
        boolean validacion = false;
        String consulta = 
                "UPDATE tutor_academico " + 
                "SET idPersona = ?, " +
                "SET idUsuario = ? " +
                "WHERE id = ?";
        ConexionBD baseDeDatos = new ConexionBD();
        try(Connection conexion = baseDeDatos.abrirConexion()) {
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, tutorAcademico.getIdPersona());
            sentencia.setInt(2, tutorAcademico.getUsuario().getId());
            sentencia.setInt(3, tutorAcademico.getId());
            int columnasAfectadas = sentencia.executeUpdate();
            if(columnasAfectadas == 0) {
                throw new SQLException("ERROR: No se ha modificado el tutor academico con el id " + tutorAcademico.getId());
            }
            validacion = true;
        } finally {
            baseDeDatos.cerrarConexion();
        }
        return validacion;
    }
}