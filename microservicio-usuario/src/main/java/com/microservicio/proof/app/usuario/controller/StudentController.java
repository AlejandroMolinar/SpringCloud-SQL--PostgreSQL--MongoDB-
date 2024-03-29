package com.microservicio.proof.app.usuario.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.microservicio.proof.app.usuario.services.StudentService;
import com.microservicio.proof.commons.controllers.CommonController;
import com.microservicio.proof.commons.student.models.entity.Student;


//-----------------------------------------------------------------------------------
//	@RestController	->	Controla la Info y la envia en un Archivo JSon
//-----------------------------------------------------------------------------------

@RestController										// Controlador de Soporte	
public class StudentController extends CommonController<Student, StudentService>{

	
	@GetMapping("/students-by-courses")																// Buscar Estudiantes por nombre 
	public ResponseEntity<?> studentsByCourses(@RequestParam List<Long> ids){
		return ResponseEntity.ok(service.findAllById(ids));
	}

	
//-----------------------------------------------------------------------------------   	
	@PutMapping("/{id}")																			// Actualizar  estudiantes por ID
	public ResponseEntity<?> update(@Valid @RequestBody Student student, BindingResult result, @PathVariable Long id){
		if(result.hasErrors()) {
			return this.validate(result);
		}
		
		Optional<Student> opStu= service.findById(id);
		
		if (opStu.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Student studentDB= opStu.get();
		studentDB.setName(student.getName());
		studentDB.setLastName(student.getLastName());
		studentDB.setEmail(student.getEmail());
			
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(studentDB));
		
	}
//-----------------------------------------------------------------------------------   	
	@GetMapping("/search-name/{name}")																// Buscar Estudiantes por nombre 
	public ResponseEntity<?> searchName(@PathVariable String name){
		return ResponseEntity.ok(service.findByNameOrLastName(name));
	}

//-----------------------------------------------------------------------------------   
	@PostMapping("/create-by-image")																			// Actualizar  estudiantes por ID
	public ResponseEntity<?> createByImage(@Valid Student student, BindingResult result, 
			@RequestParam MultipartFile file) throws IOException {
		
		if(!file.isEmpty()) {
			student.setImage(file.getBytes());					// Devuelve un arreglo de Bytes y se lo pasa al estudente 
		}
		
		return super.create(student, result);
	}	

//-----------------------------------------------------------------------------------   
	@PutMapping("/update-by-image/{id}")																			// Actualizar  estudiantes por ID
	public ResponseEntity<?> updateByImage(@Valid Student student, BindingResult result, @PathVariable Long id,
			@RequestParam MultipartFile file) throws IOException{
		
		if(result.hasErrors()) {
			return this.validate(result);
		}
		
		Optional<Student> opStu= service.findById(id);
		
		if (opStu.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Student studentDB= opStu.get();
		studentDB.setName(student.getName());
		studentDB.setLastName(student.getLastName());
		studentDB.setEmail(student.getEmail());
			
		if(!file.isEmpty()) {
			studentDB.setImage(file.getBytes());					// Devuelve un arreglo de Bytes y se lo pasa al estudente 
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(studentDB));
		
	}
	
//-----------------------------------------------------------------------------------   	
	@GetMapping("/uploads/img/{id}")														// Buscar Estudiantes por nombre 
	public ResponseEntity<?> showImage(@PathVariable Long id){
		
		Optional<Student> opStu= service.findById(id);
		
		if (opStu.isEmpty() || opStu.get().getImage() == null) {
			return ResponseEntity.notFound().build();
		}
		Resource image= new ByteArrayResource(opStu.get().getImage());
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);			// Retorna un cuerpo de tipo JPEG y
																							// muestra la imagen de los Bytes en BBDD 
	}
	
}
 



