package JUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Demo0316Test {
    Demo0316 x;
    @BeforeEach
    void setUp() {
        x = new Demo0316();
        System.out.println("new Demo0316");
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAfterClass(){
        System.out.println("End Test");
    }

    @Test
    void add1() {
        assertEquals(0,x.add(13,-13));
    }
    @Test
    void add2() {
        assertEquals(120,x.add(10,110));
    }
    @Test
    void add3() {
        assertEquals(-2,x.add(-1,-1));
    }
    @Test
    void add4() {
        assertEquals(0,x.add(0,-0));
    }
}