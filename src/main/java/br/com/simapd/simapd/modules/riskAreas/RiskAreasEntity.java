package br.com.simapd.simapd.modules.riskAreas;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "risk_area")
public class RiskAreasEntity {

  @Id
  private String id;

  @Column(name = "name", nullable = false)
  @Size(max = 100, message = "Name must not exceed 100 characters")
  private String name;

  @Column(name = "description", nullable = true)
  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  @Column(name = "latitude", nullable = false)
  @Size(max = 64, message = "Latitude must not exceed 64 characters")
  private String latitude;

  @Column(name = "longitude", nullable = false)
  @Size(max = 64, message = "Longitude must not exceed 64 characters")
  private String longitude;

  @Column(name = "created_at", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;
}
