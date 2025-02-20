package co.usco.demo.controllers;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import co.usco.demo.models.Constants;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.ControllerHelperService;
import co.usco.demo.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ControllerHelperService controllerHelperService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        controllerHelperService.addCommonAttributes(model, "/admin/dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/support-staff")
    public String patients(Model model) {
        controllerHelperService.addCommonAttributes(model, "/admin/support-staff");
        model.addAttribute("patients", userService.getAllUsersByRol("ROLE_SUPPORT_STAFF"));
        return "admin/support-staff";
    }

    @GetMapping("/medical-staff")
    public String medicalStaff(Model model) {
        controllerHelperService.addCommonAttributes(model, "/admin/medical-staff");
        model.addAttribute("patients", userService.getAllUsersByRol("ROLE_MEDICAL_STAFF"));
        return "admin/medical-staff";
    }

    @GetMapping("/register-staff")
    public String registerStaff(Model model) {
        controllerHelperService.addCommonAttributes(model, "/admin/register-staff");
        return "admin/register-staff";
    }

    @PostMapping("/register-staff")
    public String registerStaff(@RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String documentType,
                                @RequestParam String documentNumber,
                                @RequestParam String gender,
                                @RequestParam String phoneNumber,
                                @RequestParam String email,
                                @RequestParam String address,
                                @RequestParam String department,
                                @RequestParam String city,
                                @RequestParam String password,
                                @RequestParam String role,
                                @RequestParam(required = false) String medicalSpecialty,
                                @RequestParam(required = false) String horaryStart,
                                @RequestParam(required = false) String horaryEnd) {
        UserModel user = UserModel.builder()
                .firstName(firstName)
                .lastName(lastName)
                .documentType(documentType)
                .documentNumber(documentNumber)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .email(email)
                .address(address)
                .department(department)
                .city(city)
                .password(password)
                .userActive(true)
                .build();

        if (role.equals("ROLE_MEDICAL_STAFF")) {
            user.setMedicalSpecialty(Constants.MedicalSpecialty.valueOf(medicalSpecialty));
            user.setHoraryStart(LocalTime.parse(horaryStart));
            user.setHoraryEnd(LocalTime.parse(horaryEnd));
        }

        userService.registerStaff(user, role);
        return "redirect:/admin/register-staff";
    }


    @GetMapping("/assign-role")
    public String searchPatient(@RequestParam String documentNumber, Model model) {
        controllerHelperService.addCommonAttributes(model, "/admin/register-staff");
        Optional<UserModel> patient = userService.findByDocumentNumber(documentNumber);
        if (patient.isPresent()) {
            model.addAttribute("patients", List.of(patient.get()));
        } else {
            model.addAttribute("patients", null);
        }
        return "admin/register-staff";
    }

    @PostMapping("/assign-role")
    public String assignRole(@RequestParam Long patientId,
                             @RequestParam String newRole,
                             @RequestParam(required = false) String medicalSpecialty) {
        userService.assignRole(patientId, newRole, medicalSpecialty);
        return "redirect:/admin/register-staff";
    }

}
