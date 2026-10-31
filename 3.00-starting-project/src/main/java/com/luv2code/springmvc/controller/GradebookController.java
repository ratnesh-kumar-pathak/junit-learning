package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;

	@Autowired
	private StudentAndGradeService studentService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		List<CollegeStudent> students = studentService.getGradeBook();
		m.addAttribute("students", students);
		return "index";
	}


	@GetMapping("/studentInformation/{id}") public String studentInformation(@PathVariable int id, Model m) {
		if(studentService.isStudentNull(id) == null){
			return "error";
		}

		studentService.configureModel(id, m);

		return "studentInformation";
	}

	@PostMapping("/")
	public String createStudent(@ModelAttribute("student") CollegeStudent student, Model m) {
		studentService.createStudent(
				student.getFirstname(),
				student.getLastname(),
				student.getEmailAddress()
		);
		m.addAttribute("students", studentService.getGradeBook());
		return "index";
	}

	@GetMapping("/delete/student/{id}")
	public String deleteStudent(@PathVariable int id, Model m) {
		if(studentService.isStudentNull(id) == null){
			return "error";
		}
		studentService.deleteStudent(id);
		m.addAttribute("students", studentService.getGradeBook());
		return "index";
	}

	@PostMapping("/grades")
	public String createGrade(
			@RequestParam("grade") double grade,
			@RequestParam("studentId") int id,
			@RequestParam("gradeType") String type,
			Model m
			) {
		if(studentService.isStudentNull(id) == null || !studentService.createGrade(grade, id, type)){
			return "error";
		}

		studentService.configureModel(id, m);

		return "studentInformation";
	}

	@GetMapping("/grades/{id}/{gradeType}")
	public String deleteGrade(@PathVariable("id") int id, @PathVariable("gradeType") String type, Model m) {
		if(studentService.deleteGrade(id, type) == -1){
			return "error";
		}
		studentService.configureModel(id, m);
		return "studentInformation";
	}

}
