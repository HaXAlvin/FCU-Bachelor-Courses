public class MyStack {
    private final int[] data;
    int size;
    boolean is_full;
    boolean is_empty;
    int len;
    public MyStack(int size) throws Exception{
        if (size<2){
            throw new Exception("Not a stack");
        }
        this.data = new int[size];
        this.size = size;
        this.is_empty = true;
        this.is_full = false;
        this.len = 0;
    }



    public void push(int addData) throws Exception{
        if (is_full)
            throw new Exception("Full");
        this.data[this.len++] = addData;
        is_empty = false;
        if (len==size){
            is_full = true;
        }
    }
    public int pop() throws Exception{
        if (is_empty)
            throw new Exception("Empty");
        int returnData = this.data[--this.len];
        is_full = false;
        if (len == 0){
            is_empty = true;
        }
        return returnData;

    }

}
