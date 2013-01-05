package name.rulexec.snake.game;

public class Game {
    private IDisplayMatrix displayMatrix;
    
    private Snake snake;
    private Direction direction;
    
    private float timer = 0;
    
    public Game() {
        this.snake = new Snake(14, 11, Direction.RIGHT);
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
        
        // Отображаем части змейки
        boolean isFirst = true;
        for (Segment s : this.snake.getSegments()) {
            if (isFirst) {
                this.displayMatrix.setPixel(s.x, s.y, PixelType.SNAKE_HEAD);
                isFirst = false;
            } else {
                this.displayMatrix.setPixel(s.x, s.y, PixelType.SNAKE_BODY);
            }
        }
    }
    
    public void changeDirection(Direction d) {
        // Меняем направление, если туда можно двигаться
        if (this.snake.isPossibleMove(d))
            this.direction = d;
    }
    
    public boolean step(float deltaT) {
        // Прибавляем к прошедшему времени, сколько прошло
        this.timer += deltaT;
        
        // Если прошло больше, чем 300мс
        while (this.timer > 0.3) {
            this.timer -= 0.3;
            
            // То пересчитываем игровую ситуацию
            boolean alive = this.recalc();
            if (!alive) return false;
        }

        return true;
    }
    
    private boolean recalc() {
        // Меняем пиксель головы на пиксель тела
        Segment head = this.snake.getHead();
        this.displayMatrix.setPixel(head.x, head.y, PixelType.SNAKE_BODY);
        // Стираем прошлое местоположение хвоста
        Segment tail = this.snake.getTail();
        this.displayMatrix.disablePixel(tail.x, tail.y);
        // Передвигаем в нужную сторону
        boolean isIntersects = this.snake.move(this.direction);
        // Отображаем пиксель на новом месте головы
        head = this.snake.getHead();
        this.displayMatrix.setPixel(head.x, head.y, PixelType.SNAKE_HEAD);
        
        // Если врезались в границу, либо же в саму себя,
        // заканчиваем это безобразие.
        if (head.x == 0 || head.x == 27 ||
            head.y == 0 || head.y == 21 || isIntersects)
        {
            return false;
        } else {
            return true;
        }
    }
}
