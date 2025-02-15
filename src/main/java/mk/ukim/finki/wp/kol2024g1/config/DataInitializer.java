package mk.ukim.finki.wp.kol2024g1.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2024g1.model.Employee;
import mk.ukim.finki.wp.kol2024g1.model.Hotel;
import mk.ukim.finki.wp.kol2024g1.model.RoomType;
import mk.ukim.finki.wp.kol2024g1.repository.EmployeeRepository;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import mk.ukim.finki.wp.kol2024g1.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@AllArgsConstructor
@Component
public class DataInitializer {

    private final HotelService hotelService;
    private final ReservationService reservationService;
    private final EmployeeRepository employeeRepository;
    @Autowired(required = false)
    private final PasswordEncoder passwordEncoder;


    private RoomType randomize(int i) {
        if (i % 2 == 0) return RoomType.SINGLE;
        return RoomType.DOUBLE;
    }

    @PostConstruct
    public void initData() {


        Hotel lastHotel = null;
        for (int i = 1; i < 6; i++) {
            lastHotel = this.hotelService.create("Hotel: " + i);
        }
        String password = passwordEncoder != null ? passwordEncoder.encode("pwd") : "pwd";
        Employee manager = new Employee(1L, "manager", password, lastHotel, true);
        Employee receptionist = new Employee(2L, "user", password, lastHotel, false);

        employeeRepository.save(manager);
        employeeRepository.save(receptionist);

        for (int i = 1; i < 11; i++) {
            this.reservationService.create("Reservation: " + i, LocalDate.now().minusYears(25 + i), 0, this.randomize(i), this.hotelService.listAll().get((i - 1) % 5).getId());
        }
    }
}
