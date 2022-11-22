public class Complex {
    double rPart;
    double imPart;

    public Complex(double rPart, double imPart) {
        this.rPart = rPart;
        this.imPart = imPart;
    }

    // Сложение
    public Complex Add(Complex complex1, Complex complex2){
        Complex complex = new Complex(complex1.getReal() + complex2.getReal(),  complex1.getIm() + complex2.getIm());
        return complex;
    }

    public Complex Add(Complex complex1, double num) {
        Complex complex = new Complex(complex1.getReal() + num, complex1.getIm());
        return complex;
    }

    public Complex Add(double num, Complex complex1) {
        Complex complex = new Complex(num + complex1.getReal(), complex1.getIm());
        return complex;
    }


    // Вычитание 
    public Complex Sub(Complex complex1, Complex complex2){
        Complex complex = new Complex(complex1.getReal() - complex2.getReal(), complex1.getIm() - complex2.getIm());
        return complex;
    }

    public Complex Sub(Complex complex1, double num) {
        Complex complex = new Complex(complex1.getReal() - num, complex1.getIm());
        return complex;
    }

    public Complex Sub(double num, Complex complex1) {
        Complex complex = new Complex(num - complex1.getReal(), - complex1.getIm());
        return complex;
    }


    // Умножение
    public Complex Mul(Complex complex1, Complex complex2){
        Complex complex = new Complex(complex1.getReal() * complex2.getReal() - complex1.getIm() * complex2.getIm(),
         complex1.getReal() * complex2.getIm() + complex1.getIm() * complex2.getReal());
        return complex;
    }

    public Complex Mul(Complex complex1, double num) {
        Complex complex = new Complex(complex1.getReal() * num, complex1.getIm() * num);
        return complex;
    }

    public Complex Mul(double num, Complex complex1) {
        Complex complex = new Complex(complex1.getReal() * num, complex1.getIm() * num);
        return complex;
    }

    // Деление
    public Complex Div(Complex complex1, Complex complex2){
        Complex complex = new Complex((complex1.getReal() * complex2.getReal() + complex1.getIm() * complex2.getIm()) / (Math.pow(complex2.getReal(), 2) + Math.pow(complex2.getIm(), 2)), (complex2.getReal() * complex1.getIm() - complex1.getReal() * complex2.getIm()) / (Math.pow(complex2.getReal(), 2) + Math.pow(complex2.getIm(), 2)));
        return complex;
    }

    public Complex Div(Complex complex1, double num) {
        Complex complex = new Complex(complex1.getReal() / num, complex1.getIm() / num);
        return complex;
    }

    public Complex Div(double num, Complex complex1) {
        Complex complexNum = new Complex(0, 0);
        double complexDenom;
        complexNum = Mul(num, invertComplex(complex1));
        complexDenom = Mul(complex1, invertComplex(complex1)).getReal(); 
        Complex complex = new Complex(complexNum.getReal() / complexDenom, complexNum.getIm() / complexDenom);
        return complex;
    }

    public Complex Pow(double num, Complex complex1){
        Complex complex = new Complex(0, 0);
        complex.setReal(complex1.getReal());
        complex.setIm(complex1.getIm());
        for(int i = 0; i < num - 1; i++){
            complex = complex.Mul(complex, complex1);
        }
        return complex;
    }

    // Поиск сопряженного
    public Complex invertComplex(Complex complex) {
        complex.imPart = -complex.imPart;
        return complex;
    }

    // СЕТТЕРЫ
    // Сеттер реальной части
    public void setReal(double rPart) {
        this.rPart = rPart;
    }

    // Сеттер мнимой части
    public void setIm(double imPart) {
        this.imPart = imPart;
    }

    // ГЕТТЕРЫ
    // Геттер реальной части
    public double getReal() {
        return rPart;
    }

    // Геттер мнимой части
    public double getIm() {
        return imPart;
    }

    public String toString(){
        return String.valueOf(getReal()) + " " + String.valueOf(getIm()) + "i ";
    }
}
