import java.util.*;

public class Game {
    private int numberOfPlayers;
    private ArrayList<Player> players = new ArrayList<>();
    private HashSet<Player> failedPlayers = new HashSet<>();
    private int turn = 0;

    private ArrayList<Cart> characterCarts = new ArrayList<>(List.of(
        new Cart(CartKind.CHARACTER, 1),
        new Cart(CartKind.CHARACTER, 2),
        new Cart(CartKind.CHARACTER, 3),
        new Cart(CartKind.CHARACTER, 4),
        new Cart(CartKind.CHARACTER, 5),
        new Cart(CartKind.CHARACTER, 6)
    ));

    private ArrayList<Cart> placeCarts = new ArrayList<>(List.of(
        new Cart(CartKind.PLACE, 1),
        new Cart(CartKind.PLACE, 2),
        new Cart(CartKind.PLACE, 3),
        new Cart(CartKind.PLACE, 4),
        new Cart(CartKind.PLACE, 5),
        new Cart(CartKind.PLACE, 6)
    ));

    private ArrayList<Cart> roomCarts = new ArrayList<>(List.of(
        new Cart(CartKind.ROOM, 1),
        new Cart(CartKind.ROOM, 2),
        new Cart(CartKind.ROOM, 3),
        new Cart(CartKind.ROOM, 4),
        new Cart(CartKind.ROOM, 5),
        new Cart(CartKind.ROOM, 6),
        new Cart(CartKind.ROOM, 7),
        new Cart(CartKind.ROOM, 8),
        new Cart(CartKind.ROOM, 9)
    ));

    private Cart pickedRoomCart;
    private Cart pickedCharacterCart;
    private Cart pickedPlaceCart;
    private Playground playground = new Playground();

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;

        Random random = new Random();

        this.pickedRoomCart = this.roomCarts.get(random.nextInt(this.roomCarts.size()));
        this.pickedPlaceCart = this.placeCarts.get(random.nextInt(this.placeCarts.size()));
        this.pickedCharacterCart = this.characterCarts.get(random.nextInt(this.characterCarts.size()));

        ArrayList<Cart> unassignedCarts = new ArrayList<>();
        unassignedCarts.addAll(characterCarts);
        unassignedCarts.remove(this.pickedCharacterCart);
        unassignedCarts.addAll(placeCarts);
        unassignedCarts.remove(this.pickedPlaceCart);
        unassignedCarts.addAll(roomCarts);
        unassignedCarts.remove(this.pickedRoomCart);

        for (int i = 0; i < numberOfPlayers; i++) {
            Cart[] assignedCarts = new Cart[9 - numberOfPlayers];
            for (int j = 0; j < 9 - numberOfPlayers; j++) {
                int x = random.nextInt(unassignedCarts.size());
                assignedCarts[j] = unassignedCarts.get(x);
                unassignedCarts.remove(x);
            }
            this.players.add(new Player(Arrays.asList(assignedCarts), this.roomCarts, this.placeCarts, this.characterCarts));
        }

        for (Player player : this.players) {
            playground.movePlayer(player, random.nextInt(9) + 1);
        }
    }

    public boolean nextTurn() {
        this.turn++;

        for (Player player : this.players) {
            int roomNumber = this.playground.getPlayerRoom(player);
            Cart room = this.roomCarts.get(roomNumber-1);
            Cart place = player.selectPlace(this.placeCarts);
            Cart character = player.selectCharacter(this.characterCarts);

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

    private boolean broadcastSelection(Player fromPlayer, Cart room, Cart place, Cart character) {
        ArrayList<Integer> counts = new ArrayList<>();
        for (Player player : this.players) {
            if (player != fromPlayer) {
                counts.add(player.countOwnedCarts(new Cart[]{room, place, character}));
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

            Cart[] guess = player.guessCart(room, place, character, copy);
            if (guess.length == 3) {
                if (guess[0] == this.pickedRoomCart && guess[1] == this.pickedPlaceCart && guess[2] == this.pickedCharacterCart) {
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
