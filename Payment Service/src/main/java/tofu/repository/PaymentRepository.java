package tofu.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tofu.domain.UserData;


@Repository
public interface PaymentRepository extends MongoRepository<UserData,Long> {

}
