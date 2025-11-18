package cn.edu.hzcu.yrx.demo.service;

import cn.edu.hzcu.yrx.demo.exception.StudentNotFoundException;
import cn.edu.hzcu.yrx.demo.model.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudentService集成测试
 * 展示IoC和AOP功能：
 * 1. IoC: 通过@Autowired注入StudentService（由Spring容器管理）
 * 2. AOP: 每个方法调用都会触发LoggingAspect的日志记录
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService.clearAll();
    }

    @Test
    @Order(1)
    @DisplayName("测试IoC: StudentService自动注入")
    void testIoCInjection() {
        assertNotNull(studentService, "StudentService应该通过IoC容器自动注入");
    }

    @Test
    @Order(2)
    @DisplayName("测试AOP: addStudent操作日志")
    void testAddStudentWithAOPLogging() {
        System.out.println("\n========== 测试添加学生 ==========");
        Student student = new Student(null, "测试学生", "13800138000");
        Student result = studentService.addStudent(student);
        
        assertNotNull(result.getSid());
        assertEquals("测试学生", result.getName());
        System.out.println("添加成功，学生ID: " + result.getSid());
    }

    @Test
    @Order(3)
    @DisplayName("测试AOP: findStudent操作日志")
    void testFindStudentWithAOPLogging() {
        System.out.println("\n========== 测试查询学生 ==========");
        Student saved = studentService.addStudent(new Student(null, "查询测试", "13900139000"));
        
        Student found = studentService.findStudent(saved.getSid());
        
        assertEquals(saved.getSid(), found.getSid());
        assertEquals("查询测试", found.getName());
        System.out.println("查询成功，学生信息: " + found.getName() + ", " + found.getTele());
    }

    @Test
    @Order(4)
    @DisplayName("测试AOP: modifyStudent操作日志")
    void testModifyStudentWithAOPLogging() {
        System.out.println("\n========== 测试修改学生 ==========");
        Student saved = studentService.addStudent(new Student(null, "原始姓名", "13700137000"));
        
        Student updateData = new Student(null, "修改后姓名", "13700137001");
        Student modified = studentService.modifyStudent(saved.getSid(), updateData);
        
        assertEquals("修改后姓名", modified.getName());
        assertEquals("13700137001", modified.getTele());
        System.out.println("修改成功，新信息: " + modified.getName() + ", " + modified.getTele());
    }

    @Test
    @Order(5)
    @DisplayName("测试AOP: deleteStudent操作日志")
    void testDeleteStudentWithAOPLogging() {
        System.out.println("\n========== 测试删除学生 ==========");
        Student saved = studentService.addStudent(new Student(null, "待删除", "13600136000"));
        
        studentService.deleteStudent(saved.getSid());
        
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.findStudent(saved.getSid());
        });
        System.out.println("删除成功，学生ID: " + saved.getSid());
    }

    @Test
    @Order(6)
    @DisplayName("测试自定义异常: 访问不存在的学生")
    void testCustomExceptionForInvalidAccess() {
        System.out.println("\n========== 测试自定义错误提示 ==========");
        
        StudentNotFoundException exception = assertThrows(
            StudentNotFoundException.class,
            () -> studentService.findStudent(999L)
        );
        
        assertTrue(exception.getMessage().contains("Student with ID 999 not found"));
        System.out.println("错误提示: " + exception.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("测试完整的CRUD流程")
    void testCompleteCRUDFlow() {
        System.out.println("\n========== 测试完整CRUD流程 ==========");
        
        // Create
        Student student = studentService.addStudent(new Student(null, "完整测试", "13500135000"));
        System.out.println("1. 创建学生: " + student.getName() + " (ID: " + student.getSid() + ")");
        
        // Read
        Student found = studentService.findStudent(student.getSid());
        System.out.println("2. 查询学生: " + found.getName() + ", " + found.getTele());
        
        // Update
        Student updated = studentService.modifyStudent(
            student.getSid(), 
            new Student(null, "修改后", "13500135001")
        );
        System.out.println("3. 修改学生: " + updated.getName() + ", " + updated.getTele());
        
        // Delete
        studentService.deleteStudent(student.getSid());
        System.out.println("4. 删除学生: ID " + student.getSid());
        
        // Verify deletion
        assertThrows(StudentNotFoundException.class, () -> {
            studentService.findStudent(student.getSid());
        });
        System.out.println("5. 验证删除: 学生已不存在");
    }
}
