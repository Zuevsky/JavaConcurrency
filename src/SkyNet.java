/*
Существует две фракции: World and Wednesday и Фабрика: Factory.
Каждая из фракций пытается создать армию роботов, но для этого им нужны детали для роботов.
Детали для роботов делятся на: head, torso, hand, feet.
Они производятся нейтральной Factory, которая каждый день, производит не более 10 деталей.
Тип деталей выбирается случайным образом.
Ночью фракции отправляются на фабрику, для того чтобы раздобыть детали для роботов (каждая может унести не более 5 деталей).
Фракции и фабрика работают каждая в своем потоке.
Определить у кого будет сильнее армия спустя 100 дней.
*/

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class SkyNet {
    public static void main(String[] args) {
        Factory factory = new Factory("Factory");
        Fraction fraction1 = new Fraction("World", factory);
        Fraction fraction2 = new Fraction("Wednesday", factory);
        factory.start();
        fraction1.start();
        fraction2.start();
    }

    static class Factory extends Thread {
        ArrayDeque<String> storage = new ArrayDeque<>(10);
        int days = 1;

        public Factory(String name) {
            super(name);
        }

        public String detailGenerate() {
            String detail = "";
            int chance = (int) (Math.random() * 100);
            if (chance >= 0 & chance < 25) {
                detail = "head";
            } else if (chance >= 25 & chance < 50) {
                detail = "torso";
            } else if (chance >= 50 & chance < 75) {
                detail = "hand";
            } else if (chance >= 75 & chance < 100) {
                detail = "feet";
            }
            return detail;
        }

        public void addingDetail(String detail) {
            storage.addFirst(detail);
        }

        public void run() {
            synchronized (storage) {
                while(days < 100) {
                    while (storage.size() > 4) {
                        try {
                            storage.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    while (storage.size() < 10) {
                        String detail = detailGenerate();
                        addingDetail(detail);
                    }
                    days++;
                    storage.notifyAll();
                }
            }
        }
    }

    static class Fraction extends Thread {
        Map<String, Integer> detailStorage = new HashMap<String, Integer>();
        int robots = 0;
        Factory factory;
        boolean alreadyGotDetails;

        public Fraction(String name, Factory factory) {
            super(name);
            this.factory = factory;
            detailStorage.put("heads", 0);
            detailStorage.put("torsos", 0);
            detailStorage.put("hands", 0);
            detailStorage.put("feet", 0);
        }

        public void getDetailsFromFactory(Factory factory) {
            for (int i = 1; i < 6; i++) {
                String detail = factory.storage.pollLast();
                try {
                    switch (detail) {
                        case "head" -> detailStorage.replace("heads", detailStorage.get("heads") + 1);
                        case "torso" -> detailStorage.replace("torsos", detailStorage.get("torsos") + 1);
                        case "hand" -> detailStorage.replace("hands", detailStorage.get("hands") + 1);
                        case "feet" -> detailStorage.replace("feet", detailStorage.get("feet") + 1);
                    }
                } catch (NullPointerException npe) {
                }
            }
        }

        public boolean storageInventory() {
            boolean isEnoughDetails = false;
            if (detailStorage.get("heads") > 0 & detailStorage.get("torsos") > 0 & detailStorage.get("hands") > 1 & detailStorage.get("feet") > 1) {
                isEnoughDetails = true;
            }
            return isEnoughDetails;
        }

        public void robotBuilder() {
            detailStorage.replace("heads", detailStorage.get("heads") - 1);
            detailStorage.replace("torsos", detailStorage.get("torsos") - 1);
            detailStorage.replace("hands", detailStorage.get("hands") - 2);
            detailStorage.replace("feet", detailStorage.get("feet") - 2);
            robots++;
        }

        public void run() {
            synchronized (factory.storage) {
                while(factory.days < 100) {
                    while (factory.storage.size() < 5 | alreadyGotDetails) {
                        alreadyGotDetails = false;
                        try {
                            factory.storage.wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    getDetailsFromFactory(factory);
                    alreadyGotDetails = true;
                    while (storageInventory()) {
                        robotBuilder();
                    }
                    System.out.println("Фракция - " + getName() + ". День - " + factory.days + ". Роботов построено: " + robots);
                    factory.storage.notifyAll();
                }
            }
        }
    }
}
