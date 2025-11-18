package cn.edu.hzcu.yrx.demo.service;

import cn.edu.hzcu.yrx.demo.exception.StudentNotFoundException;
import cn.edu.hzcu.yrx.demo.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StudentService {

    private final List<Student> students = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Student> findAll() {
        return new ArrayList<>(students);
    }

    public Student addStudent(Student student) {
        if (student.getSid() == null) {
            student.setSid(idGenerator.getAndIncrement());
        }
        students.add(student);
        return student;
    }

    public Student findStudent(Long sid) {
        return students.stream()
                .filter(s -> s.getSid().equals(sid))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Student with ID " + sid + " not found"));
    }

    public Student modifyStudent(Long sid, Student updatedStudent) {
        Student student = findStudent(sid);
        student.setName(updatedStudent.getName());
        student.setTele(updatedStudent.getTele());
        return student;
    }

    public void deleteStudent(Long sid) {
        Student student = findStudent(sid);
        students.remove(student);
    }

    public Student save(Student s) {
        return addStudent(s);
    }

    public void clearAll() {
        students.clear();
        idGenerator.set(1);
    }
}