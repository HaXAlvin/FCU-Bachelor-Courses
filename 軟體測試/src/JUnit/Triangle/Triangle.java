package JUnit.Triangle;

public class Triangle {

    double[] lines;
    String type;
    double delta = 0.0000000001;//精度誤差

    Triangle(double a1, double a2, double a3) throws Exception {
        lines = new double[]{a1, a2, a3};
    }

    Triangle setTriangleType() throws Exception {
        if (checkUpright()) {
            this.type = "正三角形";
        } else if (checkSameAndNinety()) {
            this.type = "等腰直角三角形";
        } else if (checkNinety()) {
            this.type = "直角三角形";
        } else if (checkTwoSameLine()) {
            this.type = "等腰三角形";
        } else if (checkNormal()) {
            this.type = "一般三角形";
        } else if (checkNotTri()) {
            throw new Exception("Not A Triangle");
        }
        return this;
    }

    boolean checkUpright() {//正三角形 (contain twoSame & Normal)
        boolean sameLine = compareD(this.lines[0], this.lines[1]) && compareD(this.lines[1], this.lines[2]);
        return checkNormal() && sameLine;
    }

    boolean checkSameAndNinety() {//等腰直角三角形(contain twoSame & ninety & Normal)
        return checkNinety() && checkTwoSameLine();
    }

    boolean checkNinety() {//直角三角形(contain Normal)
        boolean is_ninety = false;
        for (int i = 0; i < 3; i++) {
            is_ninety = is_ninety || pythagorean(this.lines[i % 3], this.lines[(i + 1) % 3], this.lines[(i + 2) % 3]);
        }
        return checkNormal() && is_ninety;
    }

    boolean checkTwoSameLine() {//等腰三角形(contain Normal)
        boolean same = false;
        for (int i = 0; i < 3; i++) {
            same = same || compareD(this.lines[i % 3], this.lines[(i + 1) % 3]);
        }
        return checkNormal() && same;
    }

    boolean checkNormal() {//三角形
        boolean twoSumBigger = true;
        boolean noZero = false;
        for (int i = 0; i < 3; i++) {
            twoSumBigger = twoSumBigger && this.lines[i % 3] + this.lines[(i + 1) % 3] > this.lines[(i + 2) % 3];
            noZero = noZero || this.lines[i] > 0;
        }
        return twoSumBigger && noZero;
    }

    boolean checkNotTri() {//不是三角形
        return !checkNormal();
    }

    boolean pythagorean(double a, double b, double c) {
        return compareD(c * c, a * a + b * b);
    }

    boolean compareD(double a, double b) {
        //return if a == b (+-delta)
        return Math.abs(a - b) <= delta;
    }

}
