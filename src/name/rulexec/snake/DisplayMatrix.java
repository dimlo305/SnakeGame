package name.rulexec.snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

import name.rulexec.snake.game.IDisplayMatrix;
import name.rulexec.snake.game.PixelType;

public class DisplayMatrix implements IDisplayMatrix {
    private BufferStrategy buffer;
    private Point offset;
    
    private Graphics g;
    
    public DisplayMatrix(BufferStrategy buffer, Point offset) {
        this.buffer = buffer;
        this.offset = offset;
        
        // Заливаем всё белым, для фона
        Graphics g = this.buffer.getDrawGraphics();
        g.setColor(Color.WHITE);
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
        Color color;
        
        // В зависимости от типа, выбираем цвет
        switch (type) {
            case BLOCK: color = Color.BLACK; break;
            case SNAKE_HEAD: color = Color.GRAY; break;
            default: color = Color.BLACK;
        }
        
        this.g.setColor(color);
        
        // Вычисляем координаты пикселя
        x = 5 + x * 22;
        y = 5 + y * 22;
        
        this.g.fillRect(x, y, 20, 20);
    }
    public void disablePixel(int x, int y) {
        this.g.setColor(Color.WHITE);
        
        x = 5 + x * 22 - 1;
        y = 5 + y * 22 - 1;
        
        this.g.fillRect(x, y, 22, 22);
    }
    
    public void endDraw() {
        this.g.dispose();
        this.buffer.show();
    }
}
