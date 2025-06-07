package br.com.simapd.simapd.modules.measurements;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementsRepository extends JpaRepository<MeasurementsEntity, String> {

    List<MeasurementsEntity> findBySensorId(String sensorId);

    List<MeasurementsEntity> findByAreaId(String areaId);

    @Query("SELECT m FROM MeasurementsEntity m " +
           "WHERE (:sensorId IS NULL OR m.sensorId = :sensorId) " +
           "AND (:areaId IS NULL OR m.areaId = :areaId) " +
           "AND (:startDate IS NULL OR DATE(m.measuredAt) >= :startDate) " +
           "AND (:endDate IS NULL OR DATE(m.measuredAt) <= :endDate) " +
           "ORDER BY m.measuredAt")
    List<MeasurementsEntity> findMeasurementsForAggregation(
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
