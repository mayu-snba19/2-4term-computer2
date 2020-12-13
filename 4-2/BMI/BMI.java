import java.util.Scanner;

public class BMI {
    public static void main(String[] args) {
        Scanner kbd = new Scanner(System.in);
        System.out.print("input height (cm): ");
        int h = kbd.nextInt();
        System.out.print("input weight (kg): ");
        int w = kbd.nextInt();
        int BMI = w * 100 / h * 100 / h;
        System.out.println("BMI = " + BMI);
    }
}
