package name.rulexec.snake;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import name.rulexec.snake.game.Direction;
import name.rulexec.snake.game.Game;

public class Main implements Runnable {
    public static final int WIDTH = 624,
                            HEIGHT = 492;
    
    private JFrame frame;
    private JPanel panel;
    
    private DisplayMatrix matrix;
    private Game game;
    
    private long lastFrame;
    
    public void initUI() {
        // ����
        this.frame = new JFrame("Madness Snake");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.frame.setResizable(false);
        
        this.panel = new JPanel();
        this.panel.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.frame.add(this.panel);
        this.panel.setVisible(true);
        
        //System.out.println(this.panel.getBounds().x);
        
        
        // ����������
        this.frame.pack();
        this.frame.setVisible(true);
        
        // ����� ������� ������ ����
        Insets insets = this.frame.getInsets();
        Point offset = new Point(insets.left, insets.top);
        
        // ������ ��� �������, ������ ������ ������� "��������"
        this.frame.createBufferStrategy(2);
        this.matrix = new DisplayMatrix(this.frame.getBufferStrategy(), offset);
        
        // ������������ ������� ������ � ����
        this.frame.setFocusable(true);
        this.frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent arg0) {}
            public void keyReleased(KeyEvent arg0) {}
            
            // ����� ���� ������ �������
            public void keyPressed(KeyEvent event) {
                Direction direction = null;
                switch (event.getKeyCode()) {
                    case KeyEvent.VK_LEFT: direction = Direction.LEFT; break;
                    case KeyEvent.VK_RIGHT: direction = Direction.RIGHT; break;
                    case KeyEvent.VK_UP: direction = Direction.UP; break;
                    case KeyEvent.VK_DOWN: direction = Direction.DOWN; break;
                }
                
                if (direction != null) {
                    Main.this.game.changeDirection(direction);
                }
            }
        });
    }
    
    public void gameLoop() throws Exception {
        while (true) {
            // ������ ������ ����
            this.game = new Game();
            this.game.setDisplayMatrix(this.matrix); // �������� ��� ��������
            
            getDelta();
            
            this.matrix.startDraw();
            // ������������� ����
            this.game.init();
            this.matrix.endDraw();
            
            while (true) {
                float deltaT = this.getDelta();
                
                // �������, ��� �� ������� ����� ��������
                this.matrix.startDraw();
                
                // ������ ���� ��� ������� ������
                boolean go = this.game.step(deltaT);
                if (!go) break;
                
                // ����������� ��������� �� �������
                this.matrix.endDraw();
                
                // ���������, ������� �� ��� �������, ����� ���� �����
                // 30 ������ � �������
                long freeTime = 30 - (this.getTime() - this.lastFrame);
                if (freeTime > 0) Thread.sleep(freeTime);
            }
            
            // ���� ��������
            // ...
            // �������!
        }
    }
    
    public void run() {
        // �������������� ���������
        this.initUI();
        
        // �������� ������ ��� ���� �����,
        // ��� ���� ����� ���-�� ������ � ����, ���� �� ����� ��� ������,
        // ���� � ����� ���� ������� ������ �����
        (new Thread() {
            public void run() {
                try {
                    // ��������� ������ ������� ����!
                    Main.this.gameLoop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    public float getDelta() {
        long time = this.getTime();
        float delta = (time - this.lastFrame) / 1000f;
        this.lastFrame = time;

        return delta;
    }
    public long getTime() {
        return System.currentTimeMillis();
    }
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Main());
    }
}
