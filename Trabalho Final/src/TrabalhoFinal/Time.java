package TrabalhoFinal;
import java.util.Random;;
public class Time {
    private String nome;
    private int pontos;
    private int golsPorPartida;

    public String getNome(){
        return(this.nome);
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public int getPontos(){
        return(this.pontos);
    }
    public void derrota(){
        this.pontos = this.pontos + 0;
    }
    public void vitoria(){
        this.pontos = this.pontos + 3;
    }
    public void empate(){
        this.pontos = this.pontos + 1;
    }

    public void simularGol(){
        Random random = new Random();
        this.golsPorPartida = random.nextInt(1,5);
    }
    public int getGol(){
        return(this.golsPorPartida);
    }
}
