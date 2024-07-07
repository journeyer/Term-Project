import java.util.*;

public class Player {
    private HashSet<Cart> missingCharacterCarts;
    private HashSet<Cart> missingRoomCarts;
    private HashSet<Cart> missingPlaceCarts;

    private HashSet<Cart> carts;
    private int number;

    public Player(List<Cart> carts, List<Cart> totalRoomCarts, List<Cart> totalPlaceCarts, List<Cart> totalCharacterCarts) {
        this.carts = new HashSet<>(carts);
        this.missingCharacterCarts = new HashSet<>(totalCharacterCarts);
        this.missingCharacterCarts.removeAll(this.carts);
        this.missingPlaceCarts = new HashSet<>(totalPlaceCarts);
        this.missingPlaceCarts.removeAll(this.carts);
        this.missingRoomCarts = new HashSet<>(totalRoomCarts);
        this.missingRoomCarts.removeAll(this.carts);
    }

    public Cart selectCharacter(List<Cart> characters) {
        Random random = new Random();
        return characters.get(random.nextInt(characters.size()));
    }

    public Cart selectPlace(List<Cart> places) {
        Random random = new Random();
        return places.get(random.nextInt(places.size()));
    }

    public int countOwnedCarts(Cart[] selectedCarts) {
        int count = 0;
        for (Cart cart : selectedCarts) {
            if (this.carts.contains(cart)) {
                count++;
            }
        }
        return count;
    }

    public Cart[] guessCart(Cart selectedRoom, Cart selectedPlace, Cart selectedCharacter, List<Integer> ownedCartsCount) {
        int totalOwnedCarts = 0;
        for (Integer n : ownedCartsCount) {
            totalOwnedCarts += n;
        }

        if (this.carts.contains(selectedRoom)) {
            totalOwnedCarts++;           
        }
        if (this.carts.contains(selectedPlace)) {
            totalOwnedCarts++;           
        }
        if (this.carts.contains(selectedCharacter)) {
            totalOwnedCarts++;           
        }

        if (totalOwnedCarts == 3) {
            this.missingRoomCarts.remove(selectedRoom);
            this.missingPlaceCarts.remove(selectedPlace);
            this.missingCharacterCarts.remove(selectedCharacter);
        }

        if (this.missingRoomCarts.size() == 1 && this.missingPlaceCarts.size() == 1 && this.missingCharacterCarts.size() == 1) {
            return new Cart[]{this.missingRoomCarts.iterator().next(), this.missingPlaceCarts.iterator().next(), this.missingCharacterCarts.iterator().next()};
        }

        return new Cart[]{};
    }

    public Integer selectDestination(ArrayList<Integer> allowedRoomNumbers) {
        Random random = new Random();
        return allowedRoomNumbers.get(random.nextInt(allowedRoomNumbers.size()));
    }
}
