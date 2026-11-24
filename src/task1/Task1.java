
package task1;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class Log {
    static void write(String source, String event) {
        System.out.printf("[%s] [%s] %s%n",
                LocalTime.now().format(
                        DateTimeFormatter.ofPattern("HH:mm:ss")),
                source, event);
    }
}

// Інтерфейс стану монстра
interface MonsterState {
    String name();
    void update(Monster monster);
}

// Context — монстр
class Monster {
    private final String name;
    private int health = 100;
    private double distance = 30;
    private MonsterState state = new PatrolState();

    Monster(String name) {
        this.name = name;
    }

    int getHealth() {
        return health;
    }

    double getDistance() {
        return distance;
    }

    void setHealth(int health) {
        this.health = Math.max(0, Math.min(100, health));
    }

    void setDistance(double distance) {
        this.distance = Math.max(0, distance);
    }

    void log(String message) {
        Log.write(name, message);
    }

    void setState(MonsterState next) {
        log(state.name() + " -> " + next.name());
        state = next;
    }

    void update() {
        log("Стан: " + state.name()
                + "; здоров'я: " + health
                + "%; відстань: " + distance);

        state.update(this);
    }
}

// Стан 1 — патрулювання
class PatrolState implements MonsterState {

    public String name() {
        return "Патрулювання";
    }

    public void update(Monster m) {
        if (m.getHealth() < 20)
            m.setState(new FleeState());
        else if (m.getDistance() <= 15)
            m.setState(new ChaseState());
        else
            m.log("Патрулює територію");
    }
}

// Стан 2 — переслідування
class ChaseState implements MonsterState {

    public String name() {
        return "Переслідування";
    }

    public void update(Monster m) {
        if (m.getHealth() < 20)
            m.setState(new FleeState());
        else if (m.getDistance() > 25)
            m.setState(new ReturnState());
        else if (m.getDistance() <= 2)
            m.setState(new AttackState());
        else
            m.log("Переслідує гравця");
    }
}

// Стан 3 — атака
class AttackState implements MonsterState {

    public String name() {
        return "Атака";
    }

    public void update(Monster m) {
        if (m.getHealth() < 20)
            m.setState(new FleeState());
        else if (m.getDistance() > 25)
            m.setState(new ReturnState());
        else if (m.getDistance() > 2)
            m.setState(new ChaseState());
        else
            m.log("Атакує гравця");
    }
}

// Стан 4 — втеча
class FleeState implements MonsterState {

    public String name() {
        return "Втеча";
    }

    public void update(Monster m) {
        if (m.getDistance() > 25)
            m.setState(new ReturnState());
        else
            m.log("Тікає від гравця");
    }
}

// Стан 5 — повернення
class ReturnState implements MonsterState {

    public String name() {
        return "Повернення на спавн";
    }

    public void update(Monster m) {
        m.log("Повернувся на місце спавну; здоров'я відновлено");
        m.setHealth(100);
        m.setState(new PatrolState());
    }
}

// Демонстрація
public class Task1 {

    public static void main(String[] args) {
        runDemo();
    }

    static void runDemo() {

        Monster wolf = new Monster("Вовк");

        wolf.update();

        Log.write("Гравець", "Наближається до вовка");

        wolf.setDistance(10);
        wolf.update();
        wolf.update();

        wolf.setDistance(1);
        wolf.update();
        wolf.update();

        Log.write("Гравець",
                "Здоров'я вовка зменшилося до 15%");

        wolf.setHealth(15);
        wolf.update();
        wolf.update();

        Log.write("Гравець", "Відійшов далеко");

        wolf.setDistance(30);
        wolf.update();
        wolf.update();
        wolf.update();
    }
}
