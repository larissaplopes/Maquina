/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maquina;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 *
 * @author Larissa
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    static LinkedList<Tabela> tab;
    static LinkedList<Variaveis> pilhaVar = new LinkedList<Variaveis>(),
                                 pilhaProc = new LinkedList<Variaveis>();
    static LinkedList<String> pilhaTipo = new LinkedList<String>();
    static Tabela elem;
    static int indice = 0,
               escopo = 0;
    static boolean escopoFuncao = false;
    
    public static void main(String[] args) {
        // TODO code application logic here
        

        //char[] letras = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
        String[] palReservada = {"program", "var", "integer", "real", "boolean",
                                 "procedure", "begin", "end", "if", "then", 
                                 "else", "while", "do", "not", "for", "to"};
        
        int linha = 1,
            linhaAtual = -1,
            iV = 0;
        String entrada = "",
               entrada1 = "bb";
        boolean isOp = false,
                isFloat = false,
                isReservado = false,
                isComentario = false,
                isCComentario = false,
                isNewNumber = false,
                isNaoN = false;
        
        
        try {
            
            BufferedReader leitura = new BufferedReader(new FileReader("C:/Programa/Clauirton/TestLex1.txt"));
            String str = leitura.readLine();
            entrada += str + '\n';
            while(leitura.ready()){
                str = leitura.readLine();
                entrada += str + '\n';
            }
            
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro na abertura do arquivo");
            System.exit(1);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Problema na leitura do arquivo");
            System.exit(1);
        }
        tab = new LinkedList<Tabela>();
        for(int i = 0; i < entrada.length(); i++){
            isFloat =  isOp = isReservado =  isNewNumber = isNaoN = false;
            if(entrada.charAt(i) == '\n'){
                linha++;
                continue;
            }

            if(entrada.charAt(i) == '{'){
                isComentario = true;
                continue;
            }
            
            if(isComentario && entrada.charAt(i) != '}'){
                continue;
            }else if(entrada.charAt(i) == '}'){
                isComentario = false;
                continue;
            }
            
            if(entrada.charAt(i) == ' ' || entrada.charAt(i) == '\t' || entrada.charAt(i) == '\r'){
                continue;
            }
            if(Character.isLetter(entrada.charAt(i))){
                String str = "" + entrada.charAt(i);
                i++;
                while(i<entrada.length()){
                    if(Character.isLetter(entrada.charAt(i)) || Character.isDigit(entrada.charAt(i)) || entrada.charAt(i) == '_'){
                        str+= entrada.charAt(i);
                        i++;
                    }else{
                        //testa se eh um identificador ou palavra reservada ou operadores de comparacao
                        
                        for(String s : palReservada){
                            if(str.equals(s)){
                                isReservado = true;
                                tab.add(new Tabela(str, "PR", linha));
                                break;
                            }
                        }
                        
                        if(str.equals("or")){
                            isOp = true;
                            tab.add(new Tabela(str, "Op Add", linha));
                        }else if(str.equals("and")){
                            isOp = true;
                            tab.add(new Tabela(str, "Op Mult", linha));
                        }
                        
                        if(!isOp && !isReservado){
                           tab.add(new Tabela(str,"id",linha)); 
                        }
                        
                        break;
                    } 
                }
                i--;
                continue;
            }//if ident
            
            if(Character.isDigit(entrada.charAt(i))){
                String str = "" + entrada.charAt(i);
                i++;
                while(i<entrada.length()){
                    if(Character.isDigit(entrada.charAt(i)) || entrada.charAt(i) == '.' ){
                        str+= entrada.charAt(i);
                        if(entrada.charAt(i) == '.'){
                            iV = i;
                            isFloat = true;
                        }
                        i++;

                    } 
                    /*else if(entrada.charAt(i) == '.' && isFloat == false){
                        isFloat = true;
                    }else if(entrada.charAt(i) == 'V') {
                        isNewNumber = true;
                    }*/else{
                        //testa se eh um identificador ou palavra reservada
                        if(isFloat){
                            tab.add(new Tabela(str,"float",linha));
                        }else{
                            tab.add(new Tabela(str,"int",linha));
                        }
                        break;
                    } 
                }
                i--;
                continue;
            }//if digit
            
            if(entrada.charAt(i) == ';' || entrada.charAt(i) == '.' || entrada.charAt(i) == '(' || 
               entrada.charAt(i) == ')' || entrada.charAt(i) == ','){
                String str = "" + entrada.charAt(i);
                tab.add(new Tabela(str, "Delimitador", linha));
                continue;
            }//if delimitador
            
            if(entrada.charAt(i) == '<' || entrada.charAt(i) == '>'){
                String str = "" + entrada.charAt(i);
                if(entrada.charAt(i + 1) == '=' ){
                    str += entrada.charAt(i + 1);
                    tab.add(new Tabela(str, "Op Relacional", linha));
                    i++;
                    continue;
                }else if(entrada.charAt(i) == '<' && entrada.charAt(i + 1) == '>'){
                    str += entrada.charAt(i + 1);
                    tab.add(new Tabela(str, "Op Relacional", linha));
                    i++;
                    continue;
                }else{
                    tab.add(new Tabela(str, "Op Relacional", linha));
                    continue;
                }
            }//if relacional 
            
            if(entrada.charAt(i) == ':'){
                String str = "" + entrada.charAt(i);
                if(entrada.charAt(i + 1) == '=' ){
                    str += entrada.charAt(i + 1);
                    tab.add(new Tabela(str, "Atribuicao", linha));
                    i++;
                    continue;
                }else{
                    tab.add(new Tabela(str, "Delimitador", linha));
                    continue;
                }
            }//if :
            
            if(entrada.charAt(i) == '+' || entrada.charAt(i) == '-'){
                String str = "" + entrada.charAt(i);
                tab.add(new Tabela(str, "Op Add", linha));
                continue;
            }//if Add
            
            if(entrada.charAt(i) == '*' || entrada.charAt(i) == '/'){
                String str = "" + entrada.charAt(i);
                tab.add(new Tabela(str, "Op Mult", linha));
                continue;                
            }//if Add
            
            if(!Character.isLetter(entrada.charAt(i)) || !Character.isDigit(entrada.charAt(i)) ||
               entrada.charAt(i) != '/' || entrada.charAt(i) != ':' || entrada.charAt(i) != '*'  ){
                String str = "" + entrada.charAt(i);
                tab.add(new Tabela(str,"Erro",linha));
            }//if erro
            
        }//for
        
        System.out.println("Token\t\tClassificacao\t\tLinha");
        for(Tabela t : tab){
            System.out.println(t.toString());
        }
        Anal_Sitatico();
    }//main
    
    public static void Anal_Sitatico(){
        //elem = tab.get(indice++);
        System.out.println("\nInicio do Analisador Sintatico");
        Program();
        System.out.println("\nFim do Analisador Sintatico\n");
    }
    public static void ConsomeID(String str){
        if(tab.get(indice).id.equals(str)){
            indice++;
        }else{
            System.out.println("Erro na linha "+ tab.get(indice).linha + " " + tab.get(indice).id + " esperado "+ str);
        }
    }
    public static void ConsomeTipo(String str){
        if(tab.get(indice).type.equals(str)){
            indice++;
        }else{
            System.out.println(" 1 = Erro na linha "+ tab.get(indice).linha + " " + tab.get(indice).id + " esperado "+ str);
        }
    }
    
    public static void PreencheTipo(String s){
        for(Variaveis v : pilhaVar){
            if(v.tipo.equals("")){
                v.tipo += s;
            }
        }
    }
    
    public static boolean VerificaExistencia(String s, int escopoAtual){
        for(Variaveis v : pilhaVar){
            if(v.nome.equals(s) && v.escopo == escopoAtual){
                System.out.println("Variavel jah declarada");
                return true;
            }
        }
        return false;
    }
    
    public static void RemoveEscopo(){
        for(int i = 0; i < pilhaVar.size();i++){
            if(pilhaVar.get(i).escopo == escopo){
                pilhaVar.remove(pilhaVar.get(i));
                i--;
            }
        }
        escopo--;
    }
    
    public static void Program(){
//        if(elem.id.equals("Program")){
//            elem = tab.get(indice++);
//        }
        ConsomeID("program");
        if(!VerificaExistencia(tab.get(indice).id, escopo)){
            pilhaVar.push(new Variaveis(tab.get(indice).id, "program", escopo));
        }
        ConsomeTipo("id");
        ConsomeID(";");
        DecVar();
        DecSubProgs();
        ComComp();
        ConsomeID(".");

    }
    
    public static void DecVar(){
        if(tab.get(indice).id.equals("var")){
            ConsomeID("var");
            ListaDecVar();
        }
    }
    
    public static void ListaDecVar(){
        Lista();
        ConsomeID(":");
        Tipo();
        ConsomeID(";");
        ListaDecVar2();
    }
    
    public static void ListaDecVar2(){
        if(tab.get(indice).type.equals("id")){
            ListaDecVar();
        }
    }
    
    public static void Lista(){
        if(tab.get(indice).type.equals("id")){
            if (!VerificaExistencia(tab.get(indice).id, escopo)) {
                pilhaVar.push(new Variaveis(tab.get(indice).id, "", escopo));
            }
        }
        ConsomeTipo("id");
        Lista2();
    }
    
    public static void Lista2(){
        if(tab.get(indice).id.equals(",")){
            ConsomeID(",");
            if (!VerificaExistencia(tab.get(indice).id, escopo)) {
                pilhaVar.push(new Variaveis(tab.get(indice).id, "", escopo));
            }
            ConsomeTipo("id");
            Lista2();
        }
    }
//        if(elem.id.equals(",")){
//            elem = tab.get(indice++);
//            if(elem.type.equals("ID")){
//                Lista2();
//            }
//        }
    public static void Tipo(){
        if(tab.get(indice).id.equals("integer")){
            PreencheTipo("integer");
            ConsomeID("integer");
        }else if(tab.get(indice).id.equals("real")){
            PreencheTipo("real");
            ConsomeID("real");
        } else if(tab.get(indice).id.equals("boolean")){
            PreencheTipo("boolean");
            ConsomeID("boolean");
        }else if(!tab.get(indice).id.equals("boolean") && !tab.get(indice).id.equals("real") && 
                 !tab.get(indice).id.equals("integer")){
            System.out.println("Erro tipo");
        }
    }
    
    public static void DecSubProgs(){
        DecSubProg();
    }
    
    public static void DecSubProg(){
        if(tab.get(indice).id.equals("procedure")){
            DecSubP();
        }
    }
    
    public static boolean VerificaProc(String s, int escopoAtual){
        if(!pilhaProc.isEmpty()){
            for(Variaveis v : pilhaProc){
                if(v.nome.equals(s) && v.escopo == escopoAtual){
                    System.out.println("Procedimento jah declarada");
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void DecSubP(){
        ConsomeID("procedure");
        if(!VerificaProc(tab.get(indice).id, escopo)){
            pilhaProc.push(new Variaveis(tab.get(indice).id, "procedure", escopo));
        }
        
//        if (!VerificaExistencia(tab.get(indice).id, escopo)) {
//            pilhaVar.push(new Variaveis(tab.get(indice).id, "procedure", escopo));
//        }
        ConsomeTipo("id");
        escopo++;
        escopoFuncao = true;
        Argumentos();
        ConsomeID(";");
        DecVar();
        DecSubProgs();
        ComComp();
        ConsomeID(";");
    }
    
    public static void Argumentos(){
        if(tab.get(indice).id.equals("(")){
            ConsomeID("(");
            ListaPar();
            ConsomeID(")");
        }
    }
    
    public static void ListaPar(){
        ConsomeID("var");
        Lista();
        ConsomeID(":");
        Tipo();
        ListaPar2();
    }
    
    public static void ListaPar2(){
        if(tab.get(indice).id.equals("(")){
            if(tab.get(indice).id.equals(";"))
            ConsomeID(";");
            Lista();
            ConsomeID(":");
            Tipo();
        }
    }
    
    public static void ComComp(){
        
        ConsomeID("begin");
        if(!escopoFuncao){
            escopo++;
        }else{
            escopoFuncao = false;
        }
        ComOp();
        ConsomeID("end");
        RemoveEscopo();
    }
    
    public static void ComOp(){
        if(tab.get(indice).id.equals("if") || tab.get(indice).id.equals("while") ||
           tab.get(indice).id.equals("else") || tab.get(indice).type.equals("id") ||
           tab.get(indice).id.equals("do") ){
            ListaComando();
        }
    }
    
    public static void ListaComando(){
        Comando();
        ListaComando2();
    }
    
    public static void ListaComando2(){
        if(tab.get(indice).id.equals(";")){
            ConsomeID(";");
            Comando();
            ListaComando2();
        }
    }
    
    public static void Comando(){
        String tipo = "";
        if(tab.get(indice).type.equals("id") && tab.get(indice + 1).id.equals(":=")){
            tipo += TipoVar(tab.get(indice).id);            
            ConsomeTipo("id");
            ConsomeID(":=");
            Expressao();
            VerificaAtrib(tipo);
        }else if(tab.get(indice).id.equals("if")){
            ConsomeID("if");
            Expressao();
            ConsomeID("then");
            Comando();
            ParteElse();
        }else if(tab.get(indice).id.equals("while")){
            ConsomeID("while");
            Expressao();
            ConsomeID("do");
            Comando();
        }else if(tab.get(indice).type.equals("id") && !tab.get(indice + 1).id.equals(":=")){
            AtivProc();
        }else{
            ComComp();
        }
    }
    
    public static void ParteElse(){
        if(tab.get(indice).id.equals("else")){
            Comando();
        }
    }
    
    public static void AtivProc(){
        if(tab.get(indice).type.equals("id") && tab.get(indice + 1).id.equals("(")){
            ConsomeTipo("id");
            ConsomeID("(");
            ListaExp();
            ConsomeID(")");
        }
    }
    
    public static void ListaExp(){
        Expressao();
        ListaExp2();
    }
    
    public static void ListaExp2(){
        if(tab.get(indice).id.equals(",")){
            ConsomeID(",");
            Expressao();
            ListaExp2();
        }
    }
    
    public static void Expressao(){
        ExpSimples();
        Expressao2();
    }
    
    public static void Expressao2(){
        if(tab.get(indice).type.equals("Op Relacional")){
            ConsomeTipo("Op Relacional");
            ExpSimples();
            VerificaExpressao("Op Relacional");
            Expressao2();
        }
    }
    
    public static void ExpSimples(){
        if(tab.get(indice).id.equals("+")){
            ConsomeID("+");
            Termo();
            VerificaExpressao("sUnario");
        }else if(tab.get(indice).id.equals("-")){
            ConsomeID("-");
            Termo();
            VerificaExpressao("sUnario");
        }else{
            Termo();
            ExpSimples2();
        }
    }
    
    public static void ExpSimples2(){
        String op = "";
        if(tab.get(indice).type.equals("Op Add")){
            op += tab.get(indice).id;
            ConsomeTipo("Op Add");
            Termo();
            VerificaExpressao(op);
            ExpSimples2();
        }
    }
    
    public static void Termo(){
        Fator();
        Termo2();
    }
    
    public static void Termo2(){
        String op = "";
        if(tab.get(indice).type.equals("Op Mult")){
            op += tab.get(indice).id;
            ConsomeTipo("Op Mult");
            Fator();
            VerificaExpressao(op);
            Termo2();
        }
    }
    
    public static void Fator(){
        if(tab.get(indice).type.equals("id") && tab.get(indice + 1).id.equals("(")){
            ConsomeTipo("id");
            ConsomeID("(");
            ListaExp();
            ConsomeID(")");
        }else if(tab.get(indice).type.equals("id") && !tab.get(indice + 1).id.equals("(")){
            pilhaTipo.push(TipoVar(tab.get(indice).id));
            ConsomeTipo("id");
        }else if(tab.get(indice).type.equals("int")){
            pilhaTipo.push("integer");
            ConsomeTipo("int");
        }else if(tab.get(indice).type.equals("float")){
            pilhaTipo.push("real");
            ConsomeTipo("float");
        }else if(tab.get(indice).id.equals("true")){
            pilhaTipo.push("boolean");
            ConsomeID("true");
        }else if(tab.get(indice).id.equals("false")){
            pilhaTipo.push("boolean");
            ConsomeID("false");
        }else if(tab.get(indice).id.equals("(")){
            ConsomeID("(");
            Expressao();
            ConsomeID(")");
        }else if(tab.get(indice).id.equals("not")){
            ConsomeID("not");
            Fator();
            VerificaExpressao("not");
        }
    }
    
    public static String TipoVar(String s){
        String aux = "";
        for(Variaveis v : pilhaVar){
            if(v.nome.equals(s)){
                aux = "";
                aux += v.tipo;
            }
        }
        if(aux.equals("")){
            System.out.println("Variavel nao declarada.");
            return null;
        }
        return aux;
    }
    
    public static void VerificaExpressao(String op){
        String operando1 = "",
               operando2 = "";
        if(op.equals("not")){
            operando1 += pilhaTipo.pop();
            if(operando1.equals("boolean")){
                pilhaTipo.push(operando1);
            }else{
                System.out.println("Erro de tipo: operador " + op);
            }
        } else if(op.equals("*") || op.equals("/") ||
           op.equals("+") || op.equals("-")){
            operando1 += pilhaTipo.pop();
            operando2 += pilhaTipo.pop();
            if(operando1.equals("integer") && operando2.equals("integer")){
                pilhaTipo.push("integer");
            }else if( (operando1.equals("real") && operando2.equals("integer")) ||
                      (operando1.equals("integer") && operando2.equals("real")) ||
                      (operando1.equals("real") && operando2.equals("real"))){
                pilhaTipo.push("real");
            }else if(!operando1.equals("integer") && !operando1.equals("real")){
                System.out.println("Erro de tipo: operador " + op);
            }else if(!operando2.equals("integer") && !operando2.equals("real")){
                System.out.println("Erro de tipo: operador " + op);
            }
        }else if(op.equals("and") || op.equals("or")){
            operando1 += pilhaTipo.pop();
            operando2 += pilhaTipo.pop();
            if(operando1.equals("boolean") && operando2.equals("boolean")){
                pilhaTipo.push("boolean");
            }else{
                System.out.println("Erro de tipo: operador " + op + operando1 + " " + operando2);
            }
        }else if(op.equals("Op Relacional")){
            operando1 += pilhaTipo.pop();
            operando2 += pilhaTipo.pop();
            if( (operando1.equals("integer") || operando1.equals("real")) &&
                (operando2.equals("integer") || operando2.equals("real")) ){
                pilhaTipo.push("boolean");
            }else{
                System.out.println("Erro de tipo: operador " + op);
            }
        }else if(op.equals("sUnario")){
            operando1 += pilhaTipo.pop();
            if(operando1.equals("integer") || operando1.equals("real")){
                pilhaTipo.push(operando1);
            }else{
                System.out.println("Erro de tipo: operador " + op);
            }
        }
        operando1 = "";
        operando2 = "";
    }
    
    public static void VerificaAtrib(String s){
        String op = pilhaTipo.pop();
        
        if( ((s.equals("integer")) && op.equals("real")) ||
              (s.equals("boolean") && !op.equals("boolean"))  ){
            System.out.println("Erro de atribuicao");
        }
    }
    
}//class
