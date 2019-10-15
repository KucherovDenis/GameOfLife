package ru.sbt.javaschool.gameoflife;

public class GameOfLife {

    private static final char ALIVE = '*';
    private static final char DEAD = ' ';
    private static final char SEPARATOR = '|';

    //Размер вселенной
    private static int sizeUniverse;

    //Число отображаемых поколений
    private static int allGenerations;

    //Текущее поколение
    private static int currentGeneration = 0;

    //Массив "вселенная" клеток. Содержит * если клетка жива.
    private static boolean[][] universe;

    //предыдущее состояние вселенной
    private static boolean[][] prevUniverse = null;

    //Массив содержит число соседей для каждой клетки.
    private static int[][] neighbors;

    //Массив с координатами соседей
    private static final int[][] neighborsXY = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};

    public static void main(String[] args) {
        //значения по умолчанию
        int size = 10;
        int count = 100;

        if (args.length == 2) {
            size = Integer.parseInt(args[0]);
            count = Integer.parseInt(args[1]);
        }

        initialize(size, count);
        firstGeneration();
        showUniverse();
        run();
        endGame();
    }

    //Начальные значения переменных
    private static void initialize(int size, int count) {
        sizeUniverse = size;
        allGenerations = count;

        universe = new boolean[sizeUniverse][sizeUniverse];
        neighbors = new int[sizeUniverse][sizeUniverse];
    }

    //Выполнение игры.
    private static void run() {
        do {
            saveUniverse();
            nextGeneration();
            showUniverse();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!isEnd());
    }

    //Сохранение состояния вселенной.
    private static void saveUniverse() {
        if (prevUniverse == null) {
            prevUniverse = new boolean[sizeUniverse][sizeUniverse];
        } else {
            for (int i = 0; i < sizeUniverse; i++) {
                System.arraycopy(universe[i], 0, prevUniverse[i], 0, universe[i].length);
            }
        }

    }

    //Создание первого поколения случайным образом.
    private static void firstGeneration() {
        currentGeneration++;
        for (int i = 0; i < sizeUniverse; i++) {
            for (int j = 0; j < sizeUniverse; j++) {
                universe[i][j] = Math.random() > 0.75;
            }
        }
    }

    //Подсчет соседних живых клеток (соседей).
    private static void countingNeighbors() {
        for (int i = 0; i < sizeUniverse; i++) {
            for (int j = 0; j < sizeUniverse; j++) {
                int countNeighbors = 0;

                for(int[] k: neighborsXY){
                    int x = i + k[0];
                    int y = j + k[1];

                    if (x < 0) x = sizeUniverse - 1;
                    else if (x >= sizeUniverse) x = 0;

                    if (y < 0) y = sizeUniverse - 1;
                    else if (y >= sizeUniverse) y = 0;

                    if (universe[x][y])
                        countNeighbors++;
                }

                neighbors[i][j] = countNeighbors;
            }
        }
    }

    //Следующее поколение вселенной
    private static void nextGeneration() {
        currentGeneration++;
        countingNeighbors();

        for (int i = 0; i < sizeUniverse; i++) {
            for (int j = 0; j < sizeUniverse; j++) {
                universe[i][j] = (neighbors[i][j] == 3) || universe[i][j];
                universe[i][j] = ((neighbors[i][j] >= 2) && (neighbors[i][j] <= 3)) && universe[i][j];
            }
        }
    }

    //Метод определяет признак завершения игры. Возвращает true если игра окончена.
    private static boolean isEnd() {
        return currentGeneration == allGenerations || isDead() || isReplay();
    }

    //Метод определяет мертвы ли все клетки вселенной.
    private static boolean isDead() {
        boolean result = true;

        end:
        for (int i = 0; i < sizeUniverse; i++) {
            for (int j = 0; j < sizeUniverse; j++) {
                if (universe[i][j]) {
                    result = false;
                    break end;
                }
            }
        }

        return result;
    }

    //Метод определяет повторяется ли вселенная.
    private static boolean isReplay() {
        boolean result = true;

        end:
        for (int i = 0; i < sizeUniverse; i++) {
            for (int j = 0; j < sizeUniverse; j++) {
                if (prevUniverse[i][j] != universe[i][j]) {
                    result = false;
                    break end;
                }
            }
        }

        return result;
    }

    //Отображение Конец игры
    private static void endGame() {
        System.out.println("The End");
    }

    //Вывод вселенной на экран
    private static void showUniverse() {
        System.out.println(String.format("Generation of the universe №%d: ", currentGeneration));
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < sizeUniverse; i++) {
            out.append(SEPARATOR);

            for (int j = 0; j < sizeUniverse; j++) {
                if (universe[i][j]) out.append(ALIVE);
                else out.append(DEAD);

                out.append(SEPARATOR);
            }

            out.append('\n');
        }

        System.out.println(out.toString());
    }
}
