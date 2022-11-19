package M62;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SwimTest {
    Swim swim;
    @BeforeEach
    void set(){
        swim = new Swim();
    }
    @Test
    void priceTestWeak1() throws Exception {
        assertEquals(100,swim.getPrice(true,false,12,4,25));
        assertEquals(200,swim.getPrice(false,false,12,4,25));
    }
    @Test
    void priceTestWeak2() throws Exception {
        assertEquals(140,swim.getPrice(false,true,12,4,25));
        assertEquals(200,swim.getPrice(false,false,12,4,25));
    }
    @Test
    void priceTestWeak3() throws Exception {
        assertEquals(160,swim.getPrice(false,false,6,4,25));
        assertEquals(200,swim.getPrice(false,false,12,4,25));
    }
    @Test
    void priceTestWeak4() throws Exception {
        assertEquals(250,swim.getPrice(false,false,12,6,25));
        assertEquals(200,swim.getPrice(false,false,12,4,25));
    }
    @Test
    void priceTestWeak5() throws Exception {
        assertEquals(160,swim.getPrice(false,false,12,4,10));
        assertEquals(200,swim.getPrice(false,false,12,4,25));
    }
    @Test
    void priceTestStrong1() {
        assertThrows(Exception.class, ()-> {
            swim.getPrice(false, false, 4, 4, 25);
        });
        assertThrows(Exception.class, ()-> {
            swim.getPrice(false, false, 23, 4, 25);
        });

    }
    @Test
    void priceTestStrong2() {
        assertThrows(Exception.class, ()-> {
            swim.getPrice(false, false, 12, 4, 2);
        });
        assertThrows(Exception.class, ()-> {
            swim.getPrice(false, false, 12, 4, 76);
        });

    }
    @Test
    void priceTestStrong3() throws Exception {
        assertEquals(125,swim.getPrice(true,false,12,6,25));
        assertEquals(250,swim.getPrice(false,true,12,6,25));
        assertEquals(250,swim.getPrice(false,false,6,7,25));
        assertEquals(250,swim.getPrice(false,false,12,7,10));
    }
    @Test
    void priceTestStrong4() throws Exception {
        assertEquals(160,swim.getPrice(false,false,12,4,60));
    }
}
