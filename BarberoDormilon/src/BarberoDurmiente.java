import java.util.logging.Level;
import java.util.logging.Logger;

public class BarberoDurmiente {

    public static void main(String[] args) {
        Barbero barbero = new Barbero(false);
        System.out.println("Barbero durmiendo, sin clientes...");
        for (int i = 0; i < 4; i++) {
            try{
                Cliente c = new Cliente(i + 1, barbero);
                System.out.printf("Ingresa cliente: %d\n", i+1);
                c.start();
                Thread.sleep(300);
            } catch (InterruptedException e)  {
                Logger.getLogger(Barbero.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}

class Barbero {
    public boolean ocupado;

    public Barbero(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public synchronized void inicioCorte(int numClie) {
        while (isOcupado()) {
            System.out.println("Barbero ocupado, cliente " + numClie + " espera");
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Barbero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.setOcupado(true);
        System.out.println("El barbero empieza a cortar el pelo al cliente " + numClie);
    }

    public synchronized void finCorte(int numClie) {
        this.setOcupado(false);
        System.out.println("El barbero termina de cortar el pelo al cliente " + numClie
                +"\n--------------------------------------------------");
        notify();
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
}


class Cliente extends Thread {
    private final Barbero bar;
    private final int numCliente;

    public Cliente(int nCli, Barbero b) {
        this.numCliente = nCli;
        this.bar = b;
    }

    @Override
    public void run() {
        try {
            bar.inicioCorte(numCliente);
            sleep(1500);
            bar.finCorte(numCliente);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}