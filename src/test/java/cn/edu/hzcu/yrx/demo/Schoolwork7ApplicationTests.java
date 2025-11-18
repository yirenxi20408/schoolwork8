package cn.edu.hzcu.yrx.demo;

import cn.edu.hzcu.yrx.demo.exception.StudentNotFoundException;
import cn.edu.hzcu.yrx.demo.model.Student;
import cn.edu.hzcu.yrx.demo.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Schoolwork7ApplicationTests {

	@Autowired
	private StudentService studentService;

	@AfterEach
	void cleanup() {
		studentService.clearAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void testAddStudent() {
		Student student = new Student(null, "张三", "12345678901");
		Student saved = studentService.addStudent(student);
		
		assertNotNull(saved.getSid());
		assertEquals("张三", saved.getName());
		assertEquals("12345678901", saved.getTele());
	}

	@Test
	void testFindStudent() {
		Student student = new Student(null, "李四", "12345678902");
		Student saved = studentService.addStudent(student);
		
		Student found = studentService.findStudent(saved.getSid());
		
		assertEquals(saved.getSid(), found.getSid());
		assertEquals("李四", found.getName());
		assertEquals("12345678902", found.getTele());
	}

	@Test
	void testModifyStudent() {
		Student student = new Student(null, "王五", "12345678903");
		Student saved = studentService.addStudent(student);
		
		Student updateData = new Student(null, "王五修改", "98765432109");
		Student modified = studentService.modifyStudent(saved.getSid(), updateData);
		
		assertEquals(saved.getSid(), modified.getSid());
		assertEquals("王五修改", modified.getName());
		assertEquals("98765432109", modified.getTele());
	}

	@Test
	void testDeleteStudent() {
		Student student = new Student(null, "赵六", "12345678904");
		Student saved = studentService.addStudent(student);
		
		studentService.deleteStudent(saved.getSid());
		
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.findStudent(saved.getSid());
		});
	}

	@Test
	void testFindAllStudents() {
		studentService.addStudent(new Student(null, "学生1", "11111111111"));
		studentService.addStudent(new Student(null, "学生2", "22222222222"));
		studentService.addStudent(new Student(null, "学生3", "33333333333"));
		
		List<Student> students = studentService.findAll();
		
		assertEquals(3, students.size());
	}

	@Test
	void testStudentNotFound() {
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.findStudent(999L);
		});
	}

	@Test
	void testDeleteNonExistentStudent() {
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.deleteStudent(999L);
		});
	}

	@Test
	void testModifyNonExistentStudent() {
		Student updateData = new Student(null, "不存在", "00000000000");
		assertThrows(StudentNotFoundException.class, () -> {
			studentService.modifyStudent(999L, updateData);
		});
	}
}
