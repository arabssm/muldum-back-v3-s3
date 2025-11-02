package co.kr.muldum.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface S3FileJpaRepository extends JpaRepository<S3FileJpaEntity, Long> {
}
