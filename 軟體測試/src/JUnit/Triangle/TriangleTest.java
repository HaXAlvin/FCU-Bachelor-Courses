package JUnit.Triangle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    @Test
    void setTriangleType() throws Exception {
        assertThrows(Exception.class, () -> new Triangle(0,0,0).setTriangleType());
        assertThrows(Exception.class, () -> new Triangle(-1,5,9).setTriangleType());
        assertThrows(Exception.class, () -> new Triangle(3,3,6).setTriangleType());
        assertThrows(Exception.class, () -> new Triangle(1,2,3).setTriangleType());
        assertEquals("正三角形",new Triangle(1, 1, 1).setTriangleType().type);
        assertEquals("等腰三角形",new Triangle(5, 5, 1).setTriangleType().type);
        assertEquals("直角三角形",new Triangle(3, 4, 5).setTriangleType().type);
        assertEquals("等腰直角三角形",new Triangle(1, 1, Math.pow(2,0.5)).setTriangleType().type);
        assertEquals("一般三角形",new Triangle(3, 5, 7).setTriangleType().type);
        System.out.println("setTriangleType() Test End");
    }

    @Test
    void checkUpright() throws Exception {
        assertTrue(new Triangle(1, 1, 1).checkUpright());
        assertTrue(new Triangle(2, 2, 2).checkUpright());
        assertFalse(new Triangle(0, 0, 0).checkUpright());
        assertFalse(new Triangle(-1,-1,-1).checkUpright());
        assertFalse(new Triangle(-1,-2,-3).checkUpright());
        assertFalse(new Triangle(1,2,3).checkUpright());
        System.out.println("checkUpright() Test End");
    }

    @Test
    void checkSameAndNinety() throws Exception {
        assertTrue(new Triangle(1,1,Math.pow(2,0.5)).checkSameAndNinety());
        assertTrue(new Triangle(1,1,1.41421356237).checkSameAndNinety());
        assertFalse(new Triangle(0,0,3).checkSameAndNinety());
        assertFalse(new Triangle(1,1,2).checkSameAndNinety());
        System.out.println("checkSameAndNinety() Test End");
    }

    @Test
    void checkNinety() throws Exception {
        assertTrue(new Triangle(3,4,5).checkNinety());
        assertTrue(new Triangle(5,12,13).checkNinety());
        assertFalse(new Triangle(1,2,3).checkNinety());
        assertFalse(new Triangle(1,1,7).checkNinety());
        assertFalse(new Triangle(-1,-2,-3).checkNinety());
        System.out.println("checkNinety() Test End");
    }

    @Test
    void checkTwoSameLine() throws Exception {
        assertFalse(new Triangle(3,3,6).checkTwoSameLine());
        assertFalse(new Triangle(3,3,7).checkTwoSameLine());
        assertTrue(new Triangle(3,3,5.5).checkTwoSameLine());
        System.out.println("checkTwoSameLine() Test End");
    }

    @Test
    void checkNormal() throws Exception {
        assertTrue(new Triangle(3,4,5).checkNormal());
        assertTrue(new Triangle(5,12,13).checkNormal());
        assertFalse(new Triangle(1,2,3).checkNormal());
        assertFalse(new Triangle(1,1,7).checkNormal());
        assertFalse(new Triangle(-1,-2,-3).checkNormal());
        assertTrue(new Triangle(1, 1, 1).checkNormal());
        assertTrue(new Triangle(2, 2, 2).checkNormal());
        assertFalse(new Triangle(0, 0, 0).checkNormal());
        assertFalse(new Triangle(-1,-1,-1).checkNormal());
        assertFalse(new Triangle(-1,-2,-3).checkNormal());
        assertFalse(new Triangle(1,2,3).checkNormal());
        System.out.println("checkNormal() Test End");
    }

    @Test
    void checkNotTri() throws Exception {
        assertTrue(new Triangle(0,0,0).checkNotTri());
        assertTrue(new Triangle(-1,5,6).checkNotTri());
        assertTrue(new Triangle(1,2,3).checkNotTri());
        assertTrue(new Triangle(3,3,6).checkNotTri());
        System.out.println("checkNotTri() Test End");
    }

    @Test
    void pythagorean() throws Exception {
        assertTrue(new Triangle(0,0,0).pythagorean(3.0,4.0,5.0));
        assertFalse(new Triangle(0,0,0).pythagorean(3.0,4.0,4.9));
        assertFalse(new Triangle(0,0,0).pythagorean(3.0,4.0,4.999));
        assertTrue(new Triangle(0,0,0).pythagorean(3.0,4.0,4.99999999999));
        System.out.println("pythagorean() Test End");
    }

}