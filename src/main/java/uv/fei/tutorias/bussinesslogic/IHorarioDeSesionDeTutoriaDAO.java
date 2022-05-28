package uv.fei.tutorias.bussinesslogic;

import uv.fei.tutorias.domain.HorarioDeSesionDeTutoria;
import uv.fei.tutorias.domain.Persona;

import java.util.List;

public interface IHorarioDeSesionDeTutoriaDAO {
    public boolean deleteHorarioDeSesionDeTutoria(int searchId);
    public boolean addHorarioDeSesionDeTutoria(HorarioDeSesionDeTutoria horarioDeSesionDeTutoria);
}
