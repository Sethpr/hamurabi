package hammurabi;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Hammurabi {
    Random rand = new Random();
    Scanner in = new Scanner(System.in);
    int localPopulation = 100;
    int populationLoss = 0;
    int immigrantAmount = 5;
    int destroyedByRats = 200;
    int grain = 2800;
    int acres = 1000;
    int landValue = 19;
    int year = 1;
    int harvest = 2000;
    int efficiency = 3;
    boolean bought;
    int fed;
    int plagueLosses;
    int starvationLosses;
    int planted;


    public static void main(String[] args) {
        new Hammurabi().startGame();
    }

    public void startGame() {
        for (int i = 1; i < 10; i++) {
            year = i;
            bought = false;
            startOfYear();
            acres += askHowManyAcresToBuy(landValue, grain);
            if(bought){
                acres -= askHowManyAcresToSell(acres);
            }
            fed = askHowMuchGrainToFeedPeople(grain);
            grain -= fed;
            planted = askHowMuchGrainToPlant(acres, localPopulation, grain);
            grain -= planted;
            plagueLosses = plagueDeaths(localPopulation);
            localPopulation-= plagueLosses;
            starvationLosses = starvationDeaths(localPopulation, fed);
            if(starvationLosses > 0 && uprising(localPopulation, starvationLosses)){
                gameOver();
                break;
            }
            localPopulation-=starvationLosses;
            if(starvationLosses == 0){
                immigrantAmount = immigrants(localPopulation, acres, grain);
            }else{
                immigrantAmount = 0;
            }
            localPopulation+= immigrantAmount;
            harvest = harvest(planted);
            grain -= grainEatenByRats(grain);
            landValue = newCostOfLand();
            grain += harvest;

        }
        print("\n\nThank you for playing");
    }


    void startOfYear(){
        print("O great Hammurabi!");
        print("You are in year " + year + " of your ten year rule");
        print("In the previous year " + populationLoss + " people starved to death");
        print("In the previous year " + immigrantAmount + " people entered your kingdom");
        print("The population is now " + localPopulation);
        print("We harvested " + harvest + " bushels at " + efficiency + " bushels per acre");
        print("Rats destroyed " + destroyedByRats + " bushels, leaving " + grain +" bushels in storage");
        print("The city owns "+ acres +" acres of land");
        print("Land is currently worth "+ landValue + " bushels");
    }

    public int askHowManyAcresToBuy(int price, int bushels) {
        int amount;
        do{
            print("How many acres of land would you like to buy this year?");
            amount = input();
            if(amount * price > bushels){
                print("You mustn't go into debt great Hammurabi, you only have " + bushels + " bushels of grain");
                print("Try buying less land\n");
            }else{
                print("As you wish");
                if(amount == 0){
                    bought = true;
                }
                grain -= amount * price;
                return amount;
            }
        }while(true);
    }

    public int askHowManyAcresToSell(int acres) {
        int amount;
        do{
            print("How many acres of land would you like to sell this year?");
            amount = input();
            if(amount >= acres){
                print("You mustn't sell all your land great Hammurabi. You only have " + acres + " acres of land");
                print("Try selling less land\n");
            }else{
                print("As you wish");
                grain += amount * landValue;
                return amount;
            }
        }while(true);
    }

    public int askHowMuchGrainToFeedPeople(int bushels){
        int amount;
        do{
            print("How much grain will you budget to feed the population this year?");
            amount = input();
            if(amount > bushels){
                print("Your generosity is unmatched, but we simply do not have that much grain to give!");
                print("You only have " + bushels + " bushels of grain");
                print("The population will have to get by with less grain\n");
            }else{
                print("As you wish");
                return amount;
            }
        }while(true);
    }

    public int askHowMuchGrainToPlant(int acresOwned, int population, int bushels){
        int amount;
        do{
            print("How much grain will the kingdom plant this year?");
            amount = input();
            if(amount > bushels){
                print("There is not enough grain to plant that many acres, you only have " + bushels + " bushels");
            }else if(amount > acresOwned){
                print("Your city is great, but it is not that great! You do not own that much land, only " + acresOwned + " acres");
            }else if(amount > population * 10){
                print("Your subjects would work themselves to death managing more than 10 acres per person.\n You simply must plant less. There are only " + population + " workers");
            }else{
                print("As you wish");
                return amount;
            }
        }while(true);
    }

    public int plagueDeaths(int population){
        if(rand.nextInt(100) <= 14){
            print("A plague has descended on your lands");
            return population/2;
        }
        return 0;
    }

    public int starvationDeaths(int population, int fed){
        if(population * 20 <= fed){
            print("The people enjoyed their year of plenty");
            return 0;
        }else{
            print("The people go hungry");
            return population - fed/20;
        }
    }

    public boolean uprising(int population, int starved){
        return (double) starved / population > .45;
    }

    public int immigrants(int localPopulation, int acres, int grain) {
        int amount = (20 * acres + grain)/(100 * localPopulation) +1;
        print(amount + " people immigrated into your city O great Hammurabi");
        return amount;
    }

    public int harvest(int planted){
        efficiency = rand.nextInt(6) + 1;
        if(efficiency > 4){
            print("What a bountiful year, the harvest was grand");
        } else if( efficiency < 2){
            print("We are stricken by famine, the coming years will be harsh as we recover");
        }
        return ((planted+1)/2) * efficiency;
    }

    public int grainEatenByRats(int bushels){
        if(rand.nextInt(100) <= 39){
            print("The wretched rats are back and have eaten some of our grain");
            return (int) (bushels * (double) (rand.nextInt(20) + 10)/100);
        }
        return 0;
    }

    public int newCostOfLand(){
        return rand.nextInt(7) + 17;
    }

    public void gameOver(){
        print("You failed as a king Hammurabi.");
        print("History will forget you");
    }


    public int input(){
        int value;
        while(true){
            try{
                value = in.nextInt();
                if(value < 0){
                    print("Please enter a valid number");
                }else {
                    return value;
                }
            }catch(InputMismatchException e){
                print("Please enter a valid number");
            }

        }
    }

    void print(String sentence){
        String[] workedSentence = sentence.split("");
        try {
            Thread.sleep(1);
            for(String letter: workedSentence){
                System.out.print(letter);
                //Thread.sleep(22); //this makes it look really nice in a terminal, not so nice in an ide
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }


}
