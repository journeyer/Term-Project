import java.util.*;

public class Playground {

    public class Room {
        private int number;

        public Room(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public int[] getNeighborNumbers() {
            if (number == 1) {
                return new int[]{};
            }
            if (number == 2) {
                return new int[]{9, 3};
            }
            if (number == 9) {
                return new int[]{3, 8};
            }
            return new int[]{number - 1, number + 1};
        }
    }

    private HashMap<Player, Room> playersRooms = new HashMap<>();

    public ArrayList<Integer> getPlayerAllowedDestinations(Player player) {
        HashSet<Integer> neighbors = new HashSet<>();
        for (int num : this.playersRooms.get(player).getNeighborNumbers()) {
            neighbors.add(num);
        }

        HashSet<Integer> rooms = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        rooms.removeAll(neighbors);
        return new ArrayList<>(rooms);
    }

    public void movePlayer(Player player, int roomNumber) {
        this.playersRooms.put(player, new Room(roomNumber));
    }

    public int getPlayerRoom(Player player) {
        return this.playersRooms.get(player).getNumber();
    }
}
