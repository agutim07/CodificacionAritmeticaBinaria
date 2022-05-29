import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Alberto Gutiérrez Morán
 */

public class Main {

    public static int DECIMALS = 200;

    public static void main(String[] args) throws FileNotFoundException {
        /** IMPORTAMOS EL CÓDIGO CODIFICADO EN BINARIO DE UN ARCHIVO TXT */
        File file2 = new File("D:\\Alberto GM\\ULE\\3º\\SI\\binaryString.txt");

        /** FUENTE DE INFORMACIÓN */
        String alfabeto = "AÁBCDEÉFGHIÍJKLMNÑOÓPQRSTUÚVWXYZ .,;:()¿?¡!-0123456789aábcdeéfghiíjklmnñoópqrstuúvwxyz";
        ArrayList<Alfabeto> lista = generarListaAlfabeto(alfabeto);
        dividirFuente(lista);

        /** NUMERO BINARIO A MENSAJE */
        String codificacionBinaria = obtenerCodigoBinario(file2);
        BigDecimal num = getDecimal(codificacionBinaria);
        int longuitud=24;


        String msg = calcularMensaje(longuitud, num, lista);
        System.out.println("El mensaje codificado es: "+msg);
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

    private static String obtenerCodigoBinario(File file) throws FileNotFoundException{
        Scanner sc = new Scanner(file);
        String cod = "";
        while (sc.hasNext()){ cod+=sc.next(); }

        sc.close();
        return cod;
    }

    private static int checkExist(ArrayList<Alfabeto> list, char c){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getChar()==c){ return i; }
        }

        return -1;
    }

    private static String calcularMensaje(int lon, BigDecimal num, ArrayList<Alfabeto> list){
        String[] msg = new String[lon];
        BigDecimal numActual = num;
        for(int i=0; i<lon; i++){
            BigDecimal Lj = new BigDecimal(0);
            BigDecimal Hj = new BigDecimal(0);
            for(int x=0; x<list.size(); x++){
                int bool = numActual.compareTo(list.get(x).getL());
                int bool2 = numActual.compareTo(list.get(x).getH());
                if(bool>=0 && bool2==-1){
                    Lj = list.get(x).getL();
                    Hj = list.get(x).getH();
                    msg[i] = String.valueOf(list.get(x).getChar());
                    break;
                }
            }
            numActual = (numActual.subtract(Lj)).divide(Hj.subtract(Lj), DECIMALS, RoundingMode.HALF_UP);
        }

        /** STRING[] A STRING */
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < msg.length; i++) {
            sb.append(msg[i]);
        }

        return sb.toString();
    }

    private static BigDecimal getDecimal(String binary){
        BigDecimal decimal = new BigDecimal("0");
        int start=-1;
        for(int i=0; i<binary.length(); i++){
            if(binary.charAt(i)!='.'){
                start++;
            }else{
                break;
            }
        }

        for(int i=0; i<binary.length(); i++){
            if(binary.charAt(i)!='.'){
                if(binary.charAt(i)=='1'){
                    if(start>=0){
                        decimal = decimal.add(BigDecimal.valueOf(2).pow(start));
                    }else{
                        MathContext mc = new MathContext(DECIMALS);
                        decimal = decimal.add(BigDecimal.valueOf(2).pow(start,mc));
                    }
                }
                start--;
            }

        }

        return decimal.setScale(DECIMALS, RoundingMode.HALF_UP);
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

}


