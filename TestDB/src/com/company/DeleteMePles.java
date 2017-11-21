package com.company;

public class DeleteMePles extends Thread {

    // Gerar o numero da fatura
    private static int n = 0;

    private static synchronized int next() {
        return n++;
    }

    private static long ultima = -1;

    private static long iaa = 0, tra = 0, c = 0;
    private static boolean start = false;

    private static synchronized void regista(long antes, long depois) {


        long tr = depois - antes;

        long anterior = ultima;
        ultima = depois;

        if (anterior < 0 || !start)
            return;

        long ia = depois - anterior;

        iaa += ia;
        tra += tr;
        c++;
    }

    public static synchronized void partida() {
        start = true;
    }

    public static synchronized void imprime() {
        double trm = (tra / 1e9d) / c;
        double debito = 1 / ((iaa / 1e9d) / c);

        System.out.println("debito = " + debito + " tps, tr = " + trm + " s");

    }
}