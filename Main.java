package exc12;

import com.sun.glass.events.KeyEvent;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import static java.awt.event.ActionEvent.CTRL_MASK;
import static java.awt.event.ActionEvent.SHIFT_MASK;

// Zadanie 17 - prosty edytor tekstowy
public class Main extends JFrame implements DocumentListener {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()-> new Main());
    }

    JTextArea textArea;
    String path;
    ColorIcon lastForegroundIcon, lastBackgroundIcon;
    JLabel fontSizeLabel, fileState;
    boolean textAreaChanged;

    public Main() {
        this.path = null;
        this.textAreaChanged = false;

        this.setTitle("Prosty edytor - bez tytułu");
        this.setLayout(new BorderLayout());

        // 1 Menu bar
        JMenuBar jMenuBar = generateMenu();
        this.add(jMenuBar, BorderLayout.PAGE_START);

        // 2 Text area
        textArea = new JTextArea(); // pole tekstowe
        textArea.getDocument().addDocumentListener(this);
        JScrollPane jScrollPane = new JScrollPane(textArea);
        this.add(jScrollPane, BorderLayout.CENTER);

        // 3 Status bar
        JPanel statusBar = generateStatusBar();
        this.add(statusBar, BorderLayout.PAGE_END);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(480,360));
        this.pack();
        this.setVisible(true);
    }

    // Menu
    JMenuBar generateMenu() {
        JMenuBar jMenuBar = new JMenuBar();

        jMenuBar.add(generateFileMenu("File")); //File
        jMenuBar.add(generateEditMenu("Edit")); // Edit
        jMenuBar.add(generateOptionsMenu("Options")); //Option

        return jMenuBar;
    }

    // Menu -> File
    JMenu generateFileMenu(String name) {
        JMenu fileMenu = new JMenu(name);

        String[] items = {"Open", "Save", "Save As", "Exit"};
        for(String item : items) {

            if(item.compareTo("Exit") == 0)
                fileMenu.addSeparator();

            fileMenu.add(generateFileMenuItem(item));
        }

        return fileMenu;
    }

    JMenuItem generateFileMenuItem(String itemName) {
        JMenuItem jMenuItem = new JMenuItem(itemName);
        JFileChooser jFileChooser = new JFileChooser();

        switch (itemName) {
            case "Open" -> {
                jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, CTRL_MASK));
                jMenuItem.setMnemonic('O');
                jMenuItem = appendOpenEvent(jMenuItem);
            }
            case "Save" -> {
                jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_MASK));
                jMenuItem.setMnemonic('S');
                jMenuItem = appendSaveEvent(jMenuItem);
            }
            case "Save As" -> {
                jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, CTRL_MASK));
                jMenuItem.setMnemonic('a');
                jMenuItem = appendSaveAsEvent(jMenuItem);
            }
            case "Exit" -> {
                jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, CTRL_MASK));
                jMenuItem.setMnemonic('x');
                jMenuItem = appendExitEvent(jMenuItem);
            }
        }

        return jMenuItem;
    }

    JMenuItem appendOpenEvent(JMenuItem jMenuItem) {
        jMenuItem.addActionListener(
                (e) -> {
                    JFileChooser jFileChooser = new JFileChooser();
                    if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File file = jFileChooser.getSelectedFile();
                        this.path = file.getPath();
                        this.setTitle("Prosty edytor - " + this.path);
                        this.textArea.setText("");

                        try {
                            Scanner scanner = new Scanner(file);
                            while(scanner.hasNext()) {
                                this.textArea.append(scanner.nextLine()+'\n');
                            }
                            this.textAreaChanged = false;
                            refreshState();
                        } catch (FileNotFoundException fileNotFoundException) {
                            JOptionPane.showMessageDialog(null, "Nie udało się wczytać pliku:\n " + file);
                            fileNotFoundException.printStackTrace();
                        }
                    }
                }
        );
        return jMenuItem;
    }

    JMenuItem appendSaveEvent(JMenuItem jMenuItem) {
        jMenuItem.addActionListener(
                (e) -> {
                    // brak ścieżki zapisu
                    if(this.path == null) {
                        JFileChooser jFileChooser = new JFileChooser();
                        if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            File file = jFileChooser.getSelectedFile();
                            this.path = file.getPath();
                            this.setTitle("Prosty edytor - " + this.path);

                            try {
                                PrintWriter printWriter = new PrintWriter(this.path);
                                Scanner scanner = new Scanner(this.textArea.getText());

                                while(scanner.hasNext()) {
                                    printWriter.println(scanner.nextLine());
                                }
                                printWriter.close();
                                this.textAreaChanged = false;
                                refreshState();
                            } catch (FileNotFoundException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }
                    }
                    // zapis do aktualnej ścieżki
                    try {
                        PrintWriter printWriter = new PrintWriter(this.path);
                        Scanner scanner = new Scanner(this.textArea.getText());

                        while(scanner.hasNext()) {
                            printWriter.println(scanner.nextLine());
                        }
                        printWriter.close();
                        this.textAreaChanged = false;
                        refreshState();
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
        );
        return jMenuItem;
    }

    JMenuItem appendSaveAsEvent(JMenuItem jMenuItem) {
        jMenuItem.addActionListener(
                (e) -> {
                    JFileChooser jFileChooser = new JFileChooser();
                    if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File file = jFileChooser.getSelectedFile();
                        this.path = file.getPath();
                        this.setTitle("Prosty edytor - " + this.path);

                        try {
                            PrintWriter printWriter = new PrintWriter(this.path);
                            Scanner scanner = new Scanner(this.textArea.getText());

                            while(scanner.hasNext()) {
                                printWriter.println(scanner.nextLine());
                            }
                            printWriter.close();
                            this.textAreaChanged = false;
                            refreshState();
                        } catch (FileNotFoundException fileNotFoundException) {
                            fileNotFoundException.printStackTrace();
                        }
                    }
                }
        );
        return jMenuItem;
    }

    JMenuItem appendExitEvent(JMenuItem jMenuItem) {
        jMenuItem.addActionListener(
                (e) -> {
                    System.exit(0);
                }
        );
        return jMenuItem;
    }

    // Menu -> Edit
    JMenu generateEditMenu(String name) {
        JMenu editMenu = new JMenu(name);

        editMenu.add(generateAddress("Praca", "Racławicka 27, Toruń 04-154", 'P', KeyEvent.VK_P));
        editMenu.add(generateAddress("Dom","Biała 44, Poznań 09-741", 'D', KeyEvent.VK_D));
        editMenu.add(generateAddress("Szkoła","Wesoła 27, Kielce 12-154", 'S', KeyEvent.VK_P));

        return editMenu;
    }

    JMenuItem generateAddress(String title, String address, char mnemonic, int keyEvent) {
        JMenuItem jMenuItem = new JMenuItem(title);

        jMenuItem.setMnemonic(mnemonic);
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, CTRL_MASK+InputEvent.SHIFT_MASK));
        jMenuItem = appendInsertEvent(jMenuItem, address);

        return jMenuItem;
    }

    JMenuItem appendInsertEvent(JMenuItem jMenuItem, String text) {
        // dodawanie adresu w miejsce kursora
        jMenuItem.addActionListener(
                (e) -> {
                    this.textArea.insert(text, textArea.getCaretPosition());
                }
        );

        return jMenuItem;
    }

    // Menu -> Option
    JMenu generateOptionsMenu(String name) {
        JMenu optionMenu = new JMenu(name);

        optionMenu.add(generateColorChanger("Foreground"));
        optionMenu.add(generateColorChanger("Background"));
        optionMenu.add(generateOptionFontSize("Font size"));

        return optionMenu;
    }

    JMenu generateColorChanger(String source) {
        JMenu jMenu = new JMenu(source);

        String[] colorNames = {"Green", "Orange", "Red", "Black", "White", "Yellow", "Blue"};
        Color[] colors = {Color.GREEN, Color.ORANGE, Color.RED, Color.BLACK, Color.WHITE, Color.YELLOW, Color.BLUE};

        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem item;
        ColorIcon icon;

        // generowanie listy opcji dla foreground i background
        for(int i=0; i<colors.length; i++) {
            icon = new ColorIcon(colors[i]);
            item = new JRadioButtonMenuItem();
            item.setIcon(icon);
            item.setText("  " + colorNames[i]);
            item = appendColorChangeEvent(item, colors[i], source);
            group.add(item);
            jMenu.add(item);
        }

        return jMenu;
    }

    JRadioButtonMenuItem appendColorChangeEvent(JRadioButtonMenuItem item, Color color, String parentName) {

        item.addActionListener(
                (e) -> {
                    if(parentName.compareTo("Foreground") == 0) {
                        this.lastForegroundIcon.setColor(this.textArea.getForeground());
                        this.textArea.setForeground(color);
                        this.repaint();
                    } else {
                        this.lastBackgroundIcon.setColor(this.textArea.getBackground());
                        this.textArea.setBackground(color);
                        this.repaint();
                    }
                }
        );

        return item;
    }

    JMenu generateOptionFontSize(String name) {
        JMenu fontMenu = new JMenu(name);

        int[] fontSize = {8, 10, 12, 14, 16, 18, 20, 22, 24};
        for(int size : fontSize) {
            fontMenu.add(generateFontSizeItem(size));
        }

        return fontMenu;
    }

    JMenuItem generateFontSizeItem(int size) {
        JMenuItem jMenuItem = new JMenuItem(String.valueOf(size) + " pts");
        jMenuItem.setFont((new Font("Consolas", Font.PLAIN, size)));
        jMenuItem = appendFontSizeChange(jMenuItem, size);
        return jMenuItem;
    }

    JMenuItem appendFontSizeChange(JMenuItem jMenuItem, int size) {
        jMenuItem.addActionListener(
                (e) -> {
                    this.textArea.setFont(new Font("Calibri", Font.PLAIN, size));
                    this.fontSizeLabel.setText(String.valueOf(size) + " (size)");
                    this.repaint();
                }
        );
        return jMenuItem;
    }

    // Metoda aktualizująca stan aktualnego pliku po akcji otworzenia, modyfkiacji, zapisu...
    void refreshState() {
        if(this.path == null) {
            this.fileState.setText("new (state)");
        } else if(this.textAreaChanged) {
            this.fileState.setText("modified (state)");
        } else {
            this.fileState.setText("saved (state)");
        }
        this.repaint();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        this.textAreaChanged = true;
        refreshState();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        this.textAreaChanged = true;
        refreshState();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        this.textAreaChanged = true;
        refreshState();
    }

    JPanel generateStatusBar() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());

        // ostatni kolor czcionki
        JLabel lForeground;
        lForeground = new JLabel(" (foreground) ");
        this.lastForegroundIcon = new ColorIcon(Color.white, 1, 2);
        lForeground.setIcon(this.lastForegroundIcon);

        // ostatni kolor tła
        JLabel lBackground;
        lBackground = new JLabel(" (background) ");
        this.lastBackgroundIcon = new ColorIcon(Color.white, 1, 2);
        lBackground.setIcon(this.lastBackgroundIcon);

        // aktualny rozmiar czcionki
        this.fontSizeLabel = new JLabel(String.valueOf(this.textArea.getFont().getSize()) + " (size)");

        // aktualny stan pliku
        this.fileState = new JLabel("new (state)");

        jPanel.add(lForeground);
        jPanel.add(lBackground);
        jPanel.add(this.fontSizeLabel);
        jPanel.add(this.fileState);

        return jPanel;
    }
}
