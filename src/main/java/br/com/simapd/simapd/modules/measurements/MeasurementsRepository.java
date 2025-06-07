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

    List<MeasurementsEntity> findByAreaId(String areaId);

    @Query(value = """
            SELECT
                TRUNC(m.measured_at),
                AVG(JSON_VALUE(m.value, '$.numericValue')),
                COUNT(*),
                MIN(JSON_VALUE(m.value, '$.numericValue')),
                MAX(JSON_VALUE(m.value, '$.numericValue')),
                SUM(JSON_VALUE(m.value, '$.numericValue'))
            FROM measurement m
            WHERE (:sensorId IS NULL OR m.sensor_id = :sensorId)
            AND (:areaId IS NULL OR m.area_id = :areaId)
            AND (:startDate IS NULL OR TRUNC(m.measured_at) >= :startDate)
            AND (:endDate IS NULL OR TRUNC(m.measured_at) <= :endDate)
            GROUP BY TRUNC(m.measured_at)
            ORDER BY TRUNC(m.measured_at)
            """, nativeQuery = true)
    List<Object[]> findDailyAggregation(
            @Param("sensorId") String sensorId,
            @Param("areaId") String areaId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = """
            SELECT m FROM MeasurementsEntity m
            WHERE (:sensorId IS NULL OR m.sensorId = :sensorId)
            AND (:areaId IS NULL OR m.areaId = :areaId)
            AND (:type IS NULL OR m.type = :type)
            AND (:riskLevel IS NULL OR m.riskLevel = :riskLevel)
            ORDER BY m.measuredAt DESC
            """)
    List<MeasurementsEntity> findByFilters(
            @Param("sensorId") String sensorId,
            @Param("areaId") String areaId,
            @Param("type") Integer type,
            @Param("riskLevel") Integer riskLevel);
}
