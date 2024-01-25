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

import javax.transaction.Transactional;

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


    /** Doctor Entity test
     *
     */
    @Test
    void createDoctorInstanceWithoutAttributes() {
        Doctor invalidDoctor = new Doctor();
        assertThat(invalidDoctor).isNotNull();
    }

    @Test
    void createDoctorInstance() {
        d1 = new Doctor("Shaun", "Murphy", 20, "shaun.murphy@thegooddoctor.com");
        assertThat(d1).isNotNull();
        assertThat(d1.getFirstName()).isEqualTo("Shaun");
        assertThat(d1.getLastName()).isEqualTo("Murphy");
        assertThat(d1.getAge()).isEqualTo(20);
        assertThat(d1.getEmail()).isEqualTo("shaun.murphy@thegooddoctor.com");
    }

    /** Patient
     *
     */
    @Test
    void createPatientInstanceWithoutAttributes() {
        Patient invalidPatient = new Patient();
        assertThat(invalidPatient).isNotNull();
    }

    @Test
    void createPatientInstance() {
        p1 = new Patient("Alice", "Johnson", 30, "alice.johnson@example.com");
        assertThat(p1).isNotNull();
        assertThat(p1.getFirstName()).isEqualTo("Alice");
        assertThat(p1.getLastName()).isEqualTo("Johnson");
        assertThat(p1.getAge()).isEqualTo(30);
        assertThat(p1.getEmail()).isEqualTo("alice.johnson@example.com");
    }

    /** Room
     *
     */
    @Test
    void createRoomInstanceWithoutAttributes() {
        Room invalidRoom = new Room();
        assertThat(invalidRoom).isNotNull();
    }

    @Test
    void createRoomInstance() {
        r1 = new Room("101");
        assertThat(r1).isNotNull();
        assertThat(r1.getRoomName()).isEqualTo("101");
    }

    /** Appointment
     *
     */
    @Test
    void createAppointmentInstance() {
        p1 = new Patient("Alice", "Johnson", 30, "alice.johnson@example.com");
        d1 = new Doctor("Shaun", "Murphy", 40, "shaun.murphy@thegooddoctor.com");
        r1 = new Room("101");

        LocalDateTime startsAt = LocalDateTime.now().plusHours(1);
        LocalDateTime finishesAt = startsAt.plusHours(2);

        a1 = new Appointment(p1, d1, r1, startsAt, finishesAt);
        assertThat(a1).isNotNull();
        assertThat(a1.getPatient()).isEqualTo(p1);
        assertThat(a1.getDoctor()).isEqualTo(d1);
        assertThat(a1.getRoom()).isEqualTo(r1);
        assertThat(a1.getStartsAt()).isEqualTo(startsAt);
        assertThat(a1.getFinishesAt()).isEqualTo(finishesAt);
    }

    @Test
    void createAppointmentInstanceWithoutAttributes() {
        Appointment invalidAppointment = new Appointment();
        assertThat(invalidAppointment).isNotNull();
    }


}
