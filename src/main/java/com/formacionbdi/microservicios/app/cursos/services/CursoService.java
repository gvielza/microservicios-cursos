package com.formacionbdi.microservicios.app.cursos.services;



import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.commons.service.CommonService;

public interface CursoService extends CommonService<Curso>{
	public Curso findCursoByAlumnoId(Long id);
	public Iterable<Long> obtenerExamenesIdConRespuestasAlumnos(Long alumnoId);

}
