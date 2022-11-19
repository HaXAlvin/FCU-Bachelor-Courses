package Quiz2;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GradesTest {
    Grades g;

    @BeforeEach
    void set() {
        g = new Grades();
    }

    @Test
    void minus() throws Exception {
        assertThrows(Exception.class, () -> g.averageScore(
                new int[]{60, 50, -30},
                new int[]{30, 10, 50}
        ));
        assertThrows(Exception.class, () -> g.averageScore(
                new int[]{60, 50, 30},
                new int[]{30, 10, -50}
        ));
    }
    @Test
    void good() throws Exception{
        assertArrayEquals(
                new double[]{60,70},
                g.averageScore(
                        new int[]{50, 60, 70},
                        new int[]{60, 70, 80}
                )
        );
        assertArrayEquals(
                new double[]{0,70},
                g.averageScore(
                        new int[]{0, 0, 0},
                        new int[]{60, 70, 80}
                )
        );
    }
    @Test
    void empty() throws Exception{
        assertThrows(Exception.class,() -> g.averageScore(
                new int[]{},
                new int[]{1,2,3}
        ));
        assertThrows(Exception.class,() -> g.averageScore(
                new int[]{1,2,3},
                new int[]{}
        ));
        assertThrows(Exception.class,() -> g.averageScore(
                new int[]{},
                new int[]{}
        ));
    }
    @Test
    void tooMuch() throws Exception{
        assertThrows(Exception.class, () -> g.averageScore(
                new int[]{60, 50, 101},
                new int[]{30, 10, 50}
        ));
        assertThrows(Exception.class, () -> g.averageScore(
                new int[]{60, 50, 0},
                new int[]{30, 10, 101}
        ));
    }
}
