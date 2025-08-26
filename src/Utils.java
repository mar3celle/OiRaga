public final class Utils {
    private Utils(){}

    public static double areaFromRadius(double r){
        return Math.PI * r * r;         //converte raio em area
    }

    public static double radiusFromArea(double a){
        return Math.sqrt(a / Math.PI);      //converte area em raio
    }
}
