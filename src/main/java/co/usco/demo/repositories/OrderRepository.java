package co.usco.demo.repositories;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import co.usco.demo.models.Constants;
import co.usco.demo.models.OrderModel;
import co.usco.demo.models.UserModel;

public interface OrderRepository extends JpaRepository<OrderModel, Long> {

    List<OrderModel> findByPatient(UserModel user, Sort sort);

    List<OrderModel> findByPatientAndStatus(UserModel user, Constants.OrderStatus status, Sort sort);

    List<OrderModel> findByPatientAndOrderTypeAndStatus(UserModel user, Constants.OrderType type, Constants.OrderStatus status, Sort sort);

}
