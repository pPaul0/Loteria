import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrizeDrawThread extends Thread {
    private ClientThread clientThread;
    private Random random = new Random();

    public PrizeDrawThread(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(60000); // 1 minuto de espera

                int inicio = clientThread.getInicio();
                int fim = clientThread.getFim();
                int qtd = clientThread.getQtd();

                // sorteio dos números
                List<Integer> numerosSorteados = new ArrayList<>();
                while (numerosSorteados.size() < qtd) {
                    int num = random.nextInt(fim - inicio + 1) + inicio;
                    if (!numerosSorteados.contains(num)) {
                        numerosSorteados.add(num);
                    }
                }

                // pega apostas do cliente e zera a lista
                List<List<Integer>> apostas = clientThread.pegarEZerarApostas();

                // verifica acertos e envia ao cliente
                for (List<Integer> aposta : apostas) {
                    List<Integer> acertos = new ArrayList<>();
                    for (int n : aposta) {
                        if (numerosSorteados.contains(n)) {
                            acertos.add(n);
                        }
                    }
                    // envia mensagem ao cliente (será lida pela ClientReceiverThread)
                    clientThread.enviarMensagem("Números sorteados: " + numerosSorteados);
                    clientThread.enviarMensagem("Sua aposta: " + aposta + " | Acertos: " + acertos);
                }

                // se não houver apostas, envia apenas resultado do sorteio
                if (apostas.isEmpty()) {
                    clientThread.enviarMensagem("Números sorteados: " + numerosSorteados);
                    clientThread.enviarMensagem("Nenhuma aposta recebida neste ciclo.");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
