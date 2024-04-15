
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class HangmanGame extends JFrame implements ActionListener{
    Container c;
    JPanel p1, p2, bg;
    static final String[] WORDS = {"hello", "world", "java", "hangman", "programming", "mini project"};
    static final int MAX_TRIAL = 6;
    String wordToGuess;
    char[] guessedWord;
    int attempts;
    JLabel wordLabel;
    JLabel attemptsLabel;
    JLabel guessLabel;
    JTextField guessChar;
    JButton reset;
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

        GridLayout gl = new GridLayout(1, 2, 0, 0);
        bg.setLayout(gl);

        
        p1 = new JPanel();
        p1.setOpaque(false);

        p2 = new JPanel();
        p2.setOpaque(false); 
        p2.setLayout(new GridLayout(6, 1, 10, 10));

        wordLabel = new JLabel("Secret Word");
        wordLabel.setBounds(600, 100, 40, 60);
        wordLabel.setFont(font1);
        guessLabel = new JLabel("Enter a character to guess");
        guessLabel.setFont(font1);

        attemptsLabel = new JLabel("Attempts left");
        attemptsLabel.setFont(font1);
        
        guessChar = new JTextField(1);
        guessChar.addActionListener(this);
        
        Draw draw = new Draw();
        p1.add(draw); // Add Draw component to p1
        
        
        p2.add(wordLabel);
        p2.add(attemptsLabel);
        p2.add(guessLabel);
        p2.add(guessChar);

        bg.add(p1);
        bg.add(p2);

        ImageIcon originalIcon = new ImageIcon("reset.png");
            
        // change the ImageIcon to the desired dimensions
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        reset = new JButton(scaledIcon);
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
        wordToGuess = selectRandomWord();
        guessedWord = new char[wordToGuess.length()];
        for(int i = 0; i < wordToGuess.length(); i++)
        {
            if(wordToGuess.charAt(i) == ' ')
            {
                guessedWord[i] = ' ';
            }
            else
            {
                guessedWord[i] = '?';
            }
        }
        attempts = 0;
        wordLabel.setText("Word To Guess : " + String.valueOf(guessedWord));
        attemptsLabel.setText("Attempts left : " + (MAX_TRIAL - attempts));
        p1.repaint();
    }

    private static String selectRandomWord()
    {
        return WORDS[(int)(Math.random() * WORDS.length)];
    }
    public static void main(String[] args) {
        new HangmanGame();
    }
}
