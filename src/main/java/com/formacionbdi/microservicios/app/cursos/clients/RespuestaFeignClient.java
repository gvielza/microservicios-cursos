package com.formacionbdi.microservicios.app.cursos.clients;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(name = "microservicio-respuestas")
public interface RespuestaFeignClient {
	
	@GetMapping("/alumno/{alumnoId}/examenes-respondidos")
	public Iterable<Long>obtenerExamenesIdConRespuestasAlumnos( Long alumnoId);

}
