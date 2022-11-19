package M52;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStackTest {
    MyStack stack;
    @BeforeEach
    void set(){
        stack = null;
    }
    @Test
    void push() throws Exception {
        stack = new MyStack(3);
        stack.push(10);
        stack.push(10);
        stack.push(10);
        assertThrows(Exception.class,()->stack.push(10));
    }

    @Test
    void pop() throws Exception {
        stack = new MyStack(3);
        assertThrows(Exception.class,()->stack.pop());
        stack.push(10);
        assertEquals(10,stack.pop());
        stack.push(10);
        stack.push(20);
        assertEquals(20,stack.pop());

    }
    @Test
    void initSize() throws Exception {
        assertThrows(Exception.class,()->stack = new MyStack(0));
        assertThrows(Exception.class,()->stack = new MyStack(-1));
        stack = new MyStack(1);
        stack.push(100);
        assertEquals(100,stack.pop());
    }
    @Test
    void fullAndEmpty() throws Exception{
        stack = new MyStack(5);
        assertFalse(stack.is_full());
        assertTrue(stack.is_empty());
        stack.push(10);
        assertFalse(stack.is_full());
        assertFalse(stack.is_empty());
        stack.pop();
        assertTrue(stack.is_empty());
        stack.push(1);
        stack.push(2);
        stack.push(-1);
        stack.push(999);
        stack.push(0);
        assertTrue(stack.is_full());
        assertFalse(stack.is_empty());
    }
}