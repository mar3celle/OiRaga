public class Utils {

//classe de metodos pra usar no Player e no World

    public static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));       //nao permite sair do mapa
    }

    // Calcula se dois círculos se sobrepõem
    public static boolean overlaps(double x1, double y1, double r1,
                                   double x2, double y2, double r2) {
        double dx = x2 - x1, dy = y2 - y1;
        double rr = r1 + r2;
        return dx*dx + dy*dy < rr*rr;
    }

    public static double dist2(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1, dy = y2 - y1;
        return dx*dx + dy*dy;
    }

    // converte area em raio
    public static double areaFromRadius(double r) {
        return Math.PI * r * r;
    }

    public static double radiusFromArea(double area) {
        return Math.sqrt(area / Math.PI);   //converte raio em area
    }
}
