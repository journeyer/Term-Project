import java.util.*;

public class Player {
    private HashSet<Card> missingCharacterCards;
    private HashSet<Card> missingRoomCards;
    private HashSet<Card> missingPlaceCards;

    private HashSet<Card> cards;
    private int number;

    public Player(List<Card> cards, List<Card> totalRoomCards, List<Card> totalPlaceCards, List<Card> totalCharacterCards) {
        this.cards = new HashSet<>(cards);
        this.missingCharacterCards = new HashSet<>(totalCharacterCards);
        this.missingCharacterCards.removeAll(this.cards);
        this.missingPlaceCards = new HashSet<>(totalPlaceCards);
        this.missingPlaceCards.removeAll(this.cards);
        this.missingRoomCards = new HashSet<>(totalRoomCards);
        this.missingRoomCards.removeAll(this.cards);
    }

    public Card selectCharacter(List<Card> characters) {
        Random random = new Random();
        return characters.get(random.nextInt(characters.size()));
    }

    public Card selectPlace(List<Card> places) {
        Random random = new Random();
        return places.get(random.nextInt(places.size()));
    }

    public int countOwnedCards(Card[] selectedCards) {
        int count = 0;
        for (Card card : selectedCards) {
            if (this.cards.contains(card)) {
                count++;
            }
        }
        return count;
    }

    public Card[] guessCard(Card selectedRoom, Card selectedPlace, Card selectedCharacter, List<Integer> ownedCardsCount) {
        int totalOwnedCards = 0;
        for (Integer n : ownedCardsCount) {
            totalOwnedCards += n;
        }

        if (this.cards.contains(selectedRoom)) {
            totalOwnedCards++;           
        }
        if (this.cards.contains(selectedPlace)) {
            totalOwnedCards++;           
        }
        if (this.cards.contains(selectedCharacter)) {
            totalOwnedCards++;           
        }

        if (totalOwnedCards == 3) {
            this.missingRoomCards.remove(selectedRoom);
            this.missingPlaceCards.remove(selectedPlace);
            this.missingCharacterCards.remove(selectedCharacter);
        }

        if (this.missingRoomCards.size() == 1 && this.missingPlaceCards.size() == 1 && this.missingCharacterCards.size() == 1) {
            return new Card[]{this.missingRoomCards.iterator().next(), this.missingPlaceCards.iterator().next(), this.missingCharacterCards.iterator().next()};
        }

        return new Card[]{};
    }

    public Integer selectDestination(ArrayList<Integer> allowedRoomNumbers) {
        Random random = new Random();
        return allowedRoomNumbers.get(random.nextInt(allowedRoomNumbers.size()));
    }
}
