package pe.edu.upeu.calcfx.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;
import java.util.Stack;
import java.util.EmptyStackException;

@Controller
public class CalcfxControl {

    @FXML
    private TextField txtResultado;

    private void escribirNumero(String numero) {
        txtResultado.appendText(numero);
    }

    private void agregarOperador(String operador) {

        if(!txtResultado.getText().isEmpty() && txtResultado.getText().length()>=4){
            char op = txtResultado.getText().charAt(txtResultado.getText().length()-2);
            if(txtResultado.getText().toString().contentEquals(String.valueOf(op))){
                txtResultado.appendText(" " + operador + " ");
            }
        }else{
            txtResultado.appendText(" " + operador + " ");
        }
    }


    private void calcularResultado() {
        try {
            String[] tokens = txtResultado.getText().split(" ");
            if (tokens.length < 3) {
                return;
            }
            double num1 = Double.parseDouble(tokens[0]);
            String operador = tokens[1];
            double num2 = Double.parseDouble(tokens[2]);
            double resultado = 0;
            switch (operador) {
                case "+":
                    resultado = num1 + num2;
                    break;
                case "-":
                    resultado = num1 - num2;
                    break;
                case "*":
                    resultado = num1 * num2;
                    break;
                case "/":
                    if (num2 != 0) {
                        resultado = num1 / num2;
                    } else {
                        txtResultado.setText("Error: Div/0");
                        return;
                    }
                    break;
            }
            String[] dd=String.valueOf(resultado).split("\\.");
            System.out.println(dd.length);

            if (dd.length == 2 && dd[1].equals("0")) {
                txtResultado.setText(String.valueOf(dd[0]));
            }else{
                txtResultado.setText(String.valueOf(resultado));
            }

        } catch (Exception e) {
            txtResultado.setText("Error");
            System.out.println(e.getMessage());
        }
    }

    private void calcularPotencia(double base, double exponente) {
        double resultado = Math.pow(base, exponente);
        if (resultado == (int) resultado) {
            txtResultado.setText(String.valueOf((int) resultado));
        } else {
            txtResultado.setText(String.valueOf(resultado));
        }
    }

    private void calcularRaizCuadrada(double numero) {
        if (numero < 0) {
            txtResultado.setText("Error: Raíz de número negativo");
        } else {
            double resultado = Math.sqrt(numero);

            if (resultado == (int) resultado) {
                txtResultado.setText(String.valueOf((int) resultado));
            } else {
                txtResultado.setText(String.valueOf(resultado));
            }
        }
    }

    private void calcularInverso(double numero) {
        if (numero == 0) {
            txtResultado.setText("Error: Div/0");
        } else {
            double resultado = 1 / numero;
            txtResultado.setText(String.valueOf(resultado));
        }
    }

    private void calcularPi() {
        txtResultado.setText(String.valueOf(Math.PI));
    }

    private void  convertirADecimalABinario(int numero) {
        String resultado = Integer.toBinaryString(numero);
        txtResultado.setText(resultado);
    }

    private void analizarExpresion(String expresion) {
        expresion = expresion.replaceAll("\\s+", "");

        String[] tokens = expresion.split("(?<=[-+*/^])|(?=[-+*/^])");

        Stack<Double> numeros = new Stack<>();
        Stack<String> operadores = new Stack<>();

        try {
            for (String token : tokens) {
                if (token.matches("[0-9]+(\\.[0-9]+)?")) {
                    numeros.push(Double.parseDouble(token));
                } else if (token.matches("[-+*/^]")) {
                    while (!operadores.isEmpty() && precedencia(token) <= precedencia(operadores.peek())) {
                        double num2 = numeros.pop();
                        double num1 = numeros.pop();
                        String operador = operadores.pop();
                        double resultado = calcular(num1, num2, operador);
                        numeros.push(resultado);
                    }
                    operadores.push(token);
                } else {
                    txtResultado.setText("Error: Operador no válido");
                    return;
                }
            }

            while (!operadores.isEmpty()) {
                double num2 = numeros.pop();
                double num1 = numeros.pop();
                String operador = operadores.pop();
                double resultado = calcular(num1, num2, operador);
                numeros.push(resultado);
            }

            double resultadoFinal = numeros.pop();
            if (resultadoFinal == (int) resultadoFinal) {
                txtResultado.setText(String.valueOf((int) resultadoFinal));
            } else {
                txtResultado.setText(String.valueOf(resultadoFinal));
            }

        } catch (EmptyStackException e) {
            txtResultado.setText("Error: Expresión no válida");
        } catch (NumberFormatException e) {
            txtResultado.setText("Error: Formato de número no válido");
        } catch (Exception e) {
            txtResultado.setText("Error: " + e.getMessage());
        }
    }

    private double calcular(double num1, double num2, String operador) {
        switch (operador) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    throw new ArithmeticException("División por cero");
                }
            case "^":
                return Math.pow(num1, num2);
            default:
                throw new IllegalArgumentException("Operador no válido");
        }
    }

    private int precedencia(String operador) {
        switch (operador) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }


    @FXML
    private void controlClick(ActionEvent event) {
        Button boton = (Button) event.getSource();
        switch (boton.getId()) {
            case "btn0", "btn1", "btn2", "btn3", "btn4", "btn5", "btn6", "btn7", "btn8", "btn9": {
                escribirNumero(boton.getText());
            } break;
            case "btnDiv", "btnMult", "btnRest", "btnSum": {
                agregarOperador(boton.getText());
            } break;
            case "btnPotencia": {
                double base = Double.parseDouble(txtResultado.getText());
                double exponente = 2;
                calcularPotencia(base, exponente);
            } break;
            case "btnRaiz": {
                double numero = Double.parseDouble(txtResultado.getText());
                calcularRaizCuadrada(numero);
            } break;
            case "btnInverso": {
                double numero = Double.parseDouble(txtResultado.getText());
                calcularInverso(numero);
            } break;
            case "btnPi": {
                calcularPi();
            } break;
            case "btnDecimalABinario": {
                int numero = Integer.parseInt(txtResultado.getText());
                convertirADecimalABinario(numero);
            } break;
            case "btnAnalizar": {
                String expresion = txtResultado.getText();
                analizarExpresion(expresion);
            } break;
            case "btnBorrar": {
                txtResultado.setText("");
            } break;
            case "btnIgual": {
                calcularResultado();
            } break;
            default: {} break;
        }
    }

}
