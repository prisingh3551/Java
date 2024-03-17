
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.Arrays;

public class Hangman extends JFrame {
    Container c;
    Panel p1, p2;
    static final String[] WORDS = {"hello", "world", "java", "hangman", "programming"};
    static final int MAX_TRIAL = 7;
    String wordToGuess;
    char[] guessedWord;
    int attempts;
    JLabel wordLabel;
    JLabel attemptsLabel;
    public Hangman() {
        super("Main");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        c = getContentPane();
        GridLayout gl = new GridLayout(1, 2, 0, 0);
        c.setLayout(gl);

        wordLabel = new JLabel();
        wordLabel.setBounds(100, 600, 100, 50);
        attemptsLabel = new JLabel();

        p1 = new Panel();
        p1.setBackground(Color.LIGHT_GRAY);
        Draw draw = new Draw();
        p1.add(draw); // Add Draw component to p1

        p2 = new Panel();
        p2.setBackground(Color.RED);
        p2.add(wordLabel);
        p2.add(attemptsLabel);

        c.add(p1);
        c.add(p2);

        resetGame();

        setVisible(true);
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
        new Hangman();
    }
}

