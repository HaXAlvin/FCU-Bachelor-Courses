package Quiz2;

import java.util.Arrays;

public class Grades {
    double[] averageScore(int[] math, int[] chinese) throws Exception {

        if(math.length == 0 || chinese.length == 0){
            throw new Exception("Empty Array");
        }
        double sum=0;
        double[] ans = new double[2];
        for (int i : math) {
            inRange(i);
            sum += i;
        }
        ans[0] = sum/math.length;
        sum=0;
        for(int i:chinese){
            inRange(i);
            sum += i;
        }
        ans[1] = sum/ chinese.length;
        return ans;
    }
    void inRange(int oneGrade) throws Exception{
        if (oneGrade >100 || oneGrade<0){
            throw new Exception("Out Range Grade");
        }
    }
    public static void main(String[] args) throws Exception {
        Grades g = new Grades();
        System.out.println(Arrays.toString(g.averageScore(new int[]{1, 2, 3}, new int[]{3, 4, 5})));
    }
}
