import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class InputThread extends Thread {
    private Socket clientSocket;
    private Scanner sc = new Scanner(System.in);

    public InputThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (PrintStream saida = new PrintStream(clientSocket.getOutputStream())) {

            System.out.println("Inicio:");
            int inicio = sc.nextInt();

            System.out.println("Fim:");
            int fim = sc.nextInt();

            System.out.println("Qtd:");
            int qtd = sc.nextInt();

            saida.println(inicio);
            saida.println(fim);
            saida.println(qtd);
            saida.flush();

            while (true) {
                System.out.println("Digite " + qtd + " números separados por espaço:");
                int i = 0;
                while (i < qtd) {
                    if (sc.hasNextInt()) {
                        int bet = sc.nextInt();
                        if (bet > inicio && bet < fim) {
                            saida.println(bet);
                            saida.flush();
                            i++;
                        } else {
                            System.out.println("Número inválido, digite outro:");
                        }
                    } else {
                        sc.next();
                        System.out.println("Entrada inválida, digite um número:");
                    }
                }

                System.out.println("Aposta enviada! Digite a próxima aposta.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
