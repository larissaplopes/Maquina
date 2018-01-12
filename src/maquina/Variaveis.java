/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquina;

/**
 *
 * @author Suporte
 */
public class Variaveis {
    String nome,
           tipo;
    int escopo;
    
    public Variaveis(String nome, String tipo, int escopo){
        this.nome = nome;
        this.escopo = escopo;
        this.tipo = tipo;
    }
    public Variaveis(){
        this("", "", -1);
    }
}
