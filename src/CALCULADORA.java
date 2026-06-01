import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CALCULADORA extends JFrame {
    
    private JTextField pantalla;
    private String operador = "";
    private double n1 = 0, n2 = 0;
    private boolean nuevoNumero = true;
    
    public CALCULADORA() {
        setTitle("Calculadora");
        setSize(450, 650);
        setBackground(new Color(255, 210, 225));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Pantalla
        pantalla = new JTextField();
        pantalla.setEditable(false);
        pantalla.setHorizontalAlignment(SwingConstants.RIGHT);
        estilodisplay(pantalla);
        add(pantalla, BorderLayout.NORTH);
        
        // Panel de botones
        JPanel Botones = new JPanel();
        Botones.setBackground(new Color(255, 210, 225));
        Botones.setLayout(new GridLayout(5, 4, 10, 10));
        Botones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] Opciones = {
            "C", "DEL", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "=", "PIC"
        };
        
        for (String opc : Opciones) {
            JButton boton = new JButton(opc);
            
            if (opc.matches("[0-9]")) {
                estilonumeros(boton);
            } else if (opc.equals(".")) {
                estilonumeros(boton);
            } else if (opc.matches("[+\\-*/%]")) {
                estiloperador(boton);
            } else if (opc.equals("=")) {
                estiloigual(boton);
            } else if (opc.equals("C") || opc.equals("DEL")) {
                estiloborrado(boton);
            } else if (opc.equals("PIC")) {
                estilopic(boton);
            }
            
            if (opc.equals("PIC")) {
                boton.addActionListener(e -> mostrarFoto());
            } else {
                boton.addActionListener(new accionesBotones());
            }
            
            Botones.add(boton);
        }
        
        add(Botones, BorderLayout.CENTER);
        
        // ========== CONFIGURACIÓN DEL TECLADO ==========
        configurarTeclado();
    }
    
    private void configurarTeclado() {
        // Hacer que la ventana y todos sus componentes puedan recibir el foco
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        // Crear el listener del teclado
        KeyAdapter manejadorTeclado = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("Tecla presionada: " + KeyEvent.getKeyText(e.getKeyCode())); // Para debug
                
                int key = e.getKeyCode();
                
                // Números (fila superior)
                if (key >= KeyEvent.VK_0 && key <= KeyEvent.VK_9) {
                    presionarNumero(String.valueOf((char) key));
                }
                // Números (teclado numérico)
                else if (key >= KeyEvent.VK_NUMPAD0 && key <= KeyEvent.VK_NUMPAD9) {
                    presionarNumero(String.valueOf(key - KeyEvent.VK_NUMPAD0));
                }
                // Punto decimal
                else if (key == KeyEvent.VK_PERIOD || key == KeyEvent.VK_DECIMAL) {
                    presionarPunto();
                }
                // Suma
                else if (key == KeyEvent.VK_PLUS || key == KeyEvent.VK_ADD) {
                    presionarOperador("+");
                }
                // Resta
                else if (key == KeyEvent.VK_MINUS || key == KeyEvent.VK_SUBTRACT) {
                    presionarOperador("-");
                }
                // Multiplicación
                else if (key == KeyEvent.VK_ASTERISK || key == KeyEvent.VK_MULTIPLY) {
                    presionarOperador("*");
                }
                // División
                else if (key == KeyEvent.VK_SLASH || key == KeyEvent.VK_DIVIDE) {
                    presionarOperador("/");
                }
                // Porcentaje (Shift + 5 o tecla %)
                else if (key == KeyEvent.VK_5 && e.isShiftDown()) {
                    presionarOperador("%");
                }
                // Enter o = para igual
                else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_EQUALS) {
                    presionarIgual();
                }
                // Escape para borrar todo
                else if (key == KeyEvent.VK_ESCAPE) {
                    presionarBorrarTodo();
                }
                // Tecla C para borrar todo
                else if (key == KeyEvent.VK_C) {
                    presionarBorrarTodo();
                }
                // Backspace o Delete para borrar un carácter
                else if (key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE) {
                    presionarBorrarCaracter();
                }
            }
        };
        
        // Agregar el listener a la ventana
        this.addKeyListener(manejadorTeclado);
        
        // También agregar a la pantalla
        pantalla.addKeyListener(manejadorTeclado);
        
        // Agregar a todos los botones (para que el teclado funcione aunque el foco esté en un botón)
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component boton : panel.getComponents()) {
                    boton.addKeyListener(manejadorTeclado);
                }
            }
        }
        
        // Forzar que la ventana reciba el foco al iniciar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                requestFocusInWindow();
            }
        });
        
        // Al hacer clic en cualquier lugar, volver a enfocar la ventana
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        
        // También al hacer clic en la pantalla
        pantalla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        
        // Hacer que la ventana sea focusable siempre
        setFocusable(true);
    }
    
    private void presionarNumero(String numero) {
        if (nuevoNumero) {
            pantalla.setText(numero);
            nuevoNumero = false;
        } else {
            pantalla.setText(pantalla.getText() + numero);
        }
        requestFocusInWindow(); // Mantener el foco
    }
    
    private void presionarPunto() {
        if (!pantalla.getText().contains(".")) {
            pantalla.setText(pantalla.getText() + ".");
            nuevoNumero = false;
        }
        requestFocusInWindow();
    }
    
    private void presionarOperador(String op) {
        try {
            if (!operador.isEmpty() && !nuevoNumero) {
                calcular();
            }
            n1 = Double.parseDouble(pantalla.getText());
            operador = op;
            nuevoNumero = true;
        } catch (NumberFormatException error) {
            pantalla.setText("Error");
            nuevoNumero = true;
        }
        requestFocusInWindow();
    }
    
    private void presionarIgual() {
        try {
            if (!operador.isEmpty()) {
                calcular();
                operador = "";
                nuevoNumero = true;
            }
        } catch (NumberFormatException error) {
            pantalla.setText("Error");
        }
        requestFocusInWindow();
    }
    
    private void presionarBorrarTodo() {
        pantalla.setText("");
        n1 = 0;
        n2 = 0;
        operador = "";
        nuevoNumero = true;
        requestFocusInWindow();
    }
    
    private void presionarBorrarCaracter() {
        String actual = pantalla.getText();
        if (!actual.isEmpty()) {
            String nuevo = actual.substring(0, actual.length() - 1);
            pantalla.setText(nuevo.isEmpty() ? "0" : nuevo);
            nuevoNumero = false;
        }
        requestFocusInWindow();
    }
    
    private void mostrarFoto() {
        ImageIcon icono = new ImageIcon("1780320118114.jpg");
        
        if (icono.getIconWidth() == -1) {
            JOptionPane.showMessageDialog(this, 
                "No se encontró la imagen.\nAsegurate de que '1780320118114.jpg' esté en la carpeta del programa.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFrame imagen = new JFrame("Hola Te Amo, Para que te acuerdes de mi siempre");
        JLabel etiqueta = new JLabel(icono);
        imagen.add(etiqueta);
        imagen.pack();
        imagen.setLocationRelativeTo(this);
        imagen.setVisible(true);
        imagen.setResizable(false);
    }
    
    private class accionesBotones implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String comando = ((JButton) e.getSource()).getText();
            
            if (comando.matches("[0-9]")) {
                presionarNumero(comando);
            }
            else if (comando.equals(".")) {
                presionarPunto();
            }
            else if (comando.matches("[+\\-*/%]")) {
                presionarOperador(comando);
            }
            else if (comando.equals("=")) {
                presionarIgual();
            }
            else if (comando.equals("C")) {
                presionarBorrarTodo();
            }
            else if (comando.equals("DEL")) {
                presionarBorrarCaracter();
            }
            
            // Volver a enfocar la ventana después de hacer clic en un botón
            requestFocusInWindow();
        }
    }
    
    private void calcular() {
        n2 = Double.parseDouble(pantalla.getText());
        double resultado = 0;
        
        switch (operador) {
            case "+":
                resultado = n1 + n2;
                break;
            case "-":
                resultado = n1 - n2;
                break;
            case "*":
                resultado = n1 * n2;
                break;
            case "/":
                if (n2 != 0) {
                    resultado = n1 / n2;
                } else {
                    pantalla.setText("Error: Div/0");
                    nuevoNumero = true;
                    return;
                }
                break;
            case "%":
                resultado = n1 % n2;
                break;
            default:
                pantalla.setText("Error");
                return;
        }
        
        if (resultado == (long) resultado) {
            pantalla.setText(String.valueOf((long) resultado));
        } else {
            String formateado = String.format("%.8f", resultado);
            formateado = formateado.replaceAll("0*$", "").replaceAll("\\.$", "");
            pantalla.setText(formateado);
        }
        n1 = resultado;
        n2 = 0;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CALCULADORA calc = new CALCULADORA();
            calc.setVisible(true);
        });
    }
    
    // ========== ESTILOS ==========
    
    private void estilonumeros(JButton boton) {
        boton.setBackground(new Color(255, 200, 215));
        boton.setForeground(new Color(220, 20, 120));
        boton.setFont(new Font("Arial", Font.BOLD, 24));
        boton.setFocusPainted(false);
        /*boton.setBorderPainted(false);*/
        boton.setBorder(BorderFactory.createRaisedBevelBorder()); // Bisel elevado
        boton.setContentAreaFilled(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void estiloperador(JButton boton) {
        boton.setBackground(new Color(250, 160, 190));
        boton.setForeground(new Color(220, 20, 120));
        boton.setFont(new Font("Arial", Font.BOLD, 26));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder()); // Bisel elevado
        boton.setContentAreaFilled(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void estiloigual(JButton boton) {
        boton.setBackground(new Color(220, 20, 120));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 28));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLoweredBevelBorder()); // Bisel hundido
        boton.setContentAreaFilled(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void estiloborrado(JButton boton) {
        boton.setBackground(new Color(230, 90, 150));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 20));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder()); // Bisel elevado
        boton.setContentAreaFilled(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void estilopic(JButton boton) {
        boton.setBackground(new Color(200, 40, 100));
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createRaisedBevelBorder()); // Bisel elevado
        boton.setContentAreaFilled(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void estilodisplay(JTextField pantalla) {
        pantalla.setBackground(new Color(255, 210, 225));
        pantalla.setForeground(new Color(199, 21, 133));
        pantalla.setFont(new Font("Arial Black", Font.BOLD, 60));
        pantalla.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
    }
}