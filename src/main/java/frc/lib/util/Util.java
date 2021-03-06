package frc.lib.util;

public class Util {
    /** Prevent this class from being instantiated. */
    private Util() {
    }

    public static double kEpsilon = 1e-6;
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }
    public static boolean epsilonEquals(double a, double b){
        return epsilonEquals(a,b,kEpsilon);
    }

    public static int clamp(int x, int min, int max){
        return Math.max(Math.min(x,max),min);
    }
    public static double clamp(double x, double min, double max){
        return Math.max(Math.min(max,x),0);
    }

    public static double linearInterp(double a, double b, double t){
        t = clamp(t,0,1);
        return a + (b-a)*t;
    }
    public static Vec2 linearInterp(Vec2 a, Vec2 b, double t){
        return new Vec2(linearInterp(a.x,b.x,t), linearInterp(a.y,b.y,t));
    }

}