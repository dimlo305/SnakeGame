package name.rulexec.snake.game;

import name.rulexec.snake.Main;

public class Game {
    private IDisplayMatrix displayMatrix;
    
    private Snake snake;
    private Direction direction;
    
    private float timer = 0;
    
    private int eatX, eatY;
    
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
        
        // Генерируем первую еду
        this.spawnEat();
    }
    
    private void spawnEat() {
        int x, y;
        
        // Генерируем случайные координаты до тех пор,
        // пока клетка не окажется свободной 
        do {
            x = 1 + Main.rand.nextInt(26);
            y = 1 + Main.rand.nextInt(20);
        } while (this.snake.isOccupied(x, y));
        
        // Отображаем пиксель еды
        this.displayMatrix.setPixel(x, y, PixelType.EAT);
        
        this.eatX = x;
        this.eatY = y;
    }
    
    public void changeDirection(Direction d) {
        // Меняем направление, если туда можно двигаться
        if (this.snake.isPossibleMove(d))
            this.direction = d;
    }
    
    public boolean step(float deltaT) {
        // Прибавляем к прошедшему времени, сколько прошло
        this.timer += deltaT;
        
        // Если прошло больше, чем 200мс
        while (this.timer > 0.2) {
            this.timer -= 0.2;
            
            // То пересчитываем игровую ситуацию
            boolean alive = this.recalc();
            if (!alive) return false;
        }

        return true;
    }
    
    private boolean recalc() {
        // Меняем пиксель головы на пиксель тела
        Segment head = this.snake.getHead();
        this.displayMatrix.setPixel(head.x, head.y, head.feeded ? PixelType.SNAKE_BODY_WITH_EAT : PixelType.SNAKE_BODY);
        // Стираем прошлое местоположение хвоста
        Segment tail = this.snake.getTail().clone();
        this.displayMatrix.disablePixel(tail.x, tail.y);
        // Передвигаем в нужную сторону
        boolean isIntersects = this.snake.move(this.direction);
        
        // Получаем новое местоположение головы
        head = this.snake.getHead();
        
        // Если врезались в границу, либо же в саму себя,
        // заканчиваем это безобразие.
        if (head.x == 0 || head.x == 27 ||
            head.y == 0 || head.y == 21 || isIntersects)
        {
            return false;
        }
        
        PixelType headPixelType;
        
        // Если столкнулись с едой
        if (head.x == this.eatX && head.y == this.eatY) {
            // "Кормим" змею
            this.snake.feed();
            // Создаём новой еды
            this.spawnEat();
            
            headPixelType = PixelType.SNAKE_HEAD_WITH_EAT;
        } else {
            headPixelType = PixelType.SNAKE_HEAD;
        }
        // Отображаем пиксель на новом месте головы
        this.displayMatrix.setPixel(head.x, head.y, headPixelType);
        
        // Проверяем, может зря мы так с хвостом.
        if (this.snake.isOccupied(tail.x, tail.y)) {
            this.displayMatrix.setPixel(tail.x, tail.y, PixelType.SNAKE_BODY);
        }
        
        return true;
    }
}
