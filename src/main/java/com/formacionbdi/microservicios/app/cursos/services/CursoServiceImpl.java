package com.formacionbdi.microservicios.app.cursos.services;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.models.repository.CursoRepository;
import com.formacionbdi.microservicios.commons.service.CommonServicesImplements;
@Service
public class CursoServiceImpl extends CommonServicesImplements<Curso, CursoRepository> implements CursoService {

	@Override
	@Transactional(readOnly = true)
	public Curso findCursoByAlumnoId(Long id) {
		
		return repository.findCursoByAlumnoId(id);
	}

	

}
