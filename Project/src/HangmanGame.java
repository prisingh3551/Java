import java.util.Arrays;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.awt.event.FocusEvent;

public class HangmanGame extends JFrame implements ActionListener{
    Container c;
    JPanel p1, p2, bg;
    static final int MAX_TRIAL = 6;
    String wordToGuess;
    char[] guessedWord;
    int attempts;
    JLabel wordLabel;
    JLabel attemptsLabel;
    JLabel guessLabel;
    JLabel alreadyUsedChar;
    boolean[] usedChar;
    JTextField guessChar;
    JButton enter, reset;
    Font font1, font2;
    public HangmanGame() {
        super("Main");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        font2 = new Font("Bungee Outline", Font.BOLD, 14);
        font1 = new Font("Bungee Spice Regular", Font.ITALIC, 14);
        c = getContentPane();

        bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage image = ImageIO.read(new File("theme.jpeg")); 
                    Graphics2D g2d = (Graphics2D) g.create();
                    float opacity = 0.5f;
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    g2d.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        bg.setLayout(null);

        p1 = new JPanel();
        p1.setBounds(0, 0, 350, 500);
        p1.setOpaque(false);

        p2 = new JPanel();
        p2.setLayout(null);
        p2.setBounds(350, 0, 450, 600);
        p2.setOpaque(false); 

        attemptsLabel = new JLabel("Attempts left");
        attemptsLabel.setBounds(10, 50, 400, 60);
        attemptsLabel.setFont(font1);

        wordLabel = new JLabel("Word To Guess : ");
        wordLabel.setBounds(10, 120, 400, 60);
        wordLabel.setFont(font1);

        guessLabel = new JLabel("Enter a character to guess : ");
        guessLabel.setBounds(10, 190, 250, 60);
        guessLabel.setFont(font1);

        guessChar = new JTextField(1);
        guessChar.setBounds(260, 190, 50, 40);

        enter = new JButton("Enter");
        enter.setBounds(100, 300, 100, 50);
        
        enter.addActionListener(this);

        // Add focus listener to the button
        guessChar.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Do nothing
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Set focus back to the text field when button loses focus
                guessChar.requestFocusInWindow();
            }
        });

        alreadyUsedChar = new JLabel("Characters Used : ");
        alreadyUsedChar.setBounds(10, 350, 450, 100);
        alreadyUsedChar.setFont(font1);

        
        Draw draw = new Draw();
        p1.add(draw); // Add Draw component to p1 to display the hangman figure
        
        
        p2.add(attemptsLabel);
        p2.add(wordLabel);
        p2.add(guessLabel);
        p2.add(guessChar);
        p2.add(enter);

        p2.add(alreadyUsedChar);

        bg.add(p1);
        bg.add(p2);

        reset = new JButton("RESET");
        reset.setBounds(230, 300, 100, 50);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        p2.add(reset);
        resetGame(); // initial call to start game

        c.add(bg);

        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String input = guessChar.getText().trim();
        guessChar.setText("");
        char guess = input.charAt(0);
        boolean found = false;

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guess) {
                guessedWord[i] = guess;
                found = true;
            }
        }

        if (!found) {
            attempts++;
            setAttempts(attempts);
        }

        wordLabel.setText("Word To Guess : " + String.valueOf(guessedWord));
        attemptsLabel.setText("Attempts left: " + (MAX_TRIAL - attempts));
        alreadyUsedChar.setText(alreadyUsedChar.getText() + "\t" + guess);

        if (String.valueOf(guessedWord).equals(wordToGuess)) {
            JOptionPane.showMessageDialog(this, "Congratulations! You guessed the word: " + wordToGuess);
            resetGame();
        } else if (attempts >= MAX_TRIAL) {
            JOptionPane.showMessageDialog(this, "Sorry, you ran out of attempts. The word was: " + wordToGuess);
            resetGame();
        }
    }
    private void setAttempts(int attempts) {
        this.attempts = attempts;
        p1.repaint(); // Trigger the panel to repaint
    }


    private class Draw extends JPanel {

        public Draw() 
        {
            this.setOpaque(false);
            setPreferredSize(new Dimension(400, 600));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(3));

            if(attempts >= 0)
            {
                g2d.drawLine(100, 100, 100, 400); // vertical line
                g2d.drawLine(50, 400, 350, 400); // baseline
                g2d.drawLine(100, 350, 60, 400); // left slant line bottom
                g2d.drawLine(100, 350, 140, 400); // right slant line bottom
                g2d.drawLine(80, 100, 270, 100); // top line
                g2d.drawLine(100, 150, 140, 100); // slant line top          
            }

            if (attempts >= 1)
                g2d.drawLine(200, 100, 200, 170);
            if (attempts >= 2)
                g2d.drawArc(180, 170, 40, 40, 0, 360);
            if(attempts >= 3)
            {
                g2d.drawLine(200, 210, 200, 250);
            }   
            if(attempts >= 4)
            {
                g2d.drawLine(200, 230, 250, 200);
                g2d.drawLine(200, 230, 150, 200);
            }
            if(attempts >= 5)
            {
                g2d.drawLine(200, 250, 160, 300);
                g2d.drawLine(200, 250, 240, 300);
            }
            if (attempts >= 6) {
                g2d.drawLine(185, 190, 190, 185); //eye1
                g2d.drawLine(190, 190, 185, 185);
                g2d.drawLine(210, 190, 215, 185); // eye2
                g2d.drawLine(215, 190, 210, 185);
                
                g2d.drawArc(193, 200, 15, 10, 180, -180); // mouth

            }
        }
    }

    private void resetGame()
    {
        String randomWord = fetchRandomWord();
        wordToGuess = randomWord;
        guessedWord = new char[wordToGuess.length()];
        for(int i = 0; i < wordToGuess.length(); i++)
        {
            if(wordToGuess.charAt(i) == ' ')
            {
                guessedWord[i] = ' ';
            }
            else
            {
                guessedWord[i] = '_';
            }
        }
        attempts = 0;
        wordLabel.setText("Word To Guess : " + String.valueOf(guessedWord));
        attemptsLabel.setText("Attempts left : " + (MAX_TRIAL - attempts));
        alreadyUsedChar.setText("Characters Used : ");
        usedChar = new boolean[26];
        Arrays.fill(usedChar, false);
        p1.repaint();
    }

    private String fetchRandomWord() {
        String apiUrl = "https://random-word-api.herokuapp.com/word";
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void main(String[] args) {
        new HangmanGame();
    }
}
