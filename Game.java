import java.util.*;

public class Game {
    private int numberOfPlayers;
    private ArrayList<Player> players = new ArrayList<>();
    private HashSet<Player> failedPlayers = new HashSet<>();
    private int turn = 0;

    private ArrayList<Card> characterCards = new ArrayList<>(List.of(
        new Card(CardKind.CHARACTER, 1),
        new Card(CardKind.CHARACTER, 2),
        new Card(CardKind.CHARACTER, 3),
        new Card(CardKind.CHARACTER, 4),
        new Card(CardKind.CHARACTER, 5),
        new Card(CardKind.CHARACTER, 6)
    ));

    private ArrayList<Card> placeCards = new ArrayList<>(List.of(
        new Card(CardKind.PLACE, 1),
        new Card(CardKind.PLACE, 2),
        new Card(CardKind.PLACE, 3),
        new Card(CardKind.PLACE, 4),
        new Card(CardKind.PLACE, 5),
        new Card(CardKind.PLACE, 6)
    ));

    private ArrayList<Card> roomCards = new ArrayList<>(List.of(
        new Card(CardKind.ROOM, 1),
        new Card(CardKind.ROOM, 2),
        new Card(CardKind.ROOM, 3),
        new Card(CardKind.ROOM, 4),
        new Card(CardKind.ROOM, 5),
        new Card(CardKind.ROOM, 6),
        new Card(CardKind.ROOM, 7),
        new Card(CardKind.ROOM, 8),
        new Card(CardKind.ROOM, 9)
    ));

    private Card pickedRoomCard;
    private Card pickedCharacterCard;
    private Card pickedPlaceCard;
    private Playground playground = new Playground();

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;

        Random random = new Random();

        this.pickedRoomCard = this.roomCards.get(random.nextInt(this.roomCards.size()));
        this.pickedPlaceCard = this.placeCards.get(random.nextInt(this.placeCards.size()));
        this.pickedCharacterCard = this.characterCards.get(random.nextInt(this.characterCards.size()));

        ArrayList<Card> unassignedCards = new ArrayList<>();
        unassignedCards.addAll(characterCards);
        unassignedCards.remove(this.pickedCharacterCard);
        unassignedCards.addAll(placeCards);
        unassignedCards.remove(this.pickedPlaceCard);
        unassignedCards.addAll(roomCards);
        unassignedCards.remove(this.pickedRoomCard);

        for (int i = 0; i < numberOfPlayers; i++) {
            Card[] assignedCards = new Card[9 - numberOfPlayers];
            for (int j = 0; j < 9 - numberOfPlayers; j++) {
                int x = random.nextInt(unassignedCards.size());
                assignedCards[j] = unassignedCards.get(x);
                unassignedCards.remove(x);
            }
            this.players.add(new Player(Arrays.asList(assignedCards), this.roomCards, this.placeCards, this.characterCards));
        }

        for (Player player : this.players) {
            playground.movePlayer(player, random.nextInt(9) + 1);
        }
    }

    public boolean nextTurn() {
        this.turn++;

        for (Player player : this.players) {
            int roomNumber = this.playground.getPlayerRoom(player);
            Card room = this.roomCards.get(roomNumber-1);
            Card place = player.selectPlace(this.placeCards);
            Card character = player.selectCharacter(this.characterCards);

            boolean finished = this.broadcastSelection(player, room, place, character);
            if (finished) {
                return true;
            }

            Random random = new Random();
            ArrayList<Integer> allowedDestinations = this.playground.getPlayerAllowedDestinations(player);
            ArrayList<Integer> copy = new ArrayList<>();
            int dice = random.nextInt(6);
            for (Integer x : allowedDestinations) {
                if (dice %2 == 0) {
                    if (x%2 == 0) {
                        copy.add(x);
                    }
                } else {
                    if (x%2 != 0) {
                        copy.add(x);
                    }
                }
            }
            Integer destination = player.selectDestination(allowedDestinations);
            this.playground.movePlayer(player, destination);
        }

        return false;
    }

    private boolean broadcastSelection(Player fromPlayer, Card room, Card place, Card character) {
        ArrayList<Integer> counts = new ArrayList<>();
        for (Player player : this.players) {
            if (player != fromPlayer) {
                counts.add(player.countOwnedCards(new Card[]{room, place, character}));
            }
        }

        int i = 0;
        for (Player player : this.players) {
            if (player == fromPlayer) {
                continue;
            }
            if (this.failedPlayers.contains(player)) {
                continue;
            }

            ArrayList<Integer> copy = new ArrayList<Integer>(counts);
            copy.remove(i++);

            Card[] guess = player.guessCard(room, place, character, copy);
            if (guess.length == 3) {
                if (guess[0] == this.pickedRoomCard && guess[1] == this.pickedPlaceCard && guess[2] == this.pickedCharacterCard) {
                    System.out.println("game finished in " + this.turn + " turns");
                } else {
                    this.failedPlayers.add(player);
                }
                return true;
            }
        }

        return false;
    }
}
