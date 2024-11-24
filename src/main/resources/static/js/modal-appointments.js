document.addEventListener('DOMContentLoaded', function () {
    var viewModal = document.getElementById('viewModal');
    viewModal.addEventListener('show.bs.modal', function (event) {
      var button = event.relatedTarget;
      var appointmentDate = button.getAttribute('data-appointment-date');
      var appointmentTime = button.getAttribute('data-appointment-time');
      var appointmentDoctor = button.getAttribute('data-appointment-doctor');
      var appointmentPlace = button.getAttribute('data-appointment-place');
      var modalDate = viewModal.querySelector('#viewAppointmentDate');
      var modalTime = viewModal.querySelector('#viewAppointmentTime');
      var modalDoctor = viewModal.querySelector('#viewAppointmentDoctor');
      var modalPlace = viewModal.querySelector('#viewAppointmentPlace');
      modalDate.textContent = appointmentDate;
      modalTime.textContent = appointmentTime;
      modalDoctor.textContent = appointmentDoctor;
      modalPlace.textContent = appointmentPlace;
    });

    var cancelModal = document.getElementById('cancelModal');
    cancelModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var appointmentId = button.getAttribute('data-appointment-id');
        var appointmentDate = button.getAttribute('data-appointment-date');
        var appointmentTime = button.getAttribute('data-appointment-time');
        var appointmentDoctor = button.getAttribute('data-appointment-doctor');
        var appointmentPlace = button.getAttribute('data-appointment-place');
        var modalId = cancelModal.querySelector('#cancelAppointmentId');
        var modalDate = cancelModal.querySelector('#cancelAppointmentDate');
        var modalTime = cancelModal.querySelector('#cancelAppointmentTime');
        var modalDoctor = cancelModal.querySelector('#cancelAppointmentDoctor');
        var modalPlace = cancelModal.querySelector('#cancelAppointmentPlace');
        var hiddenAppointmentId = cancelModal.querySelector('#hiddenAppointmentId');
        modalId.textContent = appointmentId;
        modalDate.textContent = appointmentDate;
        modalTime.textContent = appointmentTime;
        modalDoctor.textContent = appointmentDoctor;
        modalPlace.textContent = appointmentPlace;
        hiddenAppointmentId.value = appointmentId;
    });

    var viewModalFinished = document.getElementById('viewModalFinished');
      viewModalFinished.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget;
        var appointmentDate = button.getAttribute('data-appointment-date');
        var appointmentTime = button.getAttribute('data-appointment-time');
        var appointmentDoctor = button.getAttribute('data-appointment-doctor');
        var appointmentPlace = button.getAttribute('data-appointment-place');
        var modalDate = viewModalFinished.querySelector('#viewAppointmentDateFinished');
        var modalTime = viewModalFinished.querySelector('#viewAppointmentTimeFinished');
        var modalDoctor = viewModalFinished.querySelector('#viewAppointmentDoctorFinished');
        var modalPlace = viewModalFinished.querySelector('#viewAppointmentPlaceFinished');
        modalDate.textContent = appointmentDate;
        modalTime.textContent = appointmentTime;
        modalDoctor.textContent = appointmentDoctor;
        modalPlace.textContent = appointmentPlace;
      });
  });

  document.addEventListener('DOMContentLoaded', function () {
    const orderSelect = document.getElementById('order');
    const orderIdInput = document.getElementById('orderId');
    orderSelect.addEventListener('change', function () {
      const selectedOrderId = orderSelect.value;
      orderIdInput.value = selectedOrderId;
    });
    document.querySelector('button[data-bs-toggle="modal"]').addEventListener('click', function () {
      const selectedOrderId = orderSelect.value; 
      if (!selectedOrderId) {
        alert('Por favor, selecciona una orden antes de continuar.');
        return;
      }
      orderIdInput.value = selectedOrderId; 
    });
  });