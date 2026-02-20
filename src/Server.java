import java.util.Random;

public class Server {

    double latency;
    Random rand = new Random();

    public Server(double latency) {
        this.latency = latency;
    }

    public double getLatency() {
        return Math.max(1, latency + rand.nextGaussian() * 10);
    }

    public void drift() {
        latency += rand.nextGaussian();
        if (latency < 10) latency = 10;
        if (latency > 300) latency = 300;
    }
}