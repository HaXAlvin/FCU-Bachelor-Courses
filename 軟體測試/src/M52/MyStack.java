package M52;
public class MyStack {
    private final int[] data;
    private final int size;
    private int index = 0;
    public MyStack(int size) throws Exception{
        if (size<1){
            throw new Exception("Not a stack");
        }
        this.data = new int[size];
        this.size = size;
    }
    public void push(int addData) throws Exception{
        if (is_full())
            throw new Exception("Full");
        data[index++] = addData;
    }
    public int pop() throws Exception{
        if (is_empty())
            throw new Exception("Empty");
        return data[--index];
    }
    public boolean is_full(){
        return size == index;
    }
    public boolean is_empty(){
        return index==0;
    }

}
