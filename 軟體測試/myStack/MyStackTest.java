import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MyStackTest {
    MyStack stack;
    @BeforeEach
    void set(){
        stack = null;
    }
    @org.junit.jupiter.api.Test
    void push() throws Exception {
        stack = new MyStack(3);
        stack.push(10);
        stack.push(10);
        stack.push(10);
        assertThrows(Exception.class,()->stack.push(10));
    }

    @org.junit.jupiter.api.Test
    void pop() throws Exception {
       stack = new MyStack(3);
       assertThrows(Exception.class,()->stack.pop());
       stack.push(10);
       assertEquals(10,stack.pop());
        stack.push(10);
        stack.push(20);
        assertEquals(20,stack.pop());

    }
    @org.junit.jupiter.api.Test
    void initSize(){
        assertThrows(Exception.class,()->stack = new MyStack(0));
        assertThrows(Exception.class,()->stack = new MyStack(1));
    }
}