package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    RoomRepository roomRepository;

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
    public ResponseEntity<List<Appointment>> createAppointment(@RequestBody Appointment appointment){
        /** TODO 
         * Implement this function, which acts as the POST /api/appointment endpoint.
         * Make sure to check out the whole project. Specially the Appointment.java class
         */
        try {
            // Validar si la cita se superpone con otras citas para la misma sala
            if (isOverlappingWithExisting(appointment)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Validar las fechas de inicio y fin de la cita
            if (appointment.getStartsAt() == null || appointment.getFinishesAt() == null ||
                    appointment.getStartsAt().isAfter(appointment.getFinishesAt())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Validar que el Doctor y el Patient existan en la base de datos
            if (isDoctorPatientValid(appointment) || appointment.getDoctor() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Validar que la Room exista en la base de datos
            if (!isRoomValid(appointment.getRoom().getRoomName())|| appointment.getRoom() == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }


            // Guardar la cita en la base de datos
            appointmentRepository.save(appointment);

            // Obtener y devolver la lista actualizada de citas
            List<Appointment> appointments = new ArrayList<>();
            appointmentRepository.findAll().forEach(appointments::add);

            return new ResponseEntity<>(appointments, HttpStatus.OK);
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir durante la creación de la cita
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    private boolean isOverlappingWithExisting(Appointment newAppointment){
        List<Appointment> existingAppointments = appointmentRepository.findAll();

        for (Appointment existingAppointment : existingAppointments) {
            if (existingAppointment.overlaps(newAppointment)) {
                return true; // Se superpone con otra cita existente
            }
        }

        return false;
    }
    // Método para verificar si el Doctor y el Patient existen en la base de datos
    private boolean isDoctorPatientValid(Appointment appointment) {
        return doctorRepository.findById(appointment.getDoctor().getId()).isEmpty()
                || patientRepository.findById(appointment.getPatient().getId()).isEmpty();
    }

    // Método para verificar si la Room existe en la base de datos
    private boolean isRoomValid(String roomName) {
        return roomRepository.findByRoomName(roomName).isPresent();
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
