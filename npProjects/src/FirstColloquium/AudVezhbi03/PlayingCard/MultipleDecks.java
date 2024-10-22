package FirstColloquium.AudVezhbi03.PlayingCard;

import java.util.Arrays;

public class MultipleDecks {

    private Deck[] decks; // може со листа

    public MultipleDecks(int numOfDecks) {
        this.decks = new Deck[numOfDecks];

        for (int i = 0; i < numOfDecks; i++) {
            decks[i] = new Deck();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        int i = 1;
        for (Deck deck : decks) {
            stringBuilder.append("(((>>>>> " + i++ + "-DECK <<<<<)))").append("\n");
            stringBuilder.append(deck.toString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {

        System.out.println("\n========== MULTIPLE-DECKS ==========\n");
        MultipleDecks multipleDecks = new MultipleDecks(3);
        System.out.println(multipleDecks);
    }
}
