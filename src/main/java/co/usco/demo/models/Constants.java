package co.usco.demo.models;

public class Constants {

    public enum Permission {
        READ, 
        CREATE, 
        UPDATE, 
        DELETE;
    }

    public enum AppointmentStatus {
        AVAILABLE, 
        SCHEDULED,
        FINISHED,
        CANCELED;
    }

    public enum DocumentType {
        LABORATORY_RESULT,
        DIAGNOSTIC_IMAGE,
        OTHER;
    }

    public enum MedicalSpecialty {
        GENERAL,
        DENTISTRY,
        LABORATORY,
        SPECIALIST;
    }

    public enum OrderStatus {
        PENDING,
        REQUESTING,
        AUTHORIZED,
        COMPLETED,
        IN_DELIVERY;
    }

    public enum OrderType {
        MEDICATION,
        LAB_APPOINTMENT,
        SPECIALIST_APPOINTMENT;
    }

    public enum AppointmentPlace {
        HOSPITAL,
        CLINIC,
        HOME;
    }

}
