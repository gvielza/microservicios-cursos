package com.formacionbdi.microservicios.app.cursos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.commons.alumnos.models.entity.Alumno;
import com.formacionbdi.microservicios.app.cursos.models.entity.Curso;
import com.formacionbdi.microservicios.app.cursos.services.CursoService;
import com.formacionbdi.microservicios.commons.controller.CommonController;
import com.microservicios.commons.examenes.models.entity.Examen;

@RestController
public class CursoController extends CommonController<Curso, CursoService> {
	@Value("${config.balanceador.text}")
	private String balanceadorTest;

	@GetMapping("/b-test")
	public ResponseEntity<?> balanceadorTest() {
Map<String, Object> response=new HashMap<String, Object>();
response.put("balanceador", balanceadorTest);
response.put("curso", service.findAll());
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
		if (result.hasErrors()) {
			return this.validar(result);
		}

		Optional<Curso> c = this.service.findById(id);
		if (!c.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Curso bdcurso = c.get();
		bdcurso.setNombre(curso.getNombre());
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(bdcurso));
	}

	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id) {
		Optional<Curso> c = this.service.findById(id);
		if (!c.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Curso bdcurso = c.get();
		alumnos.forEach(a -> {
			bdcurso.addAlumno(a);
		});
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(bdcurso));
	}

	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> eliminarAlumno(@RequestBody Alumno alumno, @PathVariable Long id) {
		Optional<Curso> c = this.service.findById(id);
		if (c.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Curso bdcurso = c.get();
		bdcurso.removeAlumno(alumno);

		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(bdcurso));
	}

	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> findCursoByAlumnoId(@PathVariable Long id) {
		// siempre el map retorna el objeto motificado
		// comunico de esta forma una sola vez con el microservicio para tener buen
		// perfomance
		Curso curso = service.findCursoByAlumnoId(id);
		if (curso != null) {
			List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdConRespuestasAlumnos(id);
			List<Examen> examenes = curso.getExamenes().stream().map(examen -> {
				if (examenesIds.contains(examen.getId())) {
					examen.setRespondido(true);
				}
				return examen;
			}).collect(Collectors.toList());
			curso.setExamenes(examenes);
		}
		return ResponseEntity.ok(curso);
	}

	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenes, @PathVariable Long id) {
		Optional<Curso> c = this.service.findById(id);
		if (c.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Curso bdcurso = c.get();
		examenes.forEach(e -> {
			bdcurso.addExamen(e);
		});
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(bdcurso));
	}

	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen(@RequestBody Examen examen, @PathVariable Long id) {
		Optional<Curso> c = this.service.findById(id);
		if (c.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Curso bdcurso = c.get();
		bdcurso.removeExamen(examen);

		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(bdcurso));
	}

}