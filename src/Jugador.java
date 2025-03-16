import java.util.Random;
import javax.swing.JPanel;
import java.util.Arrays;

public class Jugador {

    private int TOTAL_CARTAS = 10;
    private int MARGEN = 10;
    private int DISTANCIA = 40;
    private int puntaje = 0;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Random r = new Random();

    public void repartir() {
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = new Carta(r);
        }
        puntaje = 0; // Reiniciar puntaje al repartir nuevas cartas
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicionHor = MARGEN + cartas.length * DISTANCIA;
        for (Carta c : cartas) {
            c.mostrar(pnl, posicionHor, MARGEN);
            posicionHor -= DISTANCIA;
        }
        pnl.repaint();
    }

    public String getGrupos() {
        String mensaje = "No se encontraron grupos";
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;
        for (int c : contadores) {
            if (c >= 2) {
                hayGrupos = true;
                break;
            }
        }

        if (hayGrupos) {
            mensaje = "Se encontraron los siguientes grupos:\n";
            int PCarta = 0;
            for (int c : contadores) {
                if (c >= 2) {
                    mensaje += Grupo.values()[c] + " DE " + NombreCarta.values()[PCarta] + "\n";
                }
                PCarta++;
            }
        }

        Pinta pintaEscalera = verificarEscalera();
        if (pintaEscalera != null) {
            mensaje += "\n¡Escalera de " + pintaEscalera + " encontrada!";
        }

        puntaje += calcularPuntajeSinGrupo(); // Sumar puntaje de cartas sin grupo
        return mensaje + "\nPuntaje: " + puntaje;
    }

    private Pinta verificarEscalera() {
        Arrays.sort(cartas, (c1, c2) -> c1.getNombre().ordinal() - c2.getNombre().ordinal());
        
        int consecutivos = 1;
        Pinta pintaActual = cartas[0].getPinta();
        
        for (int i = 1; i < cartas.length; i++) {
            if (cartas[i].getPinta() == pintaActual &&
                cartas[i].getNombre().ordinal() == cartas[i - 1].getNombre().ordinal() + 1) {
                consecutivos++;
                if (consecutivos >= 4) {
                    return pintaActual;
                }
            } else {
                consecutivos = 1;
                pintaActual = cartas[i].getPinta();
            }
        }
        return null;
    }

    private int calcularPuntajeSinGrupo() {
        int puntajeTotal = 0;
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }
        
        for (int i = 0; i < contadores.length; i++) {
            if (contadores[i] == 1) { // Solo suma si no pertenece a un par o grupo
                if (i == 0 || i >= 10) { // AS, JACK, QUEEN, KING valen 10
                    puntajeTotal += 10;
                } else {
                    puntajeTotal += (i + 1);
                }
            }
        }
        return puntajeTotal;
    }
}
