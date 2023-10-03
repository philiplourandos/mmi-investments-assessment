package za.co.momentummetropolitan.dto;

import java.time.LocalDate;

public record ClientInfoResponse(String name, String address, String email, String mobile, LocalDate birthday) {

}
