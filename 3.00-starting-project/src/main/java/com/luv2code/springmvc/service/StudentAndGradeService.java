package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDAO;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    private ScienceGradesDao scienceGradesDao;

    @Autowired
    private StudentGrades studentGrades;

    public void createStudent(String firstName, String lastName, String email) {
        CollegeStudent student = new CollegeStudent(firstName, lastName, email);
        student.setId(0);
        studentDAO.save(student);
    }

    public CollegeStudent isStudentNull(int i) {
        Optional<CollegeStudent> collegeStudent = studentDAO.findById(i);
        return collegeStudent.orElse(null);
    }

    public void deleteStudent(int i) {
        CollegeStudent student = studentDAO.findById(i).orElse(null);
        if(student != null){
            mathGradeDao.deleteByStudentId(i);
            historyGradeDao.deleteByStudentId(i);
            scienceGradesDao.deleteByStudentId(i);
            studentDAO.deleteById(i);
        }
    }

    public List<CollegeStudent> getGradeBook() {
        return new ArrayList<>((Collection) studentDAO.findAll());
    }

    public boolean createGrade(double grade, int id, String type) {
        if(grade < 0.0 || grade > 100.0 || isStudentNull(id) == null ){
            return false;
        }

        System.out.println("\nCreate Grade " + grade + " " + id + " " + type + "\n");

        if(type.equals("math")){
            mathGrade.setGrade(grade);
            mathGrade.setId(0);
            mathGrade.setStudentId(id);
            mathGradeDao.save(mathGrade);
            return true;
        } else if(type.equals("science")){
            scienceGrade.setGrade(grade);
            scienceGrade.setId(0);
            scienceGrade.setStudentId(id);
            scienceGradesDao.save(scienceGrade);
            return true;
        } else if(type.equals("history")){
            historyGrade.setGrade(grade);
            historyGrade.setId(0);
            historyGrade.setStudentId(id);
            historyGradeDao.save(historyGrade);
            return true;
        }
        return false;
    }

    public int deleteGrade(int id, String type) {
        if(type.equals("math")){
            if(!mathGradeDao.findById(id).isPresent()){
                return -1;
            }
            mathGradeDao.deleteById(id);
        } else if(type.equals("science")){
            if(!scienceGradesDao.findById(id).isPresent()){
                return -1;
            }
            scienceGradesDao.deleteById(id);
        }  else if(type.equals("history")){
            if(!historyGradeDao.findById(id).isPresent()){
                return -1;
            }
            historyGradeDao.deleteById(id);
        } else {
            return -1;
        }
        return id;
    }

    public GradebookCollegeStudent studentInformation(int id) {
        Optional<CollegeStudent> collegeStudent = studentDAO.findById(id);
        if(!collegeStudent.isPresent()){
            return null;
        }
        CollegeStudent student = collegeStudent.get();
        List<Grade> mathGrades = new ArrayList<>(
                (Collection<MathGrade>) mathGradeDao.findGradeByStudentId(id)
        );
        List<Grade> historyGrades = new ArrayList<>(
                (Collection<HistoryGrade>) historyGradeDao.findGradeByStudentId(id)
        );
        List<Grade> scienceGrades = new ArrayList<>(
                (Collection<ScienceGrade>) scienceGradesDao.findGradeByStudentId(id)
        );

        studentGrades.setHistoryGradeResults(historyGrades);
        studentGrades.setScienceGradeResults(scienceGrades);
        studentGrades.setMathGradeResults(mathGrades);

        return new GradebookCollegeStudent(
                student.getId(),
                student.getFirstname(),
                student.getLastname(),
                student.getEmailAddress(),
                studentGrades
        );
    }

    public void configureModel(int id, Model m) {
        GradebookCollegeStudent info = studentInformation(id);
        m.addAttribute("student", info);

        if(!info.getStudentGrades().getMathGradeResults().isEmpty()){
            m.addAttribute("mathAverage", info.getStudentGrades().findGradePointAverage(
                    info.getStudentGrades().getMathGradeResults()
            ));
        } else {
            m.addAttribute("mathAverage", "N/A");
        }

        if(!info.getStudentGrades().getHistoryGradeResults().isEmpty()){
            m.addAttribute("historyAverage", info.getStudentGrades().findGradePointAverage(
                    info.getStudentGrades().getHistoryGradeResults()
            ));
        }  else {
            m.addAttribute("historyAverage", "N/A");
        }

        if(!info.getStudentGrades().getScienceGradeResults().isEmpty()){
            m.addAttribute("scienceAverage", info.getStudentGrades().findGradePointAverage(
                    info.getStudentGrades().getScienceGradeResults()
            ));
        }  else {
            m.addAttribute("scienceAverage", "N/A");
        }
    }
}












