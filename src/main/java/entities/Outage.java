package entities;

import java.time.LocalDateTime;

public record Outage(int customersAffected, LocalDateTime start, LocalDateTime end, double longitude, double latitude) {

}
