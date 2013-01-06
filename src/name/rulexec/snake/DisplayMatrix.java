package name.rulexec.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import name.rulexec.snake.game.IDisplayMatrix;
import name.rulexec.snake.game.PixelType;

public class DisplayMatrix implements IDisplayMatrix {
    private static final Color BG_COLOR = Color.decode("0xd3cab7");
    
    private BufferStrategy buffer;
    private Point offset;
    
    private Graphics g;
    
    public DisplayMatrix(BufferStrategy buffer, Point offset) {
        this.buffer = buffer;
        this.offset = offset;
    }
    
    public void reset() {
        // «Деактивируем» все пиксели заливая всё фоном
        Graphics g = this.buffer.getDrawGraphics();
        g.setColor(DisplayMatrix.BG_COLOR);
        g.translate(offset.x, offset.y);
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
        g.dispose();
        this.buffer.show();
    }
    
    public void startDraw() {
        this.g = this.buffer.getDrawGraphics();
        this.g.translate(this.offset.x, this.offset.y);
    }
    
    public void setPixel(int x, int y, PixelType type) {
        Color color = Color.BLACK;;
        
        // Вычисляем координаты пикселя
        x = 5 + x * 22;
        y = 5 + y * 22;
        
        boolean onlyEat = false;
        boolean withEat = false;
        
        // В зависимости от типа, выбираем цвет и производим тонкую настройку Вселенной
        switch (type) {
            case BLOCK: color = Color.decode("0x574c34"); break;
            case SNAKE_HEAD: color = Color.decode("0xca2721"); break;
            case SNAKE_HEAD_WITH_EAT:  color = Color.decode("0xca2721"); withEat = true; break;
            case SNAKE_BODY: color = Color.decode("0xe4605c"); break;
            case SNAKE_BODY_WITH_EAT: color = Color.decode("0xe4605c"); withEat = true; break;
            case EAT: onlyEat = true; withEat = true; break;
            default: color = Color.BLACK;
        }
        
        if (!onlyEat) {
            this.g.setColor(color);
            this.g.fillRect(x, y, 20, 20);
        }
        
        if (withEat) {
            this.g.setColor(Color.decode("0xf6d440"));
            int offset = onlyEat ? 5 : 6;
            int size = onlyEat ? 10 : 8;
            this.g.fillRect(x + offset, y + offset, size, size);
        }
    }
    public void disablePixel(int x, int y) {
        this.g.setColor(DisplayMatrix.BG_COLOR);
        
        x = 5 + x * 22 - 1;
        y = 5 + y * 22 - 1;
        
        this.g.fillRect(x, y, 22, 22);
    }
    
    public void endDraw() {
        this.g.dispose();
        this.buffer.show();
    }
}
