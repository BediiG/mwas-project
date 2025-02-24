package mk.ukim.finki.wp.kol2024g1.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wp.kol2024g1.model.Employee;
import mk.ukim.finki.wp.kol2024g1.model.Hotel;
import mk.ukim.finki.wp.kol2024g1.model.RoomType;
import mk.ukim.finki.wp.kol2024g1.repository.EmployeeRepository;
import mk.ukim.finki.wp.kol2024g1.service.HotelService;
import mk.ukim.finki.wp.kol2024g1.service.ReservationService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class DataInitializer {

    private final HotelService hotelService;
    private final ReservationService reservationService;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder; // No need for @Autowired

    // Ensure Hibernate generates RoomType
    private RoomType randomize(int i) {
        return (i % 2 == 0) ? RoomType.SINGLE : RoomType.DOUBLE;
    }

    @PostConstruct
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        System.out.println("Initializing database...");
        Hotel lastHotel = null;

        // Create 5 Hotels
        for (int i = 1; i < 6; i++) {
            lastHotel = this.hotelService.create("Hotel: " + i);
        }

        // Encode password
        String password = (passwordEncoder != null) ? passwordEncoder.encode("pwd") : "pwd";

        // Create employees with null ID (Hibernate will auto-generate IDs)
        Employee manager = new Employee(null, "manager", password, lastHotel, true);
        Employee receptionist = new Employee(null, "user", password, lastHotel, false);

        // Save employees
        System.out.println("Saving manager...");
        employeeRepository.save(manager);
        System.out.println("Saving receptionist...");
        employeeRepository.save(receptionist);

        // Create 10 Reservations
        for (int i = 1; i < 11; i++) {
            this.reservationService.create(
                    "Reservation: " + i,
                    LocalDate.now().minusYears(25 + i),
                    0,
                    this.randomize(i),
                    this.hotelService.listAll().get((i - 1) % 5).getId()
            );
        }
    }
}
