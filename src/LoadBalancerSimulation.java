import java.util.Arrays;
import java.util.Random;

public class LoadBalancerSimulation {

    static int K = 5;
    static int N = 10000;
    static double tau = 0.5;
    static double alpha = 0.1;
    static Random rand = new Random(42);

    public static void main(String[] args) {

        long t1, t2;

        t1 = System.nanoTime();
        double[] softmax = softmaxCalistir();
        t2 = System.nanoTime();
        long softmaxSure = (t2 - t1) / 1000000;

        t1 = System.nanoTime();
        double[] rr = roundRobinCalistir();
        t2 = System.nanoTime();
        long rrSure = (t2 - t1) / 1000000;

        t1 = System.nanoTime();
        double[] rnd = randomCalistir();
        t2 = System.nanoTime();
        long rndSure = (t2 - t1) / 1000000;

        System.out.println("=== SONUCLAR ===");
        System.out.printf("Softmax      ortalama: %.2f ms   p95: %.2f ms%n", ort(softmax), p95(softmax));
        System.out.printf("Round-Robin  ortalama: %.2f ms   p95: %.2f ms%n", ort(rr),      p95(rr));
        System.out.printf("Random       ortalama: %.2f ms   p95: %.2f ms%n", ort(rnd),     p95(rnd));

        System.out.printf("%nSoftmax Round-Robin'e gore +%.1f%% daha hizli%n", (ort(rr)  - ort(softmax)) / ort(rr)  * 100);
        System.out.printf("Softmax Random'a gore      +%.1f%% daha hizli%n",   (ort(rnd) - ort(softmax)) / ort(rnd) * 100);

        System.out.println("\n=== SURE ===");
        System.out.println("Softmax     : " + softmaxSure + " ms");
        System.out.println("Round-Robin : " + rrSure      + " ms");
        System.out.println("Random      : " + rndSure     + " ms");

        System.out.println("\n=== BIG-O ===");
        System.out.println("Random      -> O(1)");
        System.out.println("Round-Robin -> O(1)");
        System.out.println("Softmax     -> O(K)  (her istekte K sunucu icin 5 adim: max + exp + topla + normalize + sec)");
    }

    static Server[] sunucuOlustur() {
        Server[] sunucular = new Server[K];
        for (int i = 0; i < K; i++) {
            sunucular[i] = new Server(50 + i * 20);
        }
        return sunucular;
    }

    static int softmaxSec(double[] q) {
        double max = q[0];
        for (int i = 1; i < K; i++) {
            if (q[i] > max) max = q[i];
        }

        double[] p = new double[K];
        double toplam = 0;
        for (int i = 0; i < K; i++) {
            p[i] = Math.exp((q[i] - max) / tau);
            toplam += p[i];
        }
        for (int i = 0; i < K; i++) {
            p[i] /= toplam;
        }

        double sayi = rand.nextDouble();
        double kumulatif = 0;
        for (int i = 0; i < K; i++) {
            kumulatif += p[i];
            if (sayi < kumulatif) return i;
        }
        return K - 1;
    }

    static double[] softmaxCalistir() {
        Server[] sunucular = sunucuOlustur();
        double[] q = new double[K];
        double[] gecikmeler = new double[N];

        for (int t = 0; t < N; t++) {
            int secilen = softmaxSec(q);
            double gecikme = sunucular[secilen].getLatency();
            gecikmeler[t] = gecikme;
            q[secilen] = (1 - alpha) * q[secilen] + alpha * (-gecikme);
            for (Server s : sunucular) s.drift();
        }
        return gecikmeler;
    }

    static double[] roundRobinCalistir() {
        Server[] sunucular = sunucuOlustur();
        double[] gecikmeler = new double[N];
        for (int t = 0; t < N; t++) {
            gecikmeler[t] = sunucular[t % K].getLatency();
            for (Server s : sunucular) s.drift();
        }
        return gecikmeler;
    }

    static double[] randomCalistir() {
        Server[] sunucular = sunucuOlustur();
        double[] gecikmeler = new double[N];
        for (int t = 0; t < N; t++) {
            gecikmeler[t] = sunucular[rand.nextInt(K)].getLatency();
            for (Server s : sunucular) s.drift();
        }
        return gecikmeler;
    }

    static double ort(double[] dizi) {
        double toplam = 0;
        for (double d : dizi) toplam += d;
        return toplam / dizi.length;
    }

    static double p95(double[] dizi) {
        double[] sirali = dizi.clone();
        Arrays.sort(sirali);
        return sirali[(int) (sirali.length * 0.95)];
    }
}