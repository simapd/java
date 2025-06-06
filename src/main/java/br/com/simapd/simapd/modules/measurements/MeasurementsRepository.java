package br.com.simapd.simapd.modules.measurements;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementsRepository extends JpaRepository<MeasurementsEntity, String> {

    MeasurementsEntity findBySensorId(String sensorId);

    @Query(value = """
            SELECT
                TRUNC(m.measured_at),
                AVG(m.measurement_value),
                COUNT(*),
                MIN(m.measurement_value),
                MAX(m.measurement_value),
                SUM(m.measurement_value)
            FROM measurement m
            WHERE (:sensorId IS NULL OR m.sensor_id = :sensorId)
            AND (:startDate IS NULL OR TRUNC(m.measured_at) >= :startDate)
            AND (:endDate IS NULL OR TRUNC(m.measured_at) <= :endDate)
            GROUP BY TRUNC(m.measured_at)
            ORDER BY TRUNC(m.measured_at)
            """, nativeQuery = true)
    List<Object[]> findDailyAggregation(
            @Param("sensorId") String sensorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
