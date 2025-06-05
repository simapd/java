package br.com.simapd.simapd.modules.users;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.simapd.simapd.modules.riskAreas.RiskAreasEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class UsersEntity implements UserDetails {

  @Id
  private String id;

  @Column(name = "name", nullable = false)
  @Size(max = 150, message = "Name must not exceed 150 characters")
  private String name;

  @Column(name = "email", nullable = false, unique = true)
  @Size(max = 150, message = "Email must not exceed 150 characters")
  @Email(message = "Invalid email format")
  private String email;

  @JsonIgnore
  @Column(name = "password", nullable = false)
  @Size(max = 100, message = "Password must not exceed 100 characters")
  private String password;

  @Pattern(regexp = "^[a-z0-9]{24}$", message = "Invalid CUID2 format")
  @Column(name = "area_id", nullable = false)
  private String areaId;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "area_id", insertable = false, updatable = false)
  private RiskAreasEntity riskAreaEntity;

  @Column(name = "created_at", nullable = false)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdAt;

  // UserDetails implementation
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
} 