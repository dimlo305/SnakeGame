package name.rulexec.snake.game;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

class Segment {
    public int x, y;
    boolean feeded = false;
    
    public Segment(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void move(Direction d) {
        switch (d) {
            case LEFT: this.x -= 1; break;
            case RIGHT: this.x += 1; break;
            case UP: this.y -= 1; break;
            case DOWN: this.y += 1; break;
        }
    }
    
    public Segment clone() {
        return new Segment(x, y);
    }
}

public class Snake {
    private Deque<Segment> segments;
    private boolean[][] occupied;
    
    public Snake(int x, int y, Direction d) {
        // Сегменты змеи
        this.segments = new ArrayDeque<Segment>();
        this.segments.add(new Segment(x, y));
        Segment tail = new Segment(x, y);
        tail.move(d);
        this.segments.addLast(tail);
        
        // Будем хранить массив, в котором будем запоминать,
        // занята ли эта клетка сегментом змеи.
        // Он нужен для проверки, не врезалась ли змея в саму себя.
        this.occupied = new boolean[28][22];
        this.occupied[x][y] = true;
    }
    
    public boolean move(Direction d) {
        // Логика перемещения проста.
        // Берём последний сегмент змеи, меняем его координаты
        // и ставим в голову змеи в соответствии с направлением.
        // Так же, если последний сегмент оказывается "покормленным",
        // тогда нужно добавить в конец новый сегмент.
        
        Segment first = this.segments.getFirst();
        Segment last = this.segments.pollLast();
        
        // Если покормлен, забываем об этом и добавляем в конец новый сегмент
        if (last.feeded) {
            last.feeded = false;
            
            this.segments.addLast(new Segment(last.x, last.y));
        } else {
            // Если не покормлен, то его больше там нет
            this.occupied[last.x][last.y] = false;
        }
        
        last.x = first.x;
        last.y = first.y;
        
        last.move(d);
        
        boolean isOccupied = this.occupied[last.x][last.y];
        this.occupied[last.x][last.y] = true;
        
        this.segments.addFirst(last);
        
        return isOccupied;
    }
    
    public void feed() {
        this.segments.getFirst().feeded = true;
    }
    
    // Проверяет, возможно ли двигаться в ту сторону
    // (т.е. нет ли там тела)
    public boolean isPossibleMove(Direction d) {
        Iterator<Segment> it = this.segments.iterator();
        Segment head = it.next();
        Segment next = it.next();
        
        switch (d) {
            case LEFT: return head.x <= next.x;
            case RIGHT: return head.x >= next.x;
            case UP: return head.y <= next.y;
            case DOWN: return head.y >= next.y;
            default: throw new RuntimeException("Impossible direction");
        }
    }
    
    public boolean isOccupied(int x, int y) {
        return this.occupied[x][y];
    }
    
    public Segment getHead() {
        return this.segments.getFirst();
    }
    public Segment getTail() {
        return this.segments.getLast();
    }
    
    public Iterable<Segment> getSegments() {
        return this.segments;
    }
}
