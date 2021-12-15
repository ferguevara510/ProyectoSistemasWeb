select e.folioExamen as folioExamen, e.fin as fin, e.inicio as inicio, e.nombre as nombre, a.calificacion as calificacion, a.realizado as realizado from examen e, examen_alumno a where e.folioExamen = a.folioExamen and a.matricula = ?
select *, (select calificacion from examen_alumno a where a.matricula = 'zS19030170' and e.folioExamen = a.folioExamen) as calificacion, (select realizado from examen_alumno a where a.matricula = 'zS19030170' and e.folioExamen = a.folioExamen) as realizado from examen e;
select count(*) from respuesta r, respuesta_pregunta p where r.correcto = true and matricula = 'zS19030170' and r.id = p.idRespuesta;