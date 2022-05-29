import java.math.BigDecimal;

public class Alfabeto {
    private char caracter;
    private int frecuencia;
    private BigDecimal L;
    private BigDecimal H;

    Alfabeto(char c){
        this.frecuencia=1;
        this.caracter = c;
    }

    public void setLH(BigDecimal L, BigDecimal H) {this.L = L; this.H = H;}

    ///GETTERS///
    public char getChar(){ return this.caracter; }
    public int getFrecuencia(){ return this.frecuencia; }
    public void aumentarFrecuencia(){ this.frecuencia++; }

    public BigDecimal getL(){return L;}
    public BigDecimal getH(){return H;}


}
