package br.com.simapd.simapd.modules.userReports;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportsRepository extends JpaRepository<UserReportsEntity, String> {

  Page<UserReportsEntity> findByAreaId(String areaId, Pageable pageable);

}
