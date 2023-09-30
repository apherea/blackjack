import java.util.Scanner;

/**
 * @Jacob-Cho
 */

public class Blackjack{

    /**
     * This should instaniate any instance variables you have, especially your `Deck`. Don't forget, you are interacting with a *User* so you may need a `Scanner`.
     */
    public Blackjack(){
    }

    /**
    * The method to be called to actually play a game. This is where you implement the flow of the **Gameplay**.
    */
    public void play(){
        //Calling a bunch of needed variables
        boolean isPlaying = true;

        String userActiveHand = "";
        String dealerActiveHand = "";
        String choice;

        Deck blackJack = new Deck();
        Card[] userHand = new Card[9];
        Card[] dealerHand = new Card[9];
        Card userCard = new Card();
        Card dealerCard = new Card();

        //Prints a welcome statement
        System.out.println("Welcome, enjoy your stay.");
        System.out.println("Here are the House Rules: \n - Number Cards: Face value\n - Royals: 10 value\n - Ace: 11 value always");
        Scanner scan = new Scanner(System.in);

        while(isPlaying == true){
            //Stuff to clear
            choice = "";
            userActiveHand = "";
            dealerActiveHand = "";

            for(int i = 0; i < 9; i++){
                userHand[i] = null;
                dealerHand[i] = null; 
            }

            System.out.println("\nDealing cards...");
            blackJack.shuffle();

            //Deals the first two cards of the play for both dealer and the player
            for(int i = 0; i < 2; i++){
                userCard = blackJack.deal();
                dealerCard = blackJack.deal();

                userHand[i] = userCard;
                userActiveHand += (userHand[i]);

                dealerHand[i] = dealerCard;
                dealerActiveHand += (dealerHand[i]);
                if(i == 0){
                    userActiveHand += " | ";
                    dealerActiveHand += " | ";
                }
            }
            System.out.println("\nYour Hand: " + userActiveHand);
            System.out.println("Dealer's Hand: " + dealerActiveHand);

            //If either the player or the dealer gets blackjack or busts immediately, this if statement will go through
            if(isBlackjack(userHand) == true || isBlackjack(dealerHand) == true || isBust(userHand) == true || isBust(dealerHand) == true){
                System.out.println(determineResult(userHand, dealerHand));
            }

            //Asks the user if they want to hit or stay
            System.out.println("\nWould you like to (H)it or (S)tay: ");
            choice = scan.nextLine();

            //The if statements for if the user decides to hit or stay
            if(choice.toUpperCase().equals("HIT") || choice.toUpperCase().equals("H")){
                int userIndex = 2;
                userCard = blackJack.deal();
                userHand[userIndex] = userCard;

                //If the user busts immediately after drawing, then this will execute
                if(isBust(userHand) == true || isBlackjack(userHand) == true){
                    System.out.println("\nYour Hand: " + userHand[0] + " | " + userHand[1] + " | " + userHand[2]);
                    System.out.println(determineResult(userHand, dealerHand));
                } else{
                    userActiveHand += (" | " + userHand[userIndex]);

                    int dealerIndex = 2; 
                    while(dealerKeepHitting(dealerHand) == true){
                        System.out.println("");
                        System.out.println("\nYour Hand: " + userActiveHand);

                        dealerCard = blackJack.deal();
                        dealerHand[dealerIndex] = dealerCard;
                        dealerActiveHand += (" | " + dealerHand[dealerIndex]);
                        dealerIndex++;

                        System.out.println("Dealer's Hand: " + dealerActiveHand);
                    }

                    System.out.println("");
                    System.out.println("\nYour Hand: " + userActiveHand);
                    System.out.println("Dealer's Hand: " + dealerActiveHand);
                    System.out.println(determineResult(userHand, dealerHand));
                }
            //if statement for the stay results
            } else if(choice.toUpperCase().equals("STAY") || choice.toUpperCase().equals("S")){
                int dealerIndex = 2; 

                while(dealerKeepHitting(dealerHand) == true){
                    System.out.println("");
                    System.out.println("\nYour Hand: " + userActiveHand);

                    dealerCard = blackJack.deal();
                    dealerHand[dealerIndex] = dealerCard;
                    dealerActiveHand += (" | " + dealerHand[dealerIndex]);
                    dealerIndex++;

                    System.out.println("Dealer's Hand: " + dealerActiveHand);
                }

                System.out.println("");
                System.out.println("\nYour Hand: " + userActiveHand);
                System.out.println("Dealer's Hand: " + dealerActiveHand);
                System.out.println(determineResult(userHand, dealerHand));
            } else{
                while(!choice.toUpperCase().equals("HIT") && !choice.toUpperCase().equals("STAY")){
                    System.out.println("Invalid input, try again");
                    System.out.print("Would you like to (H)it or (S)tay: ");
                    choice = scan.nextLine();
                }
            }

            //Would you like to play again loop that makes them enter a valid input
            System.out.println("\nWould you like to play again? (Y)es/(N)o: ");
            choice = scan.nextLine();

            if(choice.toUpperCase().equals("YES")){
                isPlaying = true;
            } else if(choice.toUpperCase().equals("NO")){
                isPlaying = false;
            } else{
                while(!choice.toUpperCase().equals("YES") && !choice.toUpperCase().equals("NO")){
                    System.out.println("Invalid input, try again");
                    System.out.print("Would you like to play again? (Y)es/(N)o: ");
                    choice = scan.nextLine();
                }
            }
        }
        scan.close();
    }

    /**
     * This method takes an array of `Card`s and returns the amount of points that hand is worth. Does not alter parameters.
     * @param hand
     * @return
     */
    public static int calcPoints(Card[] hand){
        int total = 0;

        for(int i = 0; i < hand.length; i++){
            if(hand[i] == null){
                continue;
            }

            if(hand[i].getValue().toUpperCase().equals("KING") == true || hand[i].getValue().toUpperCase().equals("QUEEN") == true || hand[i].getValue().toUpperCase().equals("JACK") == true){
                total += 10;
            } else if(hand[i].getValue().toUpperCase().equals("ACE") == true){
                total += 11;
            } else {
                total += Integer.parseInt((hand[i].getValue()));
            }
        }

        return total;
    }

    /**
     * Returns a `String` in one of the following formats: `"User Wins"`, `"User Loses"`, or `"User Pushes"`. Does not alter parameters.
     * @param userHand
     * @param dealerHand
     * @return
     */
    public static String determineResult(Card[] userHand, Card[] dealerHand){
        if(isBust(userHand) == true){
            return "User Loses";
        } else if(isBust(dealerHand) == true){
            return "User Wins";
        } else if(isBlackjack(userHand) == true && isBlackjack(dealerHand) == true){
            return "User Pushes";
        } else if(isBlackjack(userHand) == true){
            return "User Wins";
        } else if(isBlackjack(dealerHand) == true){
            return "User Loses";
        } else if(calcPoints(userHand) == calcPoints(dealerHand)){
            return "User Pushes";
        } else if(calcPoints(userHand) > calcPoints(dealerHand)){
            return "User Wins";
        } else {
            return "User Loses";
        }
    }

    /**
     * returns `true` if the hand is a *Bust*, `false` otherwise. Does not alter parameters.
     * @param hand
     * @return
     */
    public static boolean isBust(Card[] hand){
        boolean result;
        
        result = false;
        if(Blackjack.calcPoints(hand) > 21){
            result = true;
        }

        return result;
    }

    /**
     * returns `true` if the hand is a *Blackjack*, `false` otherwise. Does not alter parameters.
     * @param hand
     * @return
     */
    public static boolean isBlackjack(Card[] hand){
        boolean result;

        result = false;
        if(hand.length == 2 && Blackjack.calcPoints(hand) == 21){
            result = true;
        }

        return result;
    }

    /**
     * return `true` if the dealer should keep hitting (score of hand is 16 or less), `false` otherwise. Does  not alter parameters.
     * @param hand
     * @return
     */
    public static boolean dealerKeepHitting(Card[] hand){
        boolean result;

        result = false;
        if(Blackjack.calcPoints(hand) <= 16){
            result = true;
        }

        return result;
    }
}