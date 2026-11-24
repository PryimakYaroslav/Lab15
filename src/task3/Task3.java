package task3;

import java.util.*;

class Player {
    String name;
    String playerClass;
    int contribution;

    public Player(String name, String playerClass, int contribution) {
        this.name = name;
        this.playerClass = playerClass;
        this.contribution = contribution;
    }
}

class Item {
    String name;
    String requiredClass;

    public Item(String name, String requiredClass) {
        this.name = name;
        this.requiredClass = requiredClass;
    }

    @Override
    public String toString() {
        return name;
    }
}

interface LootStrategy {
    void distribute(List<Item> items, List<Player> players);
}

// Стратегія 1 — розподіл по черзі
class RoundRobinStrategy implements LootStrategy {

    private int currentPlayer = 0;

    @Override
    public void distribute(List<Item> items, List<Player> players) {
        System.out.println("\nРозподіл по черзі:");

        for (Item item : items) {
            Player player = players.get(currentPlayer);

            System.out.println(player.name + " отримав " + item);

            currentPlayer = (currentPlayer + 1) % players.size();
        }
    }
}

// Стратегія 2 — випадковий розподіл
class RandomStrategy implements LootStrategy {

    private Random random = new Random();

    @Override
    public void distribute(List<Item> items, List<Player> players) {
        System.out.println("\nВипадковий розподіл:");

        for (Item item : items) {
            Player player = players.get(random.nextInt(players.size()));

            System.out.println(player.name + " отримав " + item);
        }
    }
}

// Стратегія 3 — розподіл за внеском у бій
class ContributionStrategy implements LootStrategy {

    @Override
    public void distribute(List<Item> items, List<Player> players) {
        System.out.println("\nРозподіл за внеском у бій:");

        List<Player> sortedPlayers = new ArrayList<>(players);

        sortedPlayers.sort(
                (p1, p2) -> Integer.compare(
                        p2.contribution, p1.contribution
                )
        );

        for (int i = 0; i < items.size(); i++) {
            Player player = sortedPlayers.get(i % sortedPlayers.size());

            System.out.println(
                    player.name + " (внесок: " +
                            player.contribution + ") отримав " + items.get(i)
            );
        }
    }
}

// Стратегія 4 — голосування лідера
class LeaderVoteStrategy implements LootStrategy {

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void distribute(List<Item> items, List<Player> players) {
        System.out.println("\nРозподіл через голосування лідера:");

        for (Item item : items) {
            System.out.println("\nКому віддати " + item + "?");

            for (int i = 0; i < players.size(); i++) {
                System.out.println((i + 1) + ". " + players.get(i).name);
            }

            System.out.print("Вибір лідера: ");

            int choice = scanner.nextInt();

            if (choice < 1 || choice > players.size()) {
                System.out.println("Некоректний вибір!");
                continue;
            }

            System.out.println(
                    players.get(choice - 1).name + " отримав " + item
            );
        }
    }
}

// Стратегія 5 — розподіл за потребою класу
class ClassNeedStrategy implements LootStrategy {

    @Override
    public void distribute(List<Item> items, List<Player> players) {
        System.out.println("\nРозподіл за потребою класу:");

        for (Item item : items) {
            Player selected = null;

            for (Player player : players) {
                if (player.playerClass.equalsIgnoreCase(item.requiredClass)) {
                    selected = player;
                    break;
                }
            }

            if (selected != null) {
                System.out.println(
                        selected.name + " (" + selected.playerClass +
                                ") отримав " + item
                );
            } else {
                System.out.println(
                        "Немає відповідного класу для предмета " + item
                );
            }
        }
    }
}

class Party {

    private List<Player> players;
    private LootStrategy strategy;

    public Party(List<Player> players, LootStrategy strategy) {
        this.players = players;
        this.strategy = strategy;
    }

    public void setStrategy(LootStrategy strategy) {
        this.strategy = strategy;
        System.out.println("\nЛідер змінив правило розподілу!");
    }

    public void distributeLoot(List<Item> items) {
        strategy.distribute(items, players);
    }
}

// Демонстрація роботи
public class Task3 {

    public static void main(String[] args) {

        List<Player> players = Arrays.asList(
                new Player("Артур", "Воїн", 90),
                new Player("Ліана", "Маг", 70),
                new Player("Рей", "Лучник", 50),
                new Player("Елла", "Лікар", 30)
        );

        Party party = new Party(
                players,
                new RoundRobinStrategy()
        );

        // Перші 5 предметів — розподіл по черзі
        List<Item> firstLoot = Arrays.asList(
                new Item("Меч", "Воїн"),
                new Item("Посох", "Маг"),
                new Item("Лук", "Лучник"),
                new Item("Зілля", "Лікар"),
                new Item("Щит", "Воїн")
        );

        party.distributeLoot(firstLoot);

        // Лідер змінює стратегію
        party.setStrategy(new ContributionStrategy());

        // Наступні 5 предметів — за внеском у бій
        List<Item> secondLoot = Arrays.asList(
                new Item("Кільце сили", "Воїн"),
                new Item("Книга магії", "Маг"),
                new Item("Арбалет", "Лучник"),
                new Item("Посох лікування", "Лікар"),
                new Item("Броня", "Воїн")
        );

        party.distributeLoot(secondLoot);

        // Додаємо нову стратегію без зміни класу Party
        party.setStrategy(new ClassNeedStrategy());

        List<Item> thirdLoot = Arrays.asList(
                new Item("Магічний кристал", "Маг"),
                new Item("Лікувальний посох", "Лікар"),
                new Item("Бойовий меч", "Воїн")
        );

        party.distributeLoot(thirdLoot);
    }
}