package Server;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Classe que controla a leitura do mapa de localizações
 */
public class MapHandler implements Runnable{
    private final int[][] users;
    private final int[][] contaminated;
    private final ReentrantLock lock;
    private final User user;
    private final int N;

    /**
     * Construtor da classe
     *
     * @param users         Mapa com o número de Utilizadores por localização
     * @param contaminated  Mapa com número de Utilizadores contaminados por localização
     * @param lock          Lock para exclusão mútua no acesso as matrizes acima referidas
     * @param user          Utilizador
     * @param N             Tamanho do mapa(NxN)
     */
    public MapHandler(int[][] users, int[][] contaminated, ReentrantLock lock, User user, int N) {
        this.users = users;
        this.contaminated = contaminated;
        this.lock = lock;
        this.user = user;
        this.N = N;
    }

    /**
     * Define o comportamento de uma thread na classe
     */
    public void run(){
        boolean local;
        boolean isCovid;
        for(int i=0; i<N; i++)
            for(int j=0; j<N; j++) {
                user.getLock().lock();
                try {
                    local = user.getLocal(i, j);
                    isCovid = user.isCovid();
                } finally { user.getLock().unlock();}

                lock.lock();
                try {
                    if (local) {
                        users[i][j]++;
                        if (isCovid)
                            contaminated[i][j]++;
                    }
                } finally {
                    lock.unlock();
                }
            }
    }
}
