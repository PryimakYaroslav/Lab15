package task2;

import java.util.Arrays;
import java.util.List;

abstract class Dungeon {

    private final String name;
    private final int requiredLevel;

    public Dungeon(String name, int requiredLevel) {
        this.name = name;
        this.requiredLevel = requiredLevel;
    }

    // Загальний алгоритм проходження
    public final void completeDungeon(int playerLevel) {
        System.out.println("\n========== " + name + " ==========");

        enterDungeon(playerLevel);

        generateRooms();

        passRooms();

        optionalStage();

        fightBoss();

        giveReward();

        System.out.println("Підземелля " + name + " завершено!");
    }

    private void enterDungeon(int playerLevel) {
        System.out.println("Вхід до підземелля.");

        if (playerLevel < requiredLevel) {
            throw new IllegalArgumentException(
                    "Недостатній рівень! Потрібно: " + requiredLevel
            );
        }

        System.out.println("Рівень гравця перевірено: " + playerLevel);
    }

    // Етапи, які кожне підземелля реалізує по-своєму
    protected abstract void generateRooms();

    private void passRooms() {
        System.out.println("Гравець проходить кімнати.");
    }

    // Необов'язковий етап із поведінкою за замовчуванням
    protected void optionalStage() {
        System.out.println("Додаткових перешкод немає.");
    }

    protected abstract void fightBoss();

    private void giveReward() {
        System.out.println("Видано стандартну нагороду: 100 золота.");
    }
}

class Cave extends Dungeon {

    public Cave() {
        super("Печера", 5);
    }

    @Override
    protected void generateRooms() {
        System.out.println("Генерація кімнат печери:");
        System.out.println("- Темний тунель");
        System.out.println("- Підземне озеро");
        System.out.println("- Кам'яний зал");
    }

    @Override
    protected void fightBoss() {
        System.out.println("Бій з босом печери: Кам'яний голем.");
        System.out.println("Кам'яного голема переможено!");
    }
}

class IceCastle extends Dungeon {

    public IceCastle() {
        super("Крижаний замок", 10);
    }

    @Override
    protected void generateRooms() {
        System.out.println("Генерація кімнат крижаного замку:");
        System.out.println("- Крижаний коридор");
        System.out.println("- Замерзла зала");
        System.out.println("- Тронна кімната");
    }

    @Override
    protected void optionalStage() {
        System.out.println("Перевірка крижаних пасток:");

        System.out.println("- Крижана підлога");
        System.out.println("- Заморожувальна пастка");

        System.out.println("Гравець подолав пастки!");
    }

    @Override
    protected void fightBoss() {
        System.out.println("Бій з босом крижаного замку: Льодяний дракон.");
        System.out.println("Льодяного дракона переможено!");
    }
}

class Swamp extends Dungeon {

    public Swamp() {
        super("Болото", 7);
    }

    @Override
    protected void generateRooms() {
        System.out.println("Генерація кімнат болота:");
        System.out.println("- Трясовина");
        System.out.println("- Затоплена печера");
        System.out.println("- Гніздо чудовиська");
    }

    @Override
    protected void fightBoss() {
        System.out.println("Бій з босом болота: Болотний троль.");
        System.out.println("Болотного троля переможено!");
    }
}

public class Task2 {

    public static void main(String[] args) {

        List<Dungeon> dungeons = Arrays.asList(
                new Cave(),
                new IceCastle(),
                new Swamp()
        );

        int playerLevel = 15;

        for (Dungeon dungeon : dungeons) {
            dungeon.completeDungeon(playerLevel);
        }
    }
}
