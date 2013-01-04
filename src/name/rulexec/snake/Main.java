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
        // Окно
        this.frame = new JFrame("Madness Snake");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.frame.setResizable(false);
        
        this.panel = new JPanel();
        this.panel.setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
        this.frame.add(this.panel);
        this.panel.setVisible(true);
        
        //System.out.println(this.panel.getBounds().x);
        
        
        // Отображаем
        this.frame.pack();
        this.frame.setVisible(true);
        
        // Узнаём размеры границ окна
        Insets insets = this.frame.getInsets();
        Point offset = new Point(insets.left, insets.top);
        
        // Создаём два буффера, создаём объект матрицы "пикселей"
        this.frame.createBufferStrategy(2);
        this.matrix = new DisplayMatrix(this.frame.getBufferStrategy(), offset);
        
        // Регистрируем нажания клавиш в окне
        this.frame.setFocusable(true);
        this.frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent arg0) {}
            public void keyReleased(KeyEvent arg0) {}
            
            // Когда была нажата клавиша
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
            // Создаём объект игры
            this.game = new Game();
            this.game.setDisplayMatrix(this.matrix); // Снабжаем его матрицей
            
            getDelta();
            
            this.matrix.startDraw();
            // Инициализация игры
            this.game.init();
            this.matrix.endDraw();
            
            while (true) {
                float deltaT = this.getDelta();
                
                // Говорим, что на матрицу можно рисовать
                this.matrix.startDraw();
                
                // Делаем один шаг игровой логики
                boolean go = this.game.step(deltaT);
                if (!go) break;
                
                // Заканчиваем рисование на матрицу
                this.matrix.endDraw();
                
                // Вычисляем, сколько бы нам поспать, чтобы было около
                // 30 кадров в секунду
                long freeTime = 30 - (this.getTime() - this.lastFrame);
                if (freeTime > 0) Thread.sleep(freeTime);
            }
            
            // Игра окончена
            // ...
            // РЕСТАРТ!
        }
    }
    
    public void run() {
        // Инициализируем интерфейс
        this.initUI();
        
        // Внезапно создаём ещё один поток,
        // Ибо если будем что-то делать в этом, пока мы будем это делать,
        // окно с игрой даже закрыть нельзя будет
        (new Thread() {
            public void run() {
                try {
                    // Запускаем чёртов игровой цикл!
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
