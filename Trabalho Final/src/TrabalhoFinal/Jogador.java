package TrabalhoFinal;

public class Jogador {
    private String nome;
    private int fichas = 50;

    public String getNome(){
        return(this.nome);
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public int getFichas(){
        return(this.fichas);
    }

    public void apostarFichas(int fichasApostadas){
        this.fichas = this.fichas - fichasApostadas;
    }

    public void ganharFichas(int fichasGanhas){
        this.fichas = this.fichas + fichasGanhas;
    }
}
