package com.mytaxi.dataaccessobject.specifications;

import com.mytaxi.domainobject.DriverDO;
import com.mytaxi.domainvalue.EngineType;
import com.mytaxi.domainvalue.OnlineStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mytaxi.dataaccessobject.specifications.FilterNames.*;

public class DriverSpecifications {

    /**
     * Find a driver by car id
     * @param carId The car id
     * @return The selected by car id {@link Specification}
     */
    public static Specification<DriverDO> findByCarId(long carId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.join("carDO").get("id"), carId);
    }

    /**
     * Find a driver marked as deleted = false
     * @return The {@link Specification}
     */
    public static Specification<DriverDO> findNotDeleted() {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("deleted"), false);
    }

    /**
     * Use filters to create a search criteria.
     * @param filters
     * @return
     */
    public static Specification<DriverDO> withFilters (final Map<String, Object> filters) {
        List<Predicate> predicates = new ArrayList<>();

        return (root, query, cb) -> {
            root
               .fetch("carDO", JoinType.LEFT)
               .fetch("manufacturerDO", JoinType.LEFT);

            if (filters.containsKey(FIELD_USER_NAME)) {
                //use coalesce to avoid the nullification of the final concatenation in case any of the Strings are null
                Expression<String> usernameExpression = cb.coalesce(cb.lower(root.get("username")), "''");
                String value = (String) filters.get(FIELD_USER_NAME);
                predicates.add(cb.like(usernameExpression, String.format("%s%s%s", "%", value.trim().toLowerCase(), "%")));
            }
            if (filters.containsKey(FIELD_ONLINE_STATUS)) {
                OnlineStatus onlineStatus = (OnlineStatus) filters.get("onlineStatus");
                predicates.add(cb.equal(root.get(FIELD_ONLINE_STATUS), onlineStatus));
            }
            if (filters.containsKey(FIELD_LICENSE_PLATE)) {
                //use coalesce to avoid the nullification of the final concatenation in case any of the Strings are null
                Expression<String> licensePlateExpression = cb.coalesce(cb.lower(root.get("carDO").get("licensePlate")), "''");
                String value = (String) filters.get(FIELD_LICENSE_PLATE);
                predicates.add(cb.like(licensePlateExpression, String.format("%s%s%s", "%", value.trim().toLowerCase(), "%")));
            }
            if(filters.containsKey(FIELD_SEAT_COUNT)){
                Integer seatCount = (Integer) filters.get(FIELD_SEAT_COUNT);
                predicates.add(cb.equal(root.get("carDO").get("seatCount"), seatCount));
            }
            if(filters.containsKey(FIELD_CONVERTIBLE)){
                Boolean convertible = (Boolean) filters.get(FIELD_CONVERTIBLE);
                predicates.add(cb.equal(root.get("carDO").get("convertible"), convertible));
            }
            if(filters.containsKey(FIELD_RATING)){
                Integer rating = (Integer) filters.get(FIELD_RATING);
                predicates.add(cb.equal(root.get("carDO").get("rating"), rating));
            }

            if(filters.containsKey(FIELD_ENGINE_TYPE)){
                EngineType engineType = (EngineType) filters.get(FIELD_ENGINE_TYPE);
                predicates.add(cb.equal(root.get("carDO").get("engineType"), engineType));
            }
//            if (filters.containsKey(FIELD_MANUFACTURER_NAME)) {
//                //use coalesce to avoid the nullification of the final concatenation in case any of the Strings are null
//                Expression<String> manufacturerNameExpression =
//                        cb.coalesce(cb.lower(root.join("carDO").join("manufacturerDO").get("name")), "''");
//                String value = (String) filters.get(FIELD_MANUFACTURER_NAME);
//                predicates.add(cb.like(manufacturerNameExpression, String.format("%s%s%s", "%", value.trim().toLowerCase(), "%")));
//            }









//            if (filters.containsKey(FIELD_USER_NAME)) {
//                //use coalesce to avoid the nullification of the final concatenation in case any of the Strings are null
//                Expression<String> usernameExpression = cb.coalesce(cb.lower(root.get("username")), "''");
//                String value = (String) filters.get(FIELD_USER_NAME);
//                predicates.add(cb.like(usernameExpression, String.format("%s%s%s", "%", value.trim().toLowerCase(), "%")));
//            }
//            if (filters.containsKey(FIELD_ONLINE_STATUS)) {
//                OnlineStatus onlineStatus = (OnlineStatus) filters.get("onlineStatus");
//                predicates.add(cb.equal(root.get(FIELD_ONLINE_STATUS), onlineStatus));
//            }
//            if (filters.containsKey(FIELD_LICENSE_PLATE)) {
//                //use coalesce to avoid the nullification of the final concatenation in case any of the Strings are null
//                Expression<String> licensePlateExpression = cb.coalesce(cb.lower(root.join("carDO").get("licensePlate")), "''");
//                String value = (String) filters.get(FIELD_LICENSE_PLATE);
//                predicates.add(cb.like(licensePlateExpression, String.format("%s%s%s", "%", value.trim().toLowerCase(), "%")));
//            }
//            if(filters.containsKey(FIELD_SEAT_COUNT)){
//                Integer seatCount = (Integer) filters.get(FIELD_SEAT_COUNT);
//                predicates.add(cb.equal(root.join("carDO").get("seatCount"), seatCount));
//            }
//            if(filters.containsKey(FIELD_CONVERTIBLE)){
//                Boolean convertible = (Boolean) filters.get(FIELD_CONVERTIBLE);
//                predicates.add(cb.equal(root.join("carDO").get("convertible"), convertible));
//            }
//            if(filters.containsKey(FIELD_RATING)){
//                Integer rating = (Integer) filters.get(FIELD_RATING);
//                predicates.add(cb.equal(root.join("carDO").get("rating"), rating));
//            }
//
//            if(filters.containsKey(FIELD_ENGINE_TYPE)){
//                EngineType engineType = (EngineType) filters.get(FIELD_ENGINE_TYPE);
//                predicates.add(cb.equal(root.join("carDO").get("engineType"), engineType));
//            }
//            if (filters.containsKey(FIELD_MANUFACTURER_NAME)) {
//                //use coalesce to avoid the nullification of the final concatenation in case any of the Strings are null
//                Expression<String> manufacturerNameExpression =
//                        cb.coalesce(cb.lower(root.join("carDO").join("manufacturerDO").get("name")), "''");
//                String value = (String) filters.get(FIELD_MANUFACTURER_NAME);
//                predicates.add(cb.like(manufacturerNameExpression, String.format("%s%s%s", "%", value.trim().toLowerCase(), "%")));
//            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
