package name.rulexec.snake.game;

public class Game {
    private IDisplayMatrix displayMatrix;
    
    private Snake snake;
    private Direction direction;
    
    private float timer = 0;
    
    public Game() {
        this.snake = new Snake(14, 11);
        this.direction = Direction.LEFT;
    }
    
    public void setDisplayMatrix(IDisplayMatrix dm) {
        this.displayMatrix = dm;
    }
    
    public void init() {
        // Показываем границы
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 28; i++) {
                this.displayMatrix.setPixel(i, j * 21, PixelType.BLOCK);
            }
        }
        
        for (int j = 0; j < 2; j++) {
            for (int i = 1; i < 21; i++) {
                this.displayMatrix.setPixel(j * 27, i, PixelType.BLOCK);
            }
        }
    }
    
    public void changeDirection(Direction d) {
        this.direction = d;
    }
    
    public boolean step(float deltaT) {
        // Прибавляем к прошедшему времени, сколько прошло
        this.timer += deltaT;
        
        boolean alive = true;
        
        // Если прошло больше, чем 300мс
        while (this.timer > 0.3) {
            this.timer -= 0.3;
            
            // То пересчитываем игровую ситуацию
            alive = this.recalc();
        }

        return alive;
    }
    
    private boolean recalc() {
        // Стираем прошлое местоположение головы
        this.displayMatrix.disablePixel(this.snake.x, this.snake.y);
        // Передвигаем в нужную сторону
        this.snake.move(this.direction);
        // Отображаем пиксель на новом месте
        this.displayMatrix.setPixel(this.snake.x, this.snake.y, PixelType.SNAKE_HEAD);
        
        // Если врезались в границу, заканчиваем это безобразие
        if (this.snake.x == 0 || this.snake.x == 27 ||
            this.snake.y == 0 || this.snake.y == 21)
        {
            return false;
        } else {
            return true;
        }
    }
}
