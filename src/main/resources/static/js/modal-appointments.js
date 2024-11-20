document.addEventListener('DOMContentLoaded', function () {
    var viewModal = document.getElementById('viewModal');
    viewModal.addEventListener('show.bs.modal', function (event) {
      var button = event.relatedTarget;
      var appointmentDate = button.getAttribute('data-appointment-date');
      var appointmentTime = button.getAttribute('data-appointment-time');
      var appointmentDoctor = button.getAttribute('data-appointment-doctor');
      var modalDate = viewModal.querySelector('#viewAppointmentDate');
      var modalTime = viewModal.querySelector('#viewAppointmentTime');
      var modalDoctor = viewModal.querySelector('#viewAppointmentDoctor');
      modalDate.textContent = appointmentDate;
      modalTime.textContent = appointmentTime;
      modalDoctor.textContent = appointmentDoctor;
    });

    var cancelModal = document.getElementById('cancelModal');
    cancelModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var appointmentId = button.getAttribute('data-appointment-id');
        var appointmentDate = button.getAttribute('data-appointment-date');
        var appointmentTime = button.getAttribute('data-appointment-time');
        var appointmentDoctor = button.getAttribute('data-appointment-doctor');
        var modalId = cancelModal.querySelector('#cancelAppointmentId');
        var modalDate = cancelModal.querySelector('#cancelAppointmentDate');
        var modalTime = cancelModal.querySelector('#cancelAppointmentTime');
        var modalDoctor = cancelModal.querySelector('#cancelAppointmentDoctor');
        var hiddenAppointmentId = cancelModal.querySelector('#hiddenAppointmentId');
        modalId.textContent = appointmentId;
        modalDate.textContent = appointmentDate;
        modalTime.textContent = appointmentTime;
        modalDoctor.textContent = appointmentDoctor;
        hiddenAppointmentId.value = appointmentId;
    });

    var viewModalFinished = document.getElementById('viewModalFinished');
      viewModalFinished.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var appointmentDate = button.getAttribute('data-appointment-date');
        var appointmentTime = button.getAttribute('data-appointment-time');
        var appointmentDoctor = button.getAttribute('data-appointment-doctor');

        var modalDate = viewModalFinished.querySelector('#viewAppointmentDateFinished');
        var modalTime = viewModalFinished.querySelector('#viewAppointmentTimeFinished');
        var modalDoctor = viewModalFinished.querySelector('#viewAppointmentDoctorFinished');

        modalDate.textContent = appointmentDate;
        modalTime.textContent = appointmentTime;
        modalDoctor.textContent = appointmentDoctor;
      });
  });