package freecell;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

enum Suit {

    clubs, spades, hearts, diamonds
};

enum Face {

    ace, two, three, four, five, six, seven, eight, nine, ten, jack,
    queen, king
};

class FaceAndSuit {

    Suit suit;
    Face face;

    public FaceAndSuit(Face f, Suit s) {
        suit = s;
        face = f;
    }

    public String toString() {
        return face + "-" + suit;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FaceAndSuit) {
            FaceAndSuit s = (FaceAndSuit) obj;
            if (face != null && suit != null && this.face == s.face && this.suit == s.suit) {
                return true;
            }
        }
        return false;

    }

    @Override
    public int hashCode() {
        int hash = 3;
        if (suit != null && face != null) {
            hash = 7 * hash + this.suit.hashCode();
            hash = 7 * hash + this.face.hashCode();
        }
        return hash;
    }
}

class CardImagePanel extends JPanel {

    BufferedImage img;
    //Point offset;
    //Point click;
    //JPanel clicked;
    FaceAndSuit card;

    public CardImagePanel() {
        img = null;
        this.setBackground(Color.green);
    }

    public CardImagePanel(BufferedImage image, FaceAndSuit card) {
        img = image;
        this.setBackground(new Color(30, 90, 25));
        this.card = card;

    }

    public void setImage(BufferedImage image) {
        img = image;
    }

    public BufferedImage getImage() {
        return img;
    }

    public FaceAndSuit getFaceAndSuit() {
        return card;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            g.drawImage(img, 0, 0, this);
        }
    }

    public Dimension getPreferredSize() {
        Dimension d = new Dimension();
        if (img == null) {
            d.setSize(800, 800);
        } else {
            d.setSize(img.getWidth(), img.getHeight());
        }
        return d;
    }
}

public class FreeCell extends JFrame {

    JFrame frame;
    HashMap<FaceAndSuit, CardImagePanel> map;
    CardImagePanel cip;
    GamePanel gp;
    ArrayList<FaceAndSuit> cards;
    ArrayList<FaceAndSuit> c1, c2, c3, c4, c5, c6, c7, c8, clubs, spades,
            hearts, diamonds, empty;
    FaceAndSuit e1 = null, e2 = null, e3 = null, e4 = null;
    VictoryDialog victory;

    public FreeCell() {

        map = new HashMap<FaceAndSuit, CardImagePanel>();
        loadCardImages();

        frame = new JFrame("Free Cell");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menu = new JMenuBar();
        JMenu game = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        ButtonListener bl = new ButtonListener();   //Start a new game.
        newGame.addActionListener(bl);

        victory = new VictoryDialog(frame, "Victory!");
        victory.setVisible(false);

        menu.add(game);
        game.add(newGame);
        frame.setJMenuBar(menu);

        gp = new GamePanel(this);
        gp.setLayout(null);
        frame.add(gp);

        frame.setVisible(true);

    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            c1 = new ArrayList<FaceAndSuit>();
            c2 = new ArrayList<FaceAndSuit>();
            c3 = new ArrayList<FaceAndSuit>();
            c4 = new ArrayList<FaceAndSuit>();
            c5 = new ArrayList<FaceAndSuit>();
            c6 = new ArrayList<FaceAndSuit>();
            c7 = new ArrayList<FaceAndSuit>();
            c8 = new ArrayList<FaceAndSuit>();
            clubs = new ArrayList<FaceAndSuit>();
            spades = new ArrayList<FaceAndSuit>();
            hearts = new ArrayList<FaceAndSuit>();
            diamonds = new ArrayList<FaceAndSuit>();
            empty = new ArrayList<FaceAndSuit>();   //FreeCell slots.

            empty.add(null);
            empty.add(null);
            empty.add(null);
            empty.add(null);

            cards = getSortedDeck();
            cards = shuffleDeck(cards);

            int j = 0, k = 0;
            //Populate the columns with cards.
            for (k = 0; k < 7; k++) {   
                c1.add(cards.get(j));
                j += 8;
            }
            j = 1;
            for (k = 0; k < 7; k++) {
                c2.add(cards.get(j));
                j += 8;
            }
            j = 2;
            for (k = 0; k < 7; k++) {
                c3.add(cards.get(j));
                j += 8;
            }
            j = 3;
            for (k = 0; k < 7; k++) {
                c4.add(cards.get(j));
                j += 8;
            }
            j = 4;
            for (k = 0; k < 6; k++) {
                c5.add(cards.get(j));
                j += 8;
            }
            j = 5;
            for (k = 0; k < 6; k++) {
                c6.add(cards.get(j));
                j += 8;
            }
            j = 6;
            for (k = 0; k < 6; k++) {
                c7.add(cards.get(j));
                j += 8;
            }
            j = 7;
            for (k = 0; k < 6; k++) {
                c8.add(cards.get(j));
                j += 8;
            }
            displayCards();

        }

        public ArrayList<FaceAndSuit> getSortedDeck() {
            ArrayList<FaceAndSuit> list = new ArrayList<FaceAndSuit>();
            for (Face f : Face.values()) {
                for (Suit s : Suit.values()) {
                    list.add(new FaceAndSuit(f, s));
                }
            }
            return list;
        }

        public ArrayList<FaceAndSuit> shuffleDeck(ArrayList<FaceAndSuit> d) {
            ArrayList<FaceAndSuit> sd = (ArrayList<FaceAndSuit>) d.clone();
            Random random = new Random();
            int shufflecount = 10000;
            int cardcount = sd.size();
            for (int i = 0; i < shufflecount; i++) {
                int card1 = random.nextInt(cardcount);
                int card2 = random.nextInt(cardcount);
                if (card1 != card2) {
                    FaceAndSuit temp = sd.get(card1);
                    sd.set(card1, sd.get(card2));
                    sd.set(card2, temp);
                }
            }
            return sd;
        }

    }

    public void displayCards() {
        final int cardWidth = 290, cardHeight = 200, xOffset = 300, yOffset = 60,
                j = 0, k = 0, xBorder = 50, yBorder = 25;

        for (int i = 0; i < c1.size(); i++) {
            System.out.print(c1.get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < c2.size(); i++) {
            System.out.print(c2.get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < c3.size(); i++) {
            System.out.print(c3.get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < c4.size(); i++) {
            System.out.print(c4.get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < c5.size(); i++) {
            System.out.print(c5.get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < c6.size(); i++) {
            System.out.print(c6.get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < c7.size(); i++) {
            System.out.print(c7.get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < c8.size(); i++) {
            System.out.print(c8.get(i) + " ");
        }
        System.out.println();
        for (int i = 0; i < empty.size(); i++) {
            System.out.print(empty.get(i) + " ");
        }
        System.out.println();
        if (empty.get(0) != null) {
            CardImagePanel cip = map.get(empty.get(0));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder, yBorder, cardHeight, cardWidth);
            gp.add(cip);
        }
        if (empty.get(1) != null) {
            CardImagePanel cip = map.get(empty.get(1));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 225, yBorder, cardHeight, cardWidth);
            gp.add(cip);
        }
        if (empty.get(2) != null) {
            CardImagePanel cip = map.get(empty.get(2));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 225 * 2, yBorder, cardHeight, cardWidth);
            gp.add(cip);
        }
        if (empty.get(3) != null) {
            CardImagePanel cip = map.get(empty.get(3));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 225 * 3, yBorder, cardHeight, cardWidth);
            gp.add(cip);
        }
        for (int i = 0; i < clubs.size(); i++) {
            CardImagePanel cip = map.get(clubs.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 225 * 7, yBorder, cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < spades.size(); i++) {
            CardImagePanel cip = map.get(spades.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 225 * 8, yBorder, cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < hearts.size(); i++) {
            CardImagePanel cip = map.get(hearts.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 225 * 9, yBorder, cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < diamonds.size(); i++) {
            CardImagePanel cip = map.get(diamonds.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 225 * 10, yBorder, cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < c1.size(); i++) {
            CardImagePanel cip = map.get(c1.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 100, 375 + (yOffset * i),
                    cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < c2.size(); i++) {
            CardImagePanel cip = map.get(c2.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 400, 375 + (yOffset * i),
                    cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < c3.size(); i++) {
            CardImagePanel cip = map.get(c3.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 700, 375 + (yOffset * i),
                    cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < c4.size(); i++) {
            CardImagePanel cip = map.get(c4.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 1000, 375 + (yOffset * i),
                    cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < c5.size(); i++) {
            CardImagePanel cip = map.get(c5.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 1300, 375 + (yOffset * i),
                    cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < c6.size(); i++) {
            CardImagePanel cip = map.get(c6.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 1600, 375 + (yOffset * i),
                    cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < c7.size(); i++) {
            CardImagePanel cip = map.get(c7.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 1900, 375 + (yOffset * i),
                    cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }
        for (int i = 0; i < c8.size(); i++) {
            CardImagePanel cip = map.get(c8.get(i));
            cip.setSize(cip.getPreferredSize());
            cip.setBounds(xBorder + 2200, 375 + (yOffset * i),
                    cardHeight, cardWidth);
            gp.add(cip);
            cip.getParent().setComponentZOrder(cip, 0);
        }

        gp.repaint();
        if ((spades.size() + clubs.size() + hearts.size() + diamonds.size())
                == 52) {
            victory.setVisible(true);
        }

    }

    public void loadCardImages() {
        File file = new File("PNG-cards-1.3");
        File[] fArray = file.listFiles();
        BufferedImage img = null, newImage = null;

        FaceAndSuit card;

        for (int i = 0; i < fArray.length; i++) {
            try {
                img = ImageIO.read(fArray[i]);
                newImage = scaleImage(img, .4);

            } catch (IOException err) {
            }
            
            String path = fArray[i].getName();
            card = new FaceAndSuit(getFace(path), getSuit(path));
            cip = new CardImagePanel(newImage, card);

            cip.setPreferredSize(cip.getPreferredSize());

            map.put(card, cip);
        }
        cip = new CardImagePanel();
    }

    private static BufferedImage scaleImage(BufferedImage img, double scale) {
        int scaledw = (int) (img.getWidth() * scale);
        int scaledh = (int) (img.getHeight() * scale);
        Image scaledimg = img.getScaledInstance(scaledw, scaledh,
                Image.SCALE_SMOOTH);
        BufferedImage scaledbufferedimg = new BufferedImage(scaledw, scaledh,
                img.getType());
        scaledbufferedimg.getGraphics().drawImage(scaledimg, 0, 0, null);
        return scaledbufferedimg;
    }

    public Face getFace(String face) {

        if (face.contains("king")) {
            return Face.king;
        } else if (face.contains("queen")) {
            return Face.queen;
        } else if (face.contains("jack")) {
            return Face.jack;
        } else if (face.contains("10") || face.contains("ten")) {
            return Face.ten;
        } else if (face.contains("9") || face.contains("nine")) {
            return Face.nine;
        } else if (face.contains("8") || face.contains("eight")) {
            return Face.eight;
        } else if (face.contains("7") || face.contains("seven")) {
            return Face.seven;
        } else if (face.contains("6") || face.contains("six")) {
            return Face.six;
        } else if (face.contains("5") || face.contains("five")) {
            return Face.five;
        } else if (face.contains("4") || face.contains("four")) {
            return Face.four;
        } else if (face.contains("3") || face.contains("three")) {
            return Face.three;
        } else if (face.contains("2") || face.contains("two")) {
            return Face.two;
        } else {
            return Face.ace;
        }
    }

    public Suit getSuit(String suit) {

        if (suit.contains("spades")) {
            return Suit.spades;
        } else if (suit.contains("clubs")) {
            return Suit.clubs;
        } else if (suit.contains("hearts")) {
            return Suit.hearts;
        } else if (suit.contains("diamonds")) {
            return Suit.diamonds;
        } else {
            return null;//it was a joker, which does not have a suit
        }
    }

    public HashMap<FaceAndSuit, CardImagePanel> getMap() {
        return map;
    }

    public static void main(String[] args) {
        FreeCell fc = new FreeCell();
    }
}

class VictoryDialog extends JDialog {

    JLabel label;
    JButton ok;

    public VictoryDialog(JFrame parent, String title) {
        super(parent, title);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        setLocation(400, 400);
        setSize(500, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 1, 25, 25));
        label = new JLabel("Victory!");
        panel.add(label);

        ok = new JButton("ok");
        
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                setVisible(false);
            }
        });
        panel.add(ok);
        add(panel);
    }
}

class GamePanel extends JPanel implements MouseListener, MouseMotionListener {

    int xBorder = 50, yBorder = 25, index, cardsDragged = 0;
    Color backGroundColor = new Color(30, 90, 25);
    FreeCell fc = null;
    double setW = this.getWidth(), setH = this.getHeight();
    double dragX = 0, dragY = 0;
    Point click, offset;
    JPanel clicked;
    Component c;
    ArrayList<FaceAndSuit> clickedColumn;
    Boolean flag = false;   //Used to check if a card in the FreeCell is clicked.
    FaceAndSuit cardFromTop = null;
    ArrayList<CardImagePanel> tableau = new ArrayList<CardImagePanel>();

    public GamePanel(FreeCell fc) {
        this.fc = fc;
        setBackground(backGroundColor);
        addMouseListener(this);
        addMouseMotionListener(this);

    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        Font font = new Font("Courier", Font.BOLD, 25);
        g.setFont(font);
        
        //Set borders for the FreeCells.
        g.drawRoundRect(xBorder, yBorder, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 225, yBorder, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 225 * 2, yBorder, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 225 * 3, yBorder, 200, 290, 10, 10);

        //Set borders for the foundations.
        g.drawRoundRect(xBorder + 225 * 7, yBorder, 200, 290, 10, 10);
        g.drawString("Clubs", xBorder + (225 * 7) + 60, yBorder + 150);
        g.drawRoundRect(xBorder + 225 * 8, yBorder, 200, 290, 10, 10);
        g.drawString("Spades", xBorder + (225 * 8) + 60, yBorder + 150);
        g.drawRoundRect(xBorder + 225 * 9, yBorder, 200, 290, 10, 10);
        g.drawString("Hearts", xBorder + (225 * 9) + 60, yBorder + 150);
        g.drawRoundRect(xBorder + 225 * 10, yBorder, 200, 290, 10, 10);
        g.drawString("Diamonds", xBorder + (225 * 10) + 45, yBorder + 150);

        //Set Borders for the columns.
        g.drawRoundRect(xBorder + 100, yBorder + 350, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 400, yBorder + 350, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 700, yBorder + 350, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 1000, yBorder + 350, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 1300, yBorder + 350, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 1600, yBorder + 350, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 1900, yBorder + 350, 200, 290, 10, 10);
        g.drawRoundRect(xBorder + 2200, yBorder + 350, 200, 290, 10, 10);
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {
        int meX = me.getX();
        int meY = me.getY();
        click = me.getPoint();
        c = getComponentAt(click);
        if (c != this) {
            if (clicked != null) {
                if (clicked != null) {
                    int x = me.getPoint().x - offset.x;
                    int y = me.getPoint().y - offset.y;
                    clicked.setLocation(x, y);
                }
                offset = null;
                clicked = null;
            } else {
                clicked = this;
                c.getParent().setComponentZOrder(c, 0);
                System.out.println(c instanceof JPanel);

                if (c instanceof JPanel) {
                    if (meX > 150 && meX < 350 && meY > 375 && meY < 1300   //Pressed in column 1.
                            && fc.c1 != null && fc.c1.get(0) != null) {
                        if (canMove(fc.c1)) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            clickedColumn = fc.c1;
                        }
                    } else if (meX > 450 && meX < 650 && meY > 375 && meY < 1300    //Pressed in column 2.
                            && fc.c2 != null && fc.c2.get(0) != null) {
                        if (canMove(fc.c2)) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            clickedColumn = fc.c2;
                        }
                    } else if (meX > 750 && meX < 950 && meY > 375 && meY < 1300    //Pressed in column 3.
                            && fc.c3 != null && fc.c3.get(0) != null) {
                        if (canMove(fc.c3)) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            clickedColumn = fc.c3;
                        }
                    } else if (meX > 1050 && meX < 1250 && meY > 375 && meY < 1300  //Pressed in column 4.
                            && fc.c4 != null && fc.c4.get(0) != null) {
                        if (canMove(fc.c4)) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            clickedColumn = fc.c4;
                        }
                    } else if (meX > 1350 && meX < 1550 && meY > 375 && meY < 1300  //Pressed in column 5.
                            && fc.c5 != null && fc.c5.get(0) != null) {
                        if (canMove(fc.c5)) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            clickedColumn = fc.c5;
                        }
                    } else if (meX > 1650 && meX < 1850 && meY > 375 && meY < 1300  //Pressed in column 6.
                            && fc.c6 != null && fc.c6.get(0) != null) {
                        if (canMove(fc.c6)) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            clickedColumn = fc.c6;
                        }
                    } else if (meX > 1950 && meX < 2150 && meY > 375 && meY < 1300  //Pressed in column 7.
                            && fc.c7 != null && fc.c7.get(0) != null) {
                        if (canMove(fc.c7)) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            clickedColumn = fc.c7;
                        }
                    } else if (meX > 2250 && meX < 2450 && meY > 375 && meY < 1300  //Pressed in column 8.
                            && fc.c8 != null && fc.c8.get(0) != null) {
                        if (canMove(fc.c8)) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            clickedColumn = fc.c8;
                        }
                    } else if (meX > xBorder && meX < xBorder + 200 //Pressed in FreeCell 1.
                            && meY > yBorder && meY < yBorder + 290) {
                        if (fc.empty.get(0) != null) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            flag = true;
                            index = 0;
                            cardsDragged = 1;
                            cardFromTop = fc.empty.get(0);
                        }
                    } else if (meX > xBorder + 225 && meX < xBorder + 425   //Pressed in FreeCell 2.
                            && meY > yBorder && meY < yBorder + 290) {
                        if (fc.empty.get(1) != null) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            flag = true;
                            index = 1;
                            cardsDragged = 1;
                            cardFromTop = fc.empty.get(1);
                        }
                    } else if (meX > xBorder + (225 * 2) && meX < xBorder + 650 //Pressed in FreeCell 3.
                            && meY > yBorder && meY < yBorder + 290) {
                        if (fc.empty.get(2) != null) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            flag = true;
                            index = 2;
                            cardsDragged = 1;
                            cardFromTop = fc.empty.get(2);
                        }
                    } else if (meX > xBorder + (225 * 3) && meX < xBorder + 875 //Pressed in FreeCell 4.
                            && meY > yBorder && meY < yBorder + 290) {
                        System.out.println("a");
                        if (fc.empty.get(3) != null) {
                            clicked = (JPanel) c;
                            offset = c.getLocation();
                            flag = true;
                            index = 3;
                            cardsDragged = 1;
                            cardFromTop = fc.empty.get(3);
                        }
                    }
                }
            }
        }
    }

    public boolean canMove(ArrayList<FaceAndSuit> column) {
        CardImagePanel cip = (CardImagePanel) c;
        FaceAndSuit current = cip.getFaceAndSuit();
        for (int i = column.size() - 1; i >= 0; i--) {
            cardsDragged++;
            if (current.equals(column.get(i))) {
                tableau.add(cip);
                if (i == column.size() - 1) {   //A single card was pressed.
                    index = i;
                    return true;
                } else {    //Multiple cards are pressed.
                    for (int j = i; j < column.size() - 1; j++) {
                        //System.out.println("a");
                        String suit1 = getSuit(current);
                        String suit2 = getSuit(column.get(j + 1));
                        if (current.face.ordinal() != column.get(j + 1).face.ordinal()  //Checks for a non-tableau.
                                + 1 || suit1.equals(suit2)) {
                            tableau.clear();
                            return false;
                        } else {    //Tableau condition.
                            CardImagePanel c = fc.map.get(column.get(j + 1));
                            System.out.println(cardsDragged);
                            tableau.add(c);
                        }
                        current = column.get(j + 1);
                        cardsDragged++;
                    }
                    index = i;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        int meX = me.getX();
        int meY = me.getY();
        if (!me.getPoint().equals(click)) {
            System.out.println("released");
            if (meX > 150 && meX < 350 && meY > 375 && meY < 1300) {    //Released in Column 1.
                if (fc.c1.isEmpty()) {  //Column is empty.
                    if (flag) { //The card dragged is from a FreeCell.
                        fc.c1.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        for (int i = index; i < clickedColumn.size(); i++) {
                            fc.c1.add(clickedColumn.get(i));
                        }
                        for (int i = clickedColumn.size() - 1; i >= index; i--) {
                            clickedColumn.remove(i);
                        }
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();

                    }
                } else {    //Column is populated.
                    if (canRelease(fc.c1)) {
                        if (flag) { //Card dragged was from FreeCell.
                            fc.c1.add(fc.empty.get(index));
                            fc.empty.set(index, null);
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        } else {
                            for (int i = index; i < clickedColumn.size(); i++) {
                                fc.c1.add(clickedColumn.get(i));
                            }
                            for (int i = clickedColumn.size() - 1; i
                                    >= index; i--) {
                                clickedColumn.remove(i);
                            }
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        }
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                }
            } else if (meX > 450 && meX < 650 && meY > 375 && meY < 1300) { //Released in Column 2.
                if (fc.c2.isEmpty()) {  //Column 2 is empty.
                    if (flag) { //Card dragged was from FreeCell.
                        fc.c2.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        for (int i = index; i < clickedColumn.size(); i++) {
                            fc.c2.add(clickedColumn.get(i));
                        }
                        for (int i = clickedColumn.size() - 1; i
                                >= index; i--) {
                            clickedColumn.remove(i);
                        }
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else {    //Column 2 is populated.
                    if (canRelease(fc.c2)) {
                        if (flag) { //Card dragged was from FreeCell.
                            fc.c2.add(fc.empty.get(index));
                            fc.empty.set(index, null);
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        } else {
                            for (int i = index; i < clickedColumn.size(); i++) {
                                fc.c2.add(clickedColumn.get(i));
                            }
                            for (int i = clickedColumn.size() - 1; i
                                    >= index; i--) {
                                clickedColumn.remove(i);
                            }
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        }
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                }
            } else if (meX > 750 && meX < 950 && meY > 375 && meY < 1300) { //Released in Column 3.
                if (fc.c3.isEmpty()) {  //Column 3 is empty.
                    if (flag) { //Card dragged from FreeCell.
                        fc.c3.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        for (int i = index; i < clickedColumn.size(); i++) {
                            fc.c3.add(clickedColumn.get(i));
                        }
                        for (int i = clickedColumn.size() - 1; i
                                >= index; i--) {
                            clickedColumn.remove(i);
                        }
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else {    //Column 3 is populated.
                    if (canRelease(fc.c3)) {
                        if (flag) {
                            fc.c3.add(fc.empty.get(index));
                            fc.empty.set(index, null);
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        } else {
                            for (int i = index; i < clickedColumn.size(); i++) {
                                fc.c3.add(clickedColumn.get(i));
                            }
                            for (int i = clickedColumn.size() - 1; i
                                    >= index; i--) {
                                clickedColumn.remove(i);
                            }
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        }
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                }
            } else if (meX > 1050 && meX < 1250 && meY > 375 && meY < 1300) {   //Card released in column 4.
                if (fc.c4.isEmpty()) {  //Column 4 is empty.
                    if (flag) { //Card dragged is from FreeCell.
                        fc.c4.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        for (int i = index; i < clickedColumn.size(); i++) {
                            fc.c4.add(clickedColumn.get(i));
                        }
                        for (int i = clickedColumn.size() - 1; i
                                >= index; i--) {
                            clickedColumn.remove(i);
                        }
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else {    //Column 4 is populated.
                    if (canRelease(fc.c4)) {
                        if (flag) { //Card dragged from FreeCell.
                            fc.c4.add(fc.empty.get(index));
                            fc.empty.set(index, null);
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        } else {
                            for (int i = index; i < clickedColumn.size(); i++) {
                                fc.c4.add(clickedColumn.get(i));
                            }
                            for (int i = clickedColumn.size() - 1; i
                                    >= index; i--) {
                                clickedColumn.remove(i);
                            }
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        }
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                }
            } else if (meX > 1350 && meX < 1550 && meY > 375 && meY < 1300) {   //Released in column 5.
                if (fc.c5.isEmpty()) {  //Column 5 is empty.
                    if (flag) { //Card dragged from FreeCell.
                        fc.c5.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        for (int i = index; i < clickedColumn.size(); i++) {
                            fc.c5.add(clickedColumn.get(i));
                        }
                        for (int i = clickedColumn.size() - 1; i
                                >= index; i--) {
                            clickedColumn.remove(i);
                        }
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else {    //Column 5 is populated.
                    if (canRelease(fc.c5)) {
                        if (flag) { //Card dragged from FreeCell.
                            fc.c5.add(fc.empty.get(index));
                            fc.empty.set(index, null);
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        } else {
                            for (int i = index; i < clickedColumn.size(); i++) {
                                fc.c5.add(clickedColumn.get(i));
                            }
                            for (int i = clickedColumn.size() - 1; i
                                    >= index; i--) {
                                clickedColumn.remove(i);
                            }
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        }
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                }
            } else if (meX > 1650 && meX < 1850 && meY > 375 && meY < 1300) {   //Released in Column 6.
                if (fc.c6.isEmpty()) {  //Column 6 is empty.
                    if (flag) { //Card dragged from FreeCell.
                        fc.c6.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        for (int i = index; i < clickedColumn.size(); i++) {
                            fc.c6.add(clickedColumn.get(i));
                        }
                        for (int i = clickedColumn.size() - 1; i
                                >= index; i--) {
                            clickedColumn.remove(i);
                        }
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else {    //Column 6 is populated.
                    if (canRelease(fc.c6)) {
                        if (flag) { //Card dragged from FreeCell.
                            fc.c6.add(fc.empty.get(index));
                            fc.empty.set(index, null);
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        } else {
                            for (int i = index; i < clickedColumn.size(); i++) {
                                fc.c6.add(clickedColumn.get(i));
                            }
                            for (int i = clickedColumn.size() - 1; i
                                    >= index; i--) {
                                clickedColumn.remove(i);
                            }
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        }
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                }
            } else if (meX > 1950 && meX < 2150 && meY > 375 && meY < 1300) {   //Released in Column 7.
                if (fc.c7.isEmpty()) {  //Column 7 is empty.
                    if (flag) { //Card dragged from FreeCell.
                        fc.c7.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        for (int i = index; i < clickedColumn.size(); i++) {
                            fc.c7.add(clickedColumn.get(i));
                        }
                        for (int i = clickedColumn.size() - 1; i
                                >= index; i--) {
                            clickedColumn.remove(i);
                        }
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else {    //Column 7 is populated.
                    if (canRelease(fc.c7)) {
                        if (flag) { //Card dragged from FreeCell.
                            fc.c7.add(fc.empty.get(index));
                            fc.empty.set(index, null);
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        } else {
                            for (int i = index; i < clickedColumn.size(); i++) {
                                fc.c7.add(clickedColumn.get(i));
                            }
                            for (int i = clickedColumn.size() - 1; i
                                    >= index; i--) {
                                clickedColumn.remove(i);
                            }
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        }
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                }
            } else if (meX > 2250 && meX < 2450 && meY > 375 && meY < 1300) {   //Released in Column 8.
                if (fc.c8.isEmpty()) {  //Column 8 is empty.
                    if (flag) { //Card dragged from FreeCell.
                        fc.c8.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        for (int i = index; i < clickedColumn.size(); i++) {
                            fc.c8.add(clickedColumn.get(i));
                        }
                        for (int i = clickedColumn.size() - 1; i
                                >= index; i--) {
                            clickedColumn.remove(i);
                        }
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else {    //Column 8 is populated.
                    if (canRelease(fc.c8)) {    
                        if (flag) { //Card dragged from FreeCell.
                            fc.c1.add(fc.empty.get(index));
                            fc.empty.set(index, null);
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        } else {
                            for (int i = index; i < clickedColumn.size(); i++) {
                                fc.c8.add(clickedColumn.get(i));
                            }
                            for (int i = clickedColumn.size() - 1; i
                                    >= index; i--) {
                                clickedColumn.remove(i);
                            }
                            cardsDragged = 0;
                            flag = false;
                            tableau.clear();
                            fc.displayCards();
                        }
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                }
            } else if (meX > xBorder && meX < xBorder + 200
                    && meY > yBorder && meY < yBorder + 290) {  //Card released in FreeCell.
                if (flag) { //Do nothing if the card dragged was also from a FreeCell.

                } else if (fc.empty.get(0) == null && cardsDragged == 1) {  //Card release in FreeCell 1.
                    fc.empty.set(0, clickedColumn.get(clickedColumn.size() - 1));
                    clickedColumn.remove(clickedColumn.size() - 1);
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                } else {    //FreeCell 1 isn't empty.
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                }
            } else if (meX > xBorder + 225 && meX < xBorder + 425   //Release in FreeCell 2.
                    && meY > yBorder && meY < yBorder + 290) {
                if (flag) {

                } else if (fc.empty.get(1) == null && cardsDragged == 1) {  //FreeCell 2 is empty.
                    fc.empty.set(1, clickedColumn.get(clickedColumn.size() - 1));
                    clickedColumn.remove(clickedColumn.size() - 1);
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                } else {    //FreeCell 2 isn't empty.
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                }
            } else if (meX > xBorder + (225 * 2) && meX < xBorder + 650 //Release in FreeCell 3.
                    && meY > yBorder && meY < yBorder + 290) {
                if (flag) {

                } else if (fc.empty.get(2) == null && cardsDragged == 1) {  //FreeCell 3 is empty.
                    fc.empty.set(2, clickedColumn.get(clickedColumn.size() - 1));
                    clickedColumn.remove(clickedColumn.size() - 1);
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                } else {    //FreeCell 3 is populated. 
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                }
            } else if (meX > xBorder + (225 * 3) && meX < xBorder + 875 //Released in FreeCell 4.
                    && meY > yBorder && meY < yBorder + 290) {
                if (flag) {

                } else if (fc.empty.get(3) == null && cardsDragged == 1) {  //FreeCell is empty.
                    fc.empty.set(3, clickedColumn.get(clickedColumn.size() - 1));
                    System.out.println(fc.e4);
                    clickedColumn.remove(clickedColumn.size() - 1);
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                } else {    //FreeCell is populated.
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                }
            } else if (meX > xBorder + (225 * 7) && meX < xBorder + 1775    //Release in clubs foundation.
                    && meY > yBorder && meY < yBorder + 290) {
                if (flag) { //Card dragged from FreeCell.
                    System.out.println(cardsDragged);
                    if (cardsDragged == 1 && releaseInFoundation(fc.empty.get(index),
                            fc.clubs, 0)) {
                        System.out.println(fc.empty.get(index));
                        fc.clubs.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {    //Illegal move.
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else if (cardsDragged == 1 && releaseInFoundation(clickedColumn.  //Card was from one of the columns.
                        get(clickedColumn.size() - 1), fc.clubs, 0)) {
                    fc.clubs.add(clickedColumn.get(clickedColumn.size() - 1));
                    clickedColumn.remove(clickedColumn.size() - 1);
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                } else {    //Illegal move.
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                }
            } else if (meX > xBorder + (225 * 8) && meX < xBorder + 2000    //Released in spades column.
                    && meY > yBorder && meY < yBorder + 290) {
                if (flag) { //Card dragged from FreeCell.
                    System.out.println(cardsDragged);
                    if (cardsDragged == 1 && releaseInFoundation(fc.empty.get(index),
                            fc.spades, 1)) {
                        System.out.println(fc.empty.get(index));
                        fc.spades.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else if (cardsDragged == 1 && releaseInFoundation(clickedColumn.
                        get(clickedColumn.size() - 1), fc.spades, 1)) {
                    fc.spades.add(clickedColumn.get(clickedColumn.size() - 1));
                    clickedColumn.remove(clickedColumn.size() - 1);
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                } else {    //Illegal move.
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                }
            } else if (meX > xBorder + (225 * 9) && meX < xBorder + 2225    //Released in hearts foundation.
                    && meY > yBorder && meY < yBorder + 290) {
                if (flag) { //Card dragged from FreeCell.
                    System.out.println(cardsDragged);
                    if (cardsDragged == 1 && releaseInFoundation(fc.empty.get(index),
                            fc.hearts, 2)) {
                        System.out.println(fc.empty.get(index));
                        fc.hearts.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else if (cardsDragged == 1 && releaseInFoundation(clickedColumn.
                        get(clickedColumn.size() - 1), fc.hearts, 2)) {
                    fc.hearts.add(clickedColumn.get(clickedColumn.size() - 1));
                    clickedColumn.remove(clickedColumn.size() - 1);
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                } else {    //Illegal move.
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                }

            } else if (meX > xBorder + (225 * 10) && meX < xBorder + 2450   //Released in diamonds foundation.
                    && meY > yBorder && meY < yBorder + 290) {
                if (flag) { //Card dragged from FreeCells.
                    System.out.println(cardsDragged);
                    if (cardsDragged == 1 && releaseInFoundation(fc.empty.get(index),
                            fc.diamonds, 3)) {
                        System.out.println(fc.empty.get(index));
                        fc.diamonds.add(fc.empty.get(index));
                        fc.empty.set(index, null);
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    } else {
                        cardsDragged = 0;
                        flag = false;
                        tableau.clear();
                        fc.displayCards();
                    }
                } else if (cardsDragged == 1 && releaseInFoundation(clickedColumn.
                        get(clickedColumn.size() - 1), fc.diamonds, 3)) {
                    fc.diamonds.add(clickedColumn.get(clickedColumn.size() - 1));
                    clickedColumn.remove(clickedColumn.size() - 1);
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                } else {    //Illegal move.
                    cardsDragged = 0;
                    flag = false;
                    tableau.clear();
                    fc.displayCards();
                }
            }
            cardsDragged = 0;
            flag = false;
            clicked = null;
            tableau.clear();
            fc.displayCards();
        }
        clicked = null;
        click = null;
    }

    public boolean canRelease(ArrayList<FaceAndSuit> column) {
        String suit1 = getSuit(column.get(column.size() - 1));
        if (!flag) {    //Card dragged from one of the columns.
            if (cardsDragged == 1) {    //Single card.
                String suit2 = getSuit(clickedColumn.get(index));
                if (column.get(column.size() - 1).face.ordinal()
                        == clickedColumn.get(index).face.ordinal() + 1
                        && suit1.compareTo(suit2) != 0) {
                    return true;
                }
            } else {
                if (!tableau.isEmpty()) {   //Tableau is released.
                    String suit2 = getSuit(tableau.get(0).getFaceAndSuit());
                    if (column.get(column.size() - 1).face.ordinal()
                            == tableau.get(0).getFaceAndSuit().face.ordinal() + 1
                            && suit1.compareTo(suit2) != 0) {
                        return true;
                    }
                }
            }
            return false;
        } else {    //Card dragged from FreeCells.
            String suit2 = getSuit(cardFromTop);
            if (column.get(column.size() - 1).face.ordinal()
                    == cardFromTop.face.ordinal() + 1
                    && suit1.compareTo(suit2) != 0) {
                return true;
            }
            return false;
        }
    }

    public boolean releaseInFoundation(FaceAndSuit column, ArrayList<FaceAndSuit> foundation, int suit) {  
        Suit s;
        if (suit == 0) {
            s = Suit.clubs;
        } else if (suit == 1) {
            s = Suit.spades;
        } else if (suit == 2) {
            s = Suit.hearts;
        } else {
            s = Suit.diamonds;
        }
        if (foundation.isEmpty()) { 
            if (column.face == Face.ace && column.suit == s) {
                return true;
            }
            System.out.println(false);
            return false;
        } else {
            if (column.face.ordinal() == foundation.get(foundation.size() - 1).face.ordinal() + 1 && column.suit == s) {
                return true;
            }
            return false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {

    }

    @Override
    public void mouseExited(MouseEvent me) {

    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (clicked != null && offset != null) {
            int x = click.x - offset.x;
            int y = click.y - offset.y;
            if (cardsDragged == 1) {
                int a = me.getX() - x;
                int b = me.getY() - y;
                clicked.setLocation(a, b);
            } else {    //Drag a tableau.
                for (int i = 0; i < tableau.size(); i++) {
                    clicked = (JPanel) tableau.get(i);
                    int a = me.getX() - x;
                    int b = (me.getY() - y) + (i * 60);
                    clicked.setLocation(a, b);
                    clicked.getParent().setComponentZOrder(clicked, 0);
                    revalidate();
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent me) {

    }

    public String getSuit(FaceAndSuit card) {
        if (card.suit.equals(Suit.diamonds) || (card.suit.equals(Suit.hearts))) {
            return "red";
        } else {
            return "black";
        }
    }

}
