
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.util.Arrays;

public class HangmanGame extends JFrame implements ActionListener{
    Container c;
    Panel p1, p2;
    static final String[] WORDS = {"hello", "world", "java", "hangman", "programming"};
    static final int MAX_TRIAL = 7;
    String wordToGuess;
    char[] guessedWord;
    int attempts;
    JLabel wordLabel;
    JLabel attemptsLabel;
    JLabel guessLabel;
    JTextField guessChar;
    JButton reset;
    Font font1;
    public HangmanGame() {
        super("Main");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            font1 = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\Lenovo\\Downloads\\Bungee_Outline"));
            ge.registerFont(font1);
        } 
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        c = getContentPane();
        GridLayout gl = new GridLayout(1, 2, 0, 0);
        c.setLayout(gl);

        wordLabel = new JLabel("Secret Word");
        wordLabel.setFont(font1);
        guessLabel = new JLabel("Enter a character to guess");
        attemptsLabel = new JLabel("Attempts left");

        guessChar = new JTextField(1);
        guessChar.addActionListener(this);
        
        p1 = new Panel();
        p1.setBackground(Color.LIGHT_GRAY);
        Draw draw = new Draw();
        p1.add(draw); // Add Draw component to p1
        
        p2 = new Panel();
        p2.add(wordLabel);
        GridLayout gl2 = new GridLayout(8, 1, 2, 2);
        p2.setLayout(gl2);
        p2.add(attemptsLabel);
        p2.add(guessLabel);
        p2.add(guessChar);

        c.add(p1);
        c.add(p2);

        reset = new JButton("RESET");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        p2.add(reset, BorderLayout.SOUTH);

        resetGame(); // initial call to start game

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
    private class Draw extends Canvas {
        public Draw()
        {
            setPreferredSize(new Dimension(400, 600));
        }
        public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawLine(100, 100, 100, 400); // vertical line
            g.drawLine(50, 400, 350, 400); // baseline
            g.drawLine(100, 350, 60, 400); // left slant line bottom
            g.drawLine(100, 350, 140, 400); // right slant line bottom
            g.drawLine(80, 100, 270, 100); // top line
            g.drawLine(100, 150, 140, 100); // slant line top
            

            
        }
    }

    private void resetGame()
    {
        wordToGuess = selectRandomWord();
        guessedWord = new char[wordToGuess.length()];
        Arrays.fill(guessedWord, '_');
        attempts = 0;
        wordLabel.setText("Word To Guess : " + String.valueOf(guessedWord));
        attemptsLabel.setText("Attempts left : " + (MAX_TRIAL - attempts));
    }

    private static String selectRandomWord()
    {
        return WORDS[(int)(Math.random() * WORDS.length)];
    }
    public static void main(String[] args) {
        new HangmanGame();
    }
}
