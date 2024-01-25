# Hospital Appointment Management API

This API is designed to manage appointments for a hospital. It provides endpoints to create, retrieve, update, and delete appointments, allowing for efficient appointment scheduling and organization.

## Technologies Used

    Java
    Spring Boot
    MySQL (or your preferred database)
    Docker


## UML Class Diagram


## Features

    Create, retrieve, update, and delete appointments.
    Efficient scheduling of appointments for patients and doctors.
    Prevents overlapping appointments to avoid scheduling conflicts.
    Easily integrated into existing hospital management systems.

## Usage

The API provides the following endpoints:

    POST /api/appointment: Create a new appointment.
    GET /api/appointments/{id}: Retrieve details of a specific appointment.
    DELETE /api/appointments/{id}: Delete all appointments.
    DELETE /api/appointments/{id}: Delete a specific appointment.
    GET /api/appointments: Retrieve a list of all appointments.

    POST /api/doctor: Create a new doctor.
    GET /api/doctors/{id}: Retrieve details of a specific doctor.
    DELETE /api/doctors/{id}: Delete all doctors.
    DELETE /api/doctors/{id}: Delete a specific doctor.
    GET /api/doctors: Retrieve a list of all doctors.

    POST /api/patient: Create a new patient.
    GET /api/patients/{id}: Retrieve details of a specific patient.
    DELETE /api/patients/{id}: Delete all patients.
    DELETE /api/patients/{id}: Delete a specific patient.
    GET /api/patients: Retrieve a list of all patients.

    POST /api/room: Create a new room.
    GET /api/rooms/{roomName}: Retrieve details of a specific room.
    DELETE /api/rooms/{roomName}: Delete all room.
    DELETE /api/rooms/{roomName}: Delete a specific room.
    GET /api/rooms: Retrieve a list of all room.