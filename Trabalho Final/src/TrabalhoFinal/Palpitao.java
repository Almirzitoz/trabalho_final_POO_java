package TrabalhoFinal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Palpitao {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Campeonato campeonato = new Campeonato();
        
        System.out.println("Bem-vindo ao Palpitão!");
        
        System.out.print("Informe o nome do Jogador 1: ");
        String nomeJogador1 = scanner.nextLine();
        System.out.print("Informe o nome do Jogador 2: ");
        String nomeJogador2 = scanner.nextLine();
        
        Jogador jogador1 = new Jogador();
        jogador1.setNome(nomeJogador1);
        Jogador jogador2 = new Jogador();
        jogador2.setNome(nomeJogador2);
        
        System.out.println("Seja bem vindos " + jogador1.getNome() + " e " + jogador2.getNome() + ", vocês tem 50 fichas para palpitar nos seus times escolhidos.");
        
        campeonato.inicializarCampeonato(scanner);
        
        realizarPalpites(scanner, campeonato, jogador1, jogador2);
        
        campeonato.simularPartidas();
        
        exibirResultados(campeonato, jogador1, jogador2);
        
        scanner.close();
    }
    
    private static void realizarPalpites(Scanner scanner, Campeonato campeonato, Jogador jogador1, Jogador jogador2) {
        List<Partida> partidas = campeonato.getPartidas();
        
        for (Partida partida : partidas) {
            realizarPalpiteJogador(scanner, partida, jogador1);
            realizarPalpiteJogador(scanner, partida, jogador2);
        }
    }
    
    private static void realizarPalpiteJogador(Scanner scanner, Partida partida, Jogador jogador) {
        System.out.println("\nPalpite para o jogador " + jogador.getNome() + ":");
        System.out.println("1- " + partida.getTime1().getNome() + " X 2- " + partida.getTime2().getNome());
        
        int palpite;
        do {
            System.out.print("Informe o palpite do time vencedor (1 ou 2): ");
            palpite = scanner.nextInt();
        } while (palpite != 1 && palpite != 2);
        
        System.out.print("Informe o número de fichas: ");
        int fichas = scanner.nextInt();
        
        while (fichas > jogador.getFichas() || fichas <= 0) {
            System.out.println("Número de fichas inválido. Você possui " + jogador.getFichas() + " fichas.");
            System.out.print("Informe o número de fichas: ");
            fichas = scanner.nextInt();
        }
        
        jogador.apostarFichas(fichas);
        partida.registrarPalpite(jogador, palpite == 1 ? partida.getTime1() : partida.getTime2(), fichas);
    }
    
    private static void exibirResultados(Campeonato campeonato, Jogador jogador1, Jogador jogador2) {
        System.out.println("\n--- Resultados do Palpitão ---");
        campeonato.exibirInformacoes();
        System.out.println("\nPontuação final dos jogadores:");
        System.out.println(jogador1.getNome() + ": " + jogador1.getFichas() + " pontos");
        System.out.println(jogador2.getNome() + ": " + jogador2.getFichas() + " pontos");
        
        String vencedor = jogador1.getFichas() > jogador2.getFichas() ? jogador1.getNome() : jogador2.getNome();
        System.out.println("\nO vencedor do Palpitão é: " + vencedor + "!");
    }
}

class Campeonato {
    private List<Time> times;
    private List<Partida> partidas;

    public Campeonato() {
        this.times = new ArrayList<>();
        this.partidas = new ArrayList<>();
    }

    public void inicializarCampeonato(Scanner scanner) {
        int numTimes;
        do {
            System.out.print("Escolha o número de times para compor o campeonato (4 ou 8): ");
            numTimes = scanner.nextInt();
        } while (numTimes != 4 && numTimes != 8);

        scanner.nextLine(); 

        for (int i = 1; i <= numTimes; i++) {
            System.out.print("Informe o nome do time " + i + ": ");
            String nomeTime = scanner.nextLine();
            Time time = new Time();
            time.setNome(nomeTime);
            times.add(time);
        }

        gerarPartidas();
    }

    private void gerarPartidas() {
        for (int i = 0; i < times.size(); i++) {
            for (int j = i + 1; j < times.size(); j++) {
                partidas.add(new Partida(times.get(i), times.get(j)));
            }
        }
    }

    public void simularPartidas() {
        for (Partida partida : partidas) {
            partida.simularPartida();
        }
    }

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void exibirInformacoes() {
        System.out.println("a) Número de partidas: " + partidas.size());
        System.out.println("   Times participantes: " + times.stream().map(Time::getNome).reduce((a, b) -> a + ", " + b).orElse(""));

        System.out.println("\nb) Pontuação final dos times:");
        Collections.sort(times, (t1, t2) -> Integer.compare(t2.getPontos(), t1.getPontos()));
        for (Time time : times) {
            System.out.println("   " + time.getNome() + ": " + time.getPontos() + " pontos");
        }

        System.out.println("\nd) Placares dos times no campeonato:");
        for (Partida partida : partidas) {
            System.out.println("   " + partida.getTime1().getNome() + " " + partida.getGolsTime1() + " x " + 
                               partida.getGolsTime2() + " " + partida.getTime2().getNome());
        }

        System.out.println("\nf) Palpites para cada partida:");
        for (Partida partida : partidas) {
            System.out.println("   " + partida.getTime1().getNome() + " x " + partida.getTime2().getNome() + ":");
            for (Palpite palpite : partida.getPalpites()) {
                System.out.println("     " + palpite.getJogador().getNome() + " apostou " + palpite.getFichas() + 
                                   " fichas no " + palpite.getTimeEscolhido().getNome());
            }
        }

        System.out.println("\ng) Classificação dos times:");
        int numTimes = times.size();
        int libertadores = numTimes / 2;
        int sulAmericana = (numTimes - libertadores) / 2;
        int rebaixados = numTimes - libertadores - sulAmericana;

        System.out.println("   Times classificados para a Libertadores:");
        for (int i = 0; i < libertadores; i++) {
            System.out.println("     " + times.get(i).getNome());
        }

        System.out.println("   Times classificados para a Sul-Americana:");
        for (int i = libertadores; i < libertadores + sulAmericana; i++) {
            System.out.println("     " + times.get(i).getNome());
        }

        System.out.println("   Times rebaixados:");
        for (int i = numTimes - rebaixados; i < numTimes; i++) {
            System.out.println("     " + times.get(i).getNome());
        }

        System.out.println("\n   Critério: Os times foram classificados com base na pontuação final.");
        System.out.println("   A metade superior foi para a Libertadores, a metade do meio para a Sul-Americana,");
        System.out.println("   e o restante foi rebaixado.");
    }
}

class Partida {
    private Time time1;
    private Time time2;
    private int golsTime1;
    private int golsTime2;
    private List<Palpite> palpites;

    public Partida(Time time1, Time time2) {
        this.time1 = time1;
        this.time2 = time2;
        this.palpites = new ArrayList<>();
    }

    public void simularPartida() {
        time1.simularGol();
        time2.simularGol();
        this.golsTime1 = time1.getGol();
        this.golsTime2 = time2.getGol();

        if (golsTime1 > golsTime2) {
            time1.vitoria();
            time2.derrota();
        } else if (golsTime2 > golsTime1) {
            time2.vitoria();
            time1.derrota();
        } else {
            time1.empate();
            time2.empate();
        }

        calcularPontuacaoPalpites();
    }

    public void registrarPalpite(Jogador jogador, Time timeEscolhido, int fichas) {
        palpites.add(new Palpite(jogador, timeEscolhido, fichas));
    }

    private void calcularPontuacaoPalpites() {
        for (Palpite palpite : palpites) {
            Time timeEscolhido = palpite.getTimeEscolhido();
            int saldoGols = (timeEscolhido == time1) ? (golsTime1 - golsTime2) : (golsTime2 - golsTime1);
            
            if (saldoGols > 0) {
                int pontos = palpite.getFichas() * saldoGols;
                palpite.getJogador().ganharFichas(pontos);
            }
        }
    }

    public Time getTime1() {
        return time1;
    }

    public Time getTime2() {
        return time2;
    }

    public int getGolsTime1() {
        return golsTime1;
    }

    public int getGolsTime2() {
        return golsTime2;
    }

    public List<Palpite> getPalpites() {
        return palpites;
    }
}

class Palpite {
    private Jogador jogador;
    private Time timeEscolhido;
    private int fichas;

    public Palpite(Jogador jogador, Time timeEscolhido, int fichas) {
        this.jogador = jogador;
        this.timeEscolhido = timeEscolhido;
        this.fichas = fichas;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public Time getTimeEscolhido() {
        return timeEscolhido;
    }

    public int getFichas() {
        return fichas;
    }
}

class Jogador {
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

class Time {
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
