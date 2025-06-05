package br.com.simapd.simapd.modules.userReports;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "user_report")
public class UserReportsEntity {

  @Id
  private String id;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "area_id", insertable = false, updatable = false)
  private RiskAreasEntity riskAreaEntity;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "area_id", nullable = true)
  private String areaId;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "description", nullable = false)
  @Size(max = 500, message = "Description must not exceed 500 characters")
  private String description;

  @Column(name = "location_info", nullable = true)
  @Size(max = 100, message = "Location info must not exceed 100 characters")
  private String locationInfo;

  @Column(name = "photo_url", nullable = true)
  @Size(max = 500, message = "Photo url must not exceed 500 characters")
  private String photoUrl;

  @Column(name = "is_verified", nullable = true)
  @Pattern(regexp = "^[01]$", message = "Must be 0 or 1")
  private String isVerified = "0";

  @Column(name = "reported_at", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime reportedAt;
}
