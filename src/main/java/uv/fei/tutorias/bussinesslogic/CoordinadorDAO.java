package uv.fei.tutorias.bussinesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import uv.fei.tutorias.dataaccess.DataBaseConnection;
import uv.fei.tutorias.domain.Coordinador;
import uv.fei.tutorias.domain.Persona;

// author @liu
public class CoordinadorDAO implements ICoordinadorDAO {

    private final Logger LOGGER = Logger.getLogger(PersonaDAO.class);

    @Override
    public ArrayList<Coordinador> findCoordinadorByName(String searchName) {
        ArrayList<Coordinador> coordinadores = new ArrayList<>();
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        try(Connection connection = dataBaseConnection.getConnection()) {
            String query = "SELECT * FROM Coordinador C LEFT JOIN Persona P ON P.idPersona = C.idPersona WHERE CONCAT(P.nombre,\" \", P.apellidoPaterno,\" \",P.apellidoMaterno) LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchName + "%");
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() == false) {
                throw new SQLException("Coordinador not found");
            } else {
                do {
                    coordinadores.add(getCoordinador(resultSet));
                }while(resultSet.next());
            }
        } catch(SQLException ex) {
            LOGGER.error(CoordinadorDAO.class.getName(),ex);
        } finally {
            dataBaseConnection.cerrarConexion();
            return coordinadores;
        }
    }

    @Override
    public Coordinador findCoordinadorById(int idCoordinador) {
        Coordinador coordinador = new Coordinador();
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        try(Connection connection = dataBaseConnection.getConnection()) {
            String query = "SELECT * FROM Coordinador C LEFT JOIN Persona P ON P.idPersona = C.idPersona WHERE idCoordinador = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idCoordinador);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next() == false) {
                throw new SQLException("Coordinador not found");
            }
            coordinador = getCoordinador(resultSet);
        } catch(SQLException ex) {
            LOGGER.error(CoordinadorDAO.class.getName(),ex);
        } finally {
            dataBaseConnection.cerrarConexion();
            return coordinador;
        }
    }

    @Override
    public Coordinador getCoordinador(ResultSet resultSet) {
        int idCoordinador = 0;
        int idPersonaCoordinador = 0;
        String nombreCoordinador = "";
        String apellidoPaternoCoordinador = "";
        String apellidoMaternoCoordinador = "";
        String correoInstitucionalCoordinador = "";
        String correoPersonalCoordinador = "";
        int idProgramaEducativo = 0;
        try {
            idCoordinador = resultSet.getInt("idCoordinador");
            idPersonaCoordinador = resultSet.getInt("idPersona");
            nombreCoordinador = resultSet.getString("nombre");
            apellidoPaternoCoordinador = resultSet.getString("apellidoPaterno");
            apellidoMaternoCoordinador = resultSet.getString("apellidoMaterno");
            correoInstitucionalCoordinador = resultSet.getString("correoInstitucional");
            correoPersonalCoordinador = resultSet.getString("correoPersonal");
            idProgramaEducativo = resultSet.getInt("idProgramaEducativo");
        } catch(SQLException ex) {
            LOGGER.error(CoordinadorDAO.class.getName(),ex);
        }
        Persona personaCoordinador = new Persona(idPersonaCoordinador,nombreCoordinador,apellidoPaternoCoordinador,apellidoMaternoCoordinador,correoInstitucionalCoordinador,correoPersonalCoordinador);
        Coordinador coordinador = new Coordinador(idCoordinador,personaCoordinador,idProgramaEducativo);
        return coordinador;
    }

    @Override
    public boolean addCoordinador(Coordinador coordinador) {
        PersonaDAO personaDao = new PersonaDAO();
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        boolean result = false;
        try(Connection connection = dataBaseConnection.getConnection()) {
            String query = "INSERT INTO Coordinador (idProgramaEducativo, idPersona) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, coordinador.getIdProgramaEducativo());
            statement.setInt(2, personaDao.addPersonaReturnId(coordinador.getPersona()));
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("ERROR: Coordinador not added");
            }
            result = true;
        } catch(SQLException ex) {
            LOGGER.error(CoordinadorDAO.class.getName(),ex);
        } finally {
            dataBaseConnection.cerrarConexion();
            return result;
        }
    }

    @Override
    public boolean deleteCoordinadorById(int idCoordinador) {
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        boolean result = false;
        try(Connection connection = dataBaseConnection.getConnection()) {
            String query = "DELETE FROM Coordinador WHERE idCoordinador = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idCoordinador);
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0) {
                throw new SQLException("ERROR: Coordinador not deleted");
            }
            result = true;
        } catch(SQLException ex) {
            LOGGER.error(CoordinadorDAO.class.getName(),ex);
        } finally {
            dataBaseConnection.cerrarConexion();
            return result;
        }
    }
}