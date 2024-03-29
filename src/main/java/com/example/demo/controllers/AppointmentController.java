package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;


    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments(){
        List<Appointment> appointments = new ArrayList<>();

        appointmentRepository.findAll().forEach(appointments::add);

        if (appointments.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id){
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()){
            return new ResponseEntity<>(appointment.get(),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/appointment")
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment){
        // Retrieve all existing appointments
        List<Appointment> existingAppointments = appointmentRepository.findAll();
        List<Appointment> appointments = new ArrayList<>();

        // Check if any required field in the appointment is null
        if(appointment == null || appointment.getPatient() == null || appointment.getDoctor() == null || appointment.getRoom() == null ||
             appointment.getStartsAt() == null || appointment.getFinishesAt() == null){
            return new ResponseEntity<>(appointments, HttpStatus.BAD_REQUEST);
        }

        // Check if the appointment end time is before or equal to the start time
        if (appointment.getFinishesAt().isBefore(appointment.getStartsAt()) || appointment.getFinishesAt().isEqual(appointment.getStartsAt())) {
            return new ResponseEntity<>(appointments, HttpStatus.BAD_REQUEST);
        }

        // Check if the appointment overlaps with existing appointments
        if (isOverlappingWithExisting(appointment, existingAppointments)) {
           if (isSameAppointment(appointment, existingAppointments)){
               return new ResponseEntity<>(appointments, HttpStatus.BAD_REQUEST);
           }
          return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Appointment overlaps with an existing one");
        }


        try {
            // Save the appointment to the database
            appointmentRepository.save(appointment);
            // Get and return the updated list of appointments
            appointmentRepository.findAll().forEach(appointments::add);
            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }

    }

    // Check if there is any overlap with existing appointments
    private boolean isOverlappingWithExisting(Appointment newAppointment, List<Appointment> existingAppointments) {
        // Check for overlap with each existing appointment
        return existingAppointments.stream().anyMatch(existingAppointment -> existingAppointment.overlaps(newAppointment));
    }

    // Check if it is the same appointment as any existing appointment
    private boolean isSameAppointment(Appointment appointment, List<Appointment> existingAppointments) {
        // Check for overlap with each existing appointment
        for (Appointment existingAppointment : existingAppointments) {
            if ((appointment.getPatient().equals(existingAppointment.getPatient()))
                    && (appointment.getDoctor().equals(existingAppointment.getDoctor()))
                    && (appointment.getRoom().equals(existingAppointment.getRoom()))
                    && (appointment.getStartsAt().isEqual(existingAppointment.getStartsAt()))
                    && (appointment.getFinishesAt().isEqual(existingAppointment.getFinishesAt())))
            {
                return true;
            }
        }
        return false;
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id){

        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments(){
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
