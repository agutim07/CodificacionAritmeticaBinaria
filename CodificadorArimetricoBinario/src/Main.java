import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * @author Alberto Gutiérrez Morán
 */

public class Main {

    public static int DECIMALS = 200;

    public static void main(String[] args) throws IOException {
        /** FUENTE DE INFORMACIÓN */
        String alfabeto = "AÁBCDEÉFGHIÍJKLMNÑOÓPQRSTUÚVWXYZ .,;:()¿?¡!-0123456789aábcdeéfghiíjklmnñoópqrstuúvwxyz";
        ArrayList<Alfabeto> lista = generarListaAlfabeto(alfabeto);
        dividirFuente(lista);

        String mensaje = "Trabajo final de SI 2022";
        ArrayList<Character> listaChar = generarListaMsg(mensaje);

        /** CODIFICAR */
        String out = codificarBinario(lista, listaChar);

        /** EXPORTAMOS EL CÓDIGO CODIFICADO EN BINARIO A UN ARCHIVO TXT */
        FileOutputStream oFile = new FileOutputStream("D:\\Alberto GM\\ULE\\3º\\SI\\binaryString.txt");
        byte b[] = out.getBytes();
        oFile.write(b); oFile.close();

    }

    private static String codificarBinario(ArrayList<Alfabeto> listaA, ArrayList<Character> listaC){
        int pos = checkExist(listaA,listaC.get(0));
        BigDecimal L = listaA.get(pos).getL();
        BigDecimal H = listaA.get(pos).getH();

        for(int i=1; i<listaC.size(); i++){
            pos = checkExist(listaA,listaC.get(i));
            BigDecimal Ln = listaA.get(pos).getL();
            BigDecimal Hn = listaA.get(pos).getH();
            BigDecimal Lj = L;
            L = L.add((H.subtract(L)).multiply(Ln));
            H = Lj.add((H.subtract(Lj)).multiply(Hn));
        }
        L = L.setScale(DECIMALS, RoundingMode.HALF_UP);
        H = L.setScale(DECIMALS, RoundingMode.HALF_UP);

        String cod = getCodNumber(getBinary(L),getBinary(H));
        return cod;
    }

    private static String getCodNumber(String L, String H){
        String cod = L;
        for(int i=2; i<L.length(); i++){
            if(Character.getNumericValue(L.charAt(i))<Character.getNumericValue(H.charAt(i))){
                if(i!=(H.length()-1)){
                    cod = L.substring(0,i)+"1";
                    break;
                }
            }
        }

        return cod;
    }

    private static ArrayList<Alfabeto> generarListaAlfabeto(String alf){
        ArrayList<Alfabeto> lista = new ArrayList<Alfabeto>();

        for(int i=0; i<alf.length(); i++){
            char c = alf.charAt(i);
            int x = checkExist(lista, c);
            if(x==-1){
                Alfabeto newletter = new Alfabeto(c);
                lista.add(newletter);
            }else{
                lista.get(x).aumentarFrecuencia();
            }
        }

        return lista;
    }

    private static ArrayList<Character> generarListaMsg(String msg) throws FileNotFoundException {
        ArrayList<Character> lista = new ArrayList<>();
        for(int i=0; i<msg.length(); i++){
            lista.add(msg.charAt(i));
        }

        return lista;
    }

    private static int checkExist(ArrayList<Alfabeto> list, char c){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getChar()==c){ return i; }
        }

        return -1;
    }

    private static void dividirFuente(ArrayList<Alfabeto> lista){
        int total=0;
        for(int i=0; i<lista.size(); i++){
            total+=lista.get(i).getFrecuencia();
        }

        BigDecimal div = new BigDecimal(1).divide(BigDecimal.valueOf(total), DECIMALS, RoundingMode.HALF_UP);
        BigDecimal actualDiv= BigDecimal.valueOf(0);
        for(int i=0; i<lista.size(); i++){
            BigDecimal nxtDiv = actualDiv.add(div.multiply(BigDecimal.valueOf(lista.get(i).getFrecuencia())));
            if(i==lista.size()-1) nxtDiv= BigDecimal.valueOf(1);
            lista.get(i).setLH(actualDiv,nxtDiv);
            actualDiv=nxtDiv;
        }
    }

    private static String getBinary(BigDecimal num){
        String binary = "";
        int parteEntera = num.intValue();

        //COVERTIMOS LA PARTE ENTERA
        while(true){
            if(parteEntera==0){
                binary=(parteEntera)+binary;
                break;
            }
            binary=(parteEntera%2)+binary;
            parteEntera = parteEntera/2;
            if(parteEntera==1){
                binary=(parteEntera)+binary;
                break;
            }
        }


        binary+=".";
        BigDecimal parteDecimal = num.remainder(BigDecimal.ONE);
        BigInteger exactDecimal = parteDecimal.movePointRight(parteDecimal.scale()).abs().toBigInteger();

        //CONVERTIMOS LA PARTE DECIMAL
        String decimal=""; int iter=0;

        while(!exactDecimal.equals(new BigInteger("0")) && iter<(DECIMALS*4)){
            parteDecimal = parteDecimal.multiply(new BigDecimal(2));
            decimal+=parteDecimal.intValue();
            iter++;
            if(parteDecimal.intValue()>=1) parteDecimal = parteDecimal.remainder(BigDecimal.ONE);
            exactDecimal = parteDecimal.movePointRight(parteDecimal.scale()).abs().toBigInteger();
        }

        binary += decimal;
        return binary;
    }

}


