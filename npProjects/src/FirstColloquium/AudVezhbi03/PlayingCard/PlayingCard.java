package FirstColloquium.AudVezhbi03.PlayingCard;

import java.util.Objects;

public class PlayingCard {

    private int rank;
    private PlayingCardType cardType;

    public PlayingCard(int rank, PlayingCardType cardType) {
        this.rank = rank;
        this.cardType = cardType;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public PlayingCardType getCardType() {
        return cardType;
    }

    public void setCardType(PlayingCardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return String.format("%d-%s", rank, cardType.name()); // .toString() or .name()
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PlayingCard that = (PlayingCard) object;
        return rank == that.rank && cardType == that.cardType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, cardType);
    }

    public static void main(String[] args) {

    }
}
