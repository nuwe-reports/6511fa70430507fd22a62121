package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

	private Doctor d1;

	private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    @BeforeAll
    void setupTestData() {
        entityManager.persist(d1);
        entityManager.persist(p1);
        entityManager.persist(r1);
        entityManager.persist(a1);
        entityManager.persist(a2);
        entityManager.persist(a3);
        entityManager.flush();
    }
    @Test
    void shouldSaveAndRetrieveDoctor() {
        Doctor retrievedDoctor = entityManager.find(Doctor.class, d1.getId());
        assertThat(retrievedDoctor).isEqualTo(d1);
    }

    @Test
    void shouldSaveAndRetrievePatient() {
        Patient retrievedPatient = entityManager.find(Patient.class, p1.getId());
        assertThat(retrievedPatient).isEqualTo(p1);
    }

    @Test
    void shouldSaveAndRetrieveRoom() {
        Room retrievedRoom = entityManager.find(Room.class, r1.getRoomName());
        assertThat(retrievedRoom).isEqualTo(r1.getRoomName());
    }

    @Test
    void shouldSaveAndRetrieveAppointment() {
        Appointment retrievedAppointment = entityManager.find(Appointment.class, a1.getId());
        assertThat(retrievedAppointment).isEqualTo(a1);
    }

    @Test
    void appointmentEntityShouldHaveCorrectGettersAndSetters() {
        assertThat(a1)
                .hasFieldOrPropertyWithValue("patient", p1)
                .hasFieldOrPropertyWithValue("doctor", d1)
                .hasFieldOrPropertyWithValue("room", r1)
                .hasFieldOrPropertyWithValue("startsAt", a1.getStartsAt())
                .hasFieldOrPropertyWithValue("finishesAt", a1.getFinishesAt());
    }

    @Test
    void appointmentEntityShouldHaveCorrectOverlapLogic() {
        // Implement tests for the overlaps method in the Appointment entity
        LocalDateTime startsAt = LocalDateTime.now();
        LocalDateTime finishesAt = startsAt.plusHours(2);

        Appointment overlappingAppointment = new Appointment(p1, d1, r1, startsAt.plusMinutes(30), finishesAt.plusMinutes(30));
        Appointment nonOverlappingAppointment = new Appointment(p1, d1, r1, startsAt.plusHours(3), finishesAt.plusHours(4));

        entityManager.persist(overlappingAppointment);
        entityManager.persist(nonOverlappingAppointment);
        entityManager.flush();

        assertThat(a1.overlaps(overlappingAppointment)).isTrue();
        assertThat(a2.overlaps(nonOverlappingAppointment)).isFalse();
    }


}
