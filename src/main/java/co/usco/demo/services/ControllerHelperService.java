package co.usco.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import co.usco.demo.models.UserModel;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ControllerHelperService {

    @Autowired
    private UserService userService;

    public String getPreviousPage(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null || referer.isEmpty()) {
            referer = "/home/MiSalud"; 
            return referer;
        }
        return referer;
    }

    public void addCommonAttributes(Model model, String currentUri) {
        UserModel user = userService.getSessionUser();
        model.addAttribute("user", user);
        model.addAttribute("currentUri", currentUri);
    }

}
