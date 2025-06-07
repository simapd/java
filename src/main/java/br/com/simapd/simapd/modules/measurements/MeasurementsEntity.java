package br.com.simapd.simapd.modules.measurements;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "measurement")
public class MeasurementsEntity {

  @Id
  private String id;

  @Column(name = "type", nullable = false)
  private Integer type;

  @Column(name = "value", nullable = false, columnDefinition = "CLOB")
  private String value;

  @Column(name = "risk_level", nullable = false)
  private Integer riskLevel;

  @Column(name = "measured_at", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime measuredAt;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "sensor_id", nullable = false)
  private String sensorId;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "area_id", nullable = false)
  private String areaId;
}
