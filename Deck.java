import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Deck{
    //Private variables for the entire class
    private Card [] cards = new Card[52];
    private int count = 52;

    /**
     * Default constructor. Creates 52 unique `Card`s based on the potential values and suits
     */
    public Deck(){
        int card = 0;

    //for loop that runs through and makes all 52 cards of the deck
        for(int i = 0; i < 13; i++){
            for(int n = 0; n < 4; n++){
                cards[card] = new Card(Card.SUITS[n], Card.VALUES[i]);
                card++;
            }
        }
    }
    
    /**
     * Returns how many "undealt" cards are in `Deck`
     * @return
     */
    public int numLeft(){
        return count;
    }

    /**
     * Takes a card off the "top" of the `Deck` and returns it.
     * @return
     */
    public Card deal(){
        count -= 1;
    
        return this.cards[count];
    }

    /**
     * Restores the `Deck` to "full" and randomizes the order of the `Card`s to be dealt
     */
    public void shuffle(){
        count = 52;
        
        Random random = ThreadLocalRandom.current();
    
    //for loop that does a Fisher Yates shuffle thing on the deck
        for (int i = 51; i >= 0; i--)
        {
            int index = random.nextInt(i + 1);
      
            Card a = cards[index];
            cards[index] = cards[i];
            cards[i] = a;
        }
    }
}