import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Criando a janela
        JFrame frame = new JFrame("Gerador de Senhas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);
        frame.setLayout(null); // Usando layout nulo para posicionamento manual

        // Criando legendas
        JLabel labelMaxCaracteres = new JLabel("Máximo de Caracteres:");
        labelMaxCaracteres.setBounds(50, 20, 300, 30);

        JLabel labelMaiuscula = new JLabel("Quantidade de Maiúsculas:");
        labelMaiuscula.setBounds(50, 70, 300, 30);

        JLabel labelMinuscula = new JLabel("Quantidade de Minúsculas:");
        labelMinuscula.setBounds(50, 120, 300, 30);

        JLabel labelNumeros = new JLabel("Quantidade de Números:");
        labelNumeros.setBounds(50, 170, 300, 30);

        JLabel labelEspeciais = new JLabel("Quantidade de Caracteres Especiais:");
        labelEspeciais.setBounds(50, 220, 300, 30);

        // Criando campos para entrada de dados
        JTextField inputMaxCaracteres = new JTextField();
        inputMaxCaracteres.setBounds(50, 50, 300, 30); // Posição e tamanho

        JFormattedTextField inputMaiuscula = new JFormattedTextField(NumberFormat.getIntegerInstance());
        inputMaiuscula.setBounds(50, 100, 300, 30);

        JFormattedTextField inputMinuscula = new JFormattedTextField(NumberFormat.getIntegerInstance());
        inputMinuscula.setBounds(50, 150, 300, 30);

        JFormattedTextField inputNumeros = new JFormattedTextField(NumberFormat.getIntegerInstance());
        inputNumeros.setBounds(50, 200, 300, 30);

        JFormattedTextField inputEspeciais = new JFormattedTextField(NumberFormat.getIntegerInstance());
        inputEspeciais.setBounds(50, 250, 300, 30);

        // Criando botões
        JButton gerarSenhaButton = new JButton("Gerar Senha");
        gerarSenhaButton.setBounds(50, 300, 300, 30); // Posição e tamanho do botão

        JButton gerarEstouComPressaButton = new JButton("Estou Com Pressa!");
        gerarEstouComPressaButton.setBounds(50, 350, 300, 30); // Posição e tamanho do botão

        // Adicionando ação aos botões
        gerarSenhaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtendo os valores digitados
                try {
                    int numeroMaximoDeCaracteresNaSenha = Integer.parseInt(inputMaxCaracteres.getText());
                    int quantidadeDeMaiuscula = ((Number) inputMaiuscula.getValue()).intValue();
                    int quantidadeDeMinuscula = ((Number) inputMinuscula.getValue()).intValue();
                    int quantidadeDeNumeros = ((Number) inputNumeros.getValue()).intValue();
                    int quantidadeDeCaracteresEspeciais = ((Number) inputEspeciais.getValue()).intValue();
                    // Gerando a senha
                    String senha = gerarSenha(numeroMaximoDeCaracteresNaSenha, quantidadeDeMaiuscula, quantidadeDeMinuscula, quantidadeDeNumeros, quantidadeDeCaracteresEspeciais);
                    // Salvando a senha
                    if(senha.length() < 1){
                        mostrarAlerta("senha muito curta ou precisa adicionar menos tipos diferentes de caracteres.");
                    } else {
                        salvarSenha(senha);
                        mostrarSucesso("A senha " + senha + " foi copiada para a sua area de transferência com sucesso!");
                        // Copiando a senha para a área de transferência
                        copiarParaAreaDeTransferencia(senha);
                    }
                } catch (Exception erro){
                    mostrarAlerta("Valores nulos ou inválidos nos campos de input");
                    System.out.println(erro);
                }

            }
        });

        gerarEstouComPressaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Gerando a senha
                String senha = gerarSenha( numeroAleatorio(8, 14), 1, 1, 1, 1);
                // Salvando a senha
                salvarSenha(senha);
                // Copiando a senha para a área de transferência
                copiarParaAreaDeTransferencia(senha);
                mostrarSucesso("A senha " + senha + " foi copiada para a sua area de transferência com sucesso!");
            }
        });

        // Adicionando os componentes à janela
        frame.add(labelMaxCaracteres);
        frame.add(inputMaxCaracteres);
        frame.add(labelMaiuscula);
        frame.add(inputMaiuscula);
        frame.add(labelMinuscula);
        frame.add(inputMinuscula);
        frame.add(labelNumeros);
        frame.add(inputNumeros);
        frame.add(labelEspeciais);
        frame.add(inputEspeciais);
        frame.add(gerarSenhaButton);
        frame.add(gerarEstouComPressaButton);

        // Exibindo a janela
        frame.setVisible(true);
    }

    // Método para copiar a senha para a área de transferência
    public static void copiarParaAreaDeTransferencia(String senha) {
        StringSelection stringSelection = new StringSelection(senha);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    // Método para mostrar alerta
    public static void mostrarAlerta(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }
    // Método para mostrar tela de sucesso
    public static void mostrarSucesso(String mensagem) {
        JOptionPane.showMessageDialog(null, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    private static void salvarSenha(String senha){
        // Nome do arquivo onde as senhas serão salvas
        String nomeDoArquivo = "senhas.txt";

        // Usando try-with-resources para garantir que o arquivo será fechado corretamente
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeDoArquivo, true))) {
            // Escrevendo a senha no arquivo
            writer.write(senha);
            writer.newLine(); // Adiciona uma nova linha após a senha
            System.out.println("Senha salva com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    private static String gerarSenha(
            int quantidadeDeCaracteresSenha,
            int quantidadeDeMaiuscula,
            int quantidadeDeMinuscula,
            int quantidadeDeNumeros,
            int quantidadeDeCaracteresEspeciais
){

        if(quantidadeDeCaracteresSenha < quantidadeDeMaiuscula + quantidadeDeMinuscula + quantidadeDeNumeros + quantidadeDeCaracteresEspeciais){
            System.out.println("Senha muito curta ou precisa adicionar menos tipos diferentes de caracteres.");
            return "";
        }

        char[] numeros = Main.numeros();
        char[] maiusculas = Main.letrasMaiusculas();
        char[] minusculas = Main.letrasMinusculas();
        char[] caracteresEspeciais = { '!', '@', '#', '$', '%', '&', '*'};
        List<Character> senha = new ArrayList<>();
        // gerando a senha
        for (int i = 0; i < quantidadeDeCaracteresSenha; i++){
            if (quantidadeDeMinuscula > 0){
                int seletor = Main.numeroAleatorio(0, 25);
                senha.add(minusculas[seletor]);
                quantidadeDeMinuscula--;
                continue;
            }
            if (quantidadeDeMaiuscula > 0){
                int seletor = Main.numeroAleatorio(0, 25);
                senha.add(maiusculas[seletor]);
                quantidadeDeMaiuscula--;
                continue;
            }
            if (quantidadeDeCaracteresEspeciais > 0){
                int seletor = Main.numeroAleatorio(0, caracteresEspeciais.length - 1);
                senha.add(caracteresEspeciais[seletor]);
                quantidadeDeCaracteresEspeciais--;
                continue;
            }
            if (quantidadeDeNumeros > 0){
                int seletor = Main.numeroAleatorio(0, 9);
                senha.add(numeros[seletor]);
                quantidadeDeNumeros--;
                continue;
            }

            int seletorTipoCaracter = Main.numeroAleatorio(1, 4);

            if (seletorTipoCaracter == 1){
                int seletor = Main.numeroAleatorio(0, 25);
                senha.add(minusculas[seletor]);
            } else if(seletorTipoCaracter == 2){
                int seletor = Main.numeroAleatorio(0, 25);
                senha.add(maiusculas[seletor]);
            } else if(seletorTipoCaracter == 3){
                int seletor = Main.numeroAleatorio(0, caracteresEspeciais.length - 1);
                senha.add(caracteresEspeciais[seletor]);
            } else {
                int seletor = Main.numeroAleatorio(0, 9);
                senha.add(numeros[seletor]);
            }
        }
        Collections.shuffle(senha);
        String senhaString = "";
        for (Character caractere : senha){
            senhaString += caractere;
        }
        System.out.println("Senha gerada: " + senhaString); // Mostrando no console
        return senhaString;
    }

    private static int numeroAleatorio(int minimo, int maximo){
        // Criando um objeto Random
        Random random = new Random();
        // Gerando um número inteiro aleatório entre min (inclusivo) e max (exclusivo)
        return random.nextInt((maximo - minimo) + 1) + minimo;
    }

    private static char[] numeros(){
        char[] numeros = new char[10];
        for (int i = 0; i < 10; i++) {
            numeros[i] = (char) ('0' + i); // Gera letras minúsculas de a a z
        }
        return numeros;
    }

    private static char[] letrasMinusculas(){
        char[] minusculas = new char[26];
        for (int i = 0; i < 26; i++) {
            minusculas[i] = (char) ('a' + i); // Gera letras minúsculas de a a z
        }
        return minusculas;
    }
    private static char[] letrasMaiusculas(){
        char[] maiusculas = new char[26];
        for (int i = 0; i < 26; i++) {
            maiusculas[i] = (char) ('A' + i); // Gera letras minúsculas de a a z
        }
        return maiusculas;
    }
}