/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquina;

/**
 *
 * @author Larissa
 */
public class Tabela {
    String id, type;
    int linha;
    
    public Tabela(String id, String type, int linha){
        this.id = id;
        this.type = type;
        this.linha =linha;
    }
    
    public String toString(){
        return "" + id + "\t\t" + type + "\t" + linha;
    }
}
