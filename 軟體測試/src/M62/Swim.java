package M62;

public class Swim {
    private static final int NORMAL_PRICE = 200;
    private static final int WEEKEND_PRICE = 250;
    double getPrice (boolean is_member,boolean is_group,int time,int week,int age) throws Exception {
        if(time>22 || time<5) throw new Exception("Not Open");
        if(age<3 || age>75) throw new Exception("Not Open");
        double discount = 1;
        if (week == 6 || week == 7) {
            discount = is_member ? 0.5 : 1;
            return WEEKEND_PRICE * discount;
        }
        if (time < 7) discount = 0.8;
        if (age <=12 || age>=60) discount = 0.8;
        if (is_group) discount = 0.7;
        if (is_member) discount = 0.5;

        return NORMAL_PRICE * discount;
    }
}
