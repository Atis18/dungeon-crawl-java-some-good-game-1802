package com.codecool.dungeoncrawl.logic.map_generation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void calculatesCenterForEvenDimensions() {
        Room room = new Room(10, 20, 6, 4);

        assertEquals(13, room.centerX());
        assertEquals(22, room.centerY());
    }

    @Test
    void calculatesCenterForOddDimensions() {
        Room room = new Room(10, 20, 5, 7);

        assertEquals(12, room.centerX());
        assertEquals(23, room.centerY());
    }

    @Test
    void overlappingRoomsIntersect() {
        Room first = new Room(2, 2, 5, 5);
        Room second = new Room(4, 4, 5, 5);

        assertTrue(first.intersects(second));
        assertTrue(second.intersects(first));
    }

    @Test
    void roomsSeparatedByOneTileAreStillConsideredTooClose() {
        Room first = new Room(1, 1, 4, 4);
        Room second = new Room(6, 1, 4, 4);

        assertTrue(first.intersects(second));
    }

    @Test
    void roomsWithEnoughHorizontalOrVerticalSeparationDoNotIntersect() {
        Room first = new Room(1, 1, 4, 4);

        assertFalse(first.intersects(new Room(7, 1, 4, 4)));
        assertFalse(first.intersects(new Room(1, 7, 4, 4)));
    }
}
