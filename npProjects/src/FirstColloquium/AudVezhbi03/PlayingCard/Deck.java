package FirstColloquium.AudVezhbi03.PlayingCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {

    private PlayingCard[] cards;
    private boolean[] isDealt; // дали картата на таа позиција е поделена или не
    private int dealtTotal; // број на поделени карти

    public Deck() {
        this.cards = new PlayingCard[52]; // Allocating memory
        this.isDealt = new boolean[52];
        this.dealtTotal = 0;

        for (int i = 0; i < PlayingCardType.values().length; i++) { // HEARTS | DIAMONDS | SPADES | CLUBS
            // PlayingCardType.values() - враќа цела низа
            // PlayingCardType.values().length - враќа по 14-пати од секој тип на карта

            for (int j = 0; j < 13; j++) { // 2|3|4|5|6|7|8|9}10|A|J|Q|K
                cards[i * 13 + j] = new PlayingCard(j + 2, PlayingCardType.values()[i]); // креирање на карта
                // cards[0 * 13 + 0] = new PlayingCard(2, HEARTS)
                // cards[0 * 13 + 1] = new PlayingCard(3, HEARTS)
            }
        }
    }

    public PlayingCard[] getCards() {
        return cards;
    }

    public void setCards(PlayingCard[] cards) {
        this.cards = cards;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Deck deck = (Deck) object;
        return Arrays.equals(cards, deck.cards);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(cards);
    }

    /*
    @Override
    public String toString() {
        String s = "";
        for (PlayingCard card : cards) {
            s += card.toString();
            s += "\n";
        }

        return s;
    }
    */

    // FROM STRING TO STRING-BUILDER
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (PlayingCard card : cards) {
            stringBuilder.append(card.toString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString(); // враќање string
    }

    public boolean hasCardsLeft() {
        return (cards.length - dealtTotal) > 0; // вк.карти - поделени
    }

    public PlayingCard[] shuffle() {

        /*
        *Arrays
        ==============================
        - операции со низи
        - Arrays.asList(cards).
             - .stream()
             - .add()
             - .sort()

        *Collections
        ==============================
        - се што е колекција
        - Collections.shuffle()
             - shuffle() - работи на листа, затоа треба да се креира листа која потоа ќе се меша
        */

        List<PlayingCard> playingCardList = Arrays.asList(cards);
        Collections.shuffle(playingCardList);

        return cards;

        // List е интерфејс кој е некаков тип на листа
        // List може да креира објект од тип ArrayList() или LinkedList()
        //    - List<PlayingCard> playingCardList = new ArrayList();
    }

    public PlayingCard dealCard() {
        if (!hasCardsLeft()) {
            return null;
        }

        // Math.random() - дава вредности од 0 до 1
        int card = (int) (Math.random() * 52);
        if (!isDealt[card]) { // ако картата не е поделена
            isDealt[card] = true;
            dealtTotal++;

            return cards[card]; // враќање на картата која во моментот е поделена
        }

        return dealCard(); // рекурзивен повик до функцијата за поделба на цел шпил од карти
    }

    public void dealCardSecondWay() {
        shuffle();

        for (PlayingCard card : cards) {
            System.out.println(card);
        }
    }

    public static void main(String[] args) {
        System.out.println("\n========== DECK-1 ==========\n");
        Deck deck1 = new Deck();
        System.out.println(deck1);

        System.out.println("========== SHUFFLE ==========\n");
        deck1.shuffle();
        System.out.println(deck1);

        System.out.println("========== DealCard 1stWay ==========\n");
        PlayingCard card;
        while ((card = deck1.dealCard()) != null) {
            System.out.println(card);
        }

        System.out.println("\n========== DECK-2 ==========\n");
        Deck deck2 = new Deck();
        System.out.println(deck2);

        System.out.println("========== DealCards 2ndWay ==========\n");
        deck2.dealCardSecondWay();
    }
}
