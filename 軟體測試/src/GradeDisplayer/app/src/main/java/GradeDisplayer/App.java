package GradeDisplayer;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.gradeDisplayer(1);
    }
    //main app
    void gradeDisplayer(int classID){
        int[] grade = gradeCollecter(classID);
        grade = gradeSorter(grade);
        System.out.println(Arrays.toString(grade));
    }
    //return grade[]
    int[] gradeCollecter(int classID){
        int[][] subjectGrade = gradeDB(classID);
        return averageComputer(subjectGrade);
    }
    //return grade[]
    int[] averageComputer(int[][] subjectGrade){
        int peopleCount = subjectGrade.length;
        int[] grade = new int[peopleCount];
        for(int i=0;i<peopleCount;i++){
            for(int j=0;j<subjectGrade[i].length;j++){
                grade[i] += subjectGrade[i][j];
            }
            grade[i]/=subjectGrade[i].length;
        }
        return grade;
    }

    //return subjectGrade[][]
    int[][] gradeDB(int classID){
        if (classID==1){
            return new int[][]{
                    {10,20,30,40},
                    {60,60,60,60},
                    {90,100,80,70}
            };
        }
        if (classID==2){
            return new int[][]{
                    {20,60,80,90},
                    {30,40,50,30},
                    {12,23,34,45}
            };
        }
        return null;
    }
    //return a sorted grade[]
    int[] gradeSorter(int[] grade){
        for(int i=0;i<grade.length;i++){
            for(int j=i+1;j<grade.length;j++){
                if(grade[i]>grade[j]){
                    grade = dataSwaper(grade,i,j);
                }
            }
        }
        return grade;
    }
    //swap i,j int data[]
    int[] dataSwaper(int[] data, int i, int j){
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
        return data;
    }
}
